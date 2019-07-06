package com.conoftherings.draft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.conoftherings.playercards.Card;
import com.conoftherings.playercards.CardType;
import com.conoftherings.playercards.Sphere;

/**
 *
 */
public class Draft {
    private List<Card> heroes;
    private List<Card> playerCards;
    private DraftStatus status;
    private Settings settings;
    //1 - Hero does not already exist in draft (?) [Either I allow for multiple packs-worth of "Gandalf Guy" cards or we strictly allow one user's "Gandalf Guy" pack in the draft]
    //2 - The pack would add to the total player cards in the draft (ie not everything is a duplicate)
    //3 - The pack would keep the spheres in relative balance
    /**
     *
     *
     * @param settings
     */
    public Draft(Settings settings) {
        this.heroes = new ArrayList<>();
        this.playerCards = new ArrayList<>();
        this.status = DraftStatus.NEEDS_PLAYER_CARDS;
        this.settings = settings;
    }

    //For testing purposes
    public Draft() {
    }

    /**
     * This determines whether a draft pack is a valid addition to the draft pool.
     * Validity is determined by the draft's current {@link DraftStatus}, each status
     * requires different rules:
     * <br/>
     * <ul>
     *     <li>{@link DraftStatus#NEEDS_SPHERE_BALANCE} - will only accept the new pack if it contains cards for the least used sphere (in an attempt to even out the sphere distribution).</li>
     *     <li>{@link DraftStatus#NEEDS_HEROES} - will only accept the new pack if it contains a hero not already included in the draft pool.</li>
     *     <li>{@link DraftStatus#NEEDS_PLAYER_CARDS} - will only accept the new pack if it contains player cards not already included in the draft pool (or if the draft pool contains less than the maximum copies allowed for the card, which is 3 copies).</li>
     * </ul>
     *
     * @param pack - the pack to be considered for adding to draft pool
     *
     * @return whether or not the pack can be added
     */
    public boolean isPackValidAddition(Pack pack) {//TODO: Think about the repercussions of segmenting these cases. Should the pack be constrained by *all* of these?
        boolean willPackAddToDraft = false;
        switch (this.status) {
            case NEEDS_SPHERE_BALANCE:
                Sphere leastUsedSphere = determineLeastUsedSphere();
                if (leastUsedSphere != null) {
                    long newSphereCount = pack.getCards().stream().filter(card -> card.getSphere() == leastUsedSphere).count();
                    willPackAddToDraft = newSphereCount > 0;
                }
                break;
            case NEEDS_HEROES:
                long newHeroCount = pack.getCards().stream().filter(Card::isHero).filter(card -> !this.heroes.stream().anyMatch(card::equals)).count();
                //Will add if new heroes exist in pack
                willPackAddToDraft = newHeroCount > 0;
                break;
            case NEEDS_PLAYER_CARDS:
                int maxPlayerCardDuplicates = settings.getMaxPlayerCardDuplication();
                long newPlayerCardCount = pack.getCards().stream().filter(card -> !card.isHero()).filter(card -> this.playerCards.stream().filter(card::equals).count() < maxPlayerCardDuplicates).count();
                willPackAddToDraft = newPlayerCardCount > 0;
                break;
            case DRAFT_VALID:
                //Do not add cards if status is valid
                break;
            default:
                throw new IllegalStateException("Unknown status: " + this.status);
        }
        return willPackAddToDraft;
    }

    /**
     * Handles adding a {@link Pack} to the draft, iff the pack qualifies as a valid addition defined by {@link Draft#isPackValidAddition(Pack)}.
     * It will only add <b>unique</b> heroes and up to a maximum of 3 copies of any player card. In the case cards
     * are added to the draft, the draft's {@link DraftStatus} will be updated afterwards.
     *
     * @param pack - the pack to be added into the draft pool
     */
    public void updateDraft(Pack pack) {
        if (isPackValidAddition(pack)) {
            for (Card packCard: pack.getCards()) {
                if (packCard.getType() == CardType.HERO) {
                    //Allow a single copy of a hero, so when not present add to draft
                    if (!this.heroes.stream().filter(packCard::equals).findFirst().isPresent()) {
                        this.heroes.add(packCard);
                    }
                } else {
                    //Allow 3 copies of everything else, so when not yet at 3 add to draft
                    if (this.playerCards.stream().filter(packCard::equals).count() < settings.getMaxPlayerCardDuplication()) {
                        this.playerCards.add(packCard);
                    }
                }
            }
            determineDraftStatus();
        }
    }

    public boolean isValid() {
        return this.status == DraftStatus.DRAFT_VALID;
    }

    /**
     *
     */
    public void determineDraftStatus() {
        DraftStatus draftStatus = DraftStatus.DRAFT_VALID;
        float totalCardCount = Float.valueOf(this.playerCards.size() + this.heroes.size());

        //1 - We need at least minCardsPerPlayer * playerCount player cards
        int playerDeckCards = this.playerCards.size();
        if (playerDeckCards < (settings.getMinPlayerCardsPerPlayer() * this.settings.getPlayerCount())) {
            //we have failed at achieving enough player deck cards to pass the draft
            draftStatus = DraftStatus.NEEDS_PLAYER_CARDS;
        }

        //2 - We need 45 heroes (9 heroes/page * 5 pages)
        if (draftStatus == DraftStatus.DRAFT_VALID) {
            int heroCards = this.heroes.size();
            if (heroCards < settings.getMinHeroCards()) {
                //we have failed at achieving enough heroes to pass the draft
                draftStatus = DraftStatus.NEEDS_HEROES;
            }
        }

        //3 - Each sphere should maintain a a nearly equal ratio in the draft pool
        if (draftStatus == DraftStatus.DRAFT_VALID) {
            float minSphereBalanceRatio = settings.getMinSphereBalanceRatio();
            for (Sphere sphere : Sphere.values()) {
                if (sphere == Sphere.NEUTRAL) {
                    //Do not count Neutral as a sphere for this equation
                    continue;
                }
                long sphereCount = Stream.of(this.playerCards, this.heroes).flatMap(List::stream).map(Card::getSphere).filter(sphere::equals).count();
                if (sphereCount / totalCardCount < minSphereBalanceRatio) {
                    //we have failed at achieving a good sphere balance ratio!
                    draftStatus = DraftStatus.NEEDS_SPHERE_BALANCE;
                    break;
                }
            }
        }

        this.status = draftStatus;
    }

    private Sphere determineLeastUsedSphere() {
        Map<Sphere, Integer> sphereCounts = retrieveCurrentSphereCounts();
        Sphere leastUsedSphere = sphereCounts
                .entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return leastUsedSphere;
    }

    private Map<Sphere, Integer> retrieveCurrentSphereCounts() {
        Map<Sphere, Integer> sphereCounts = new HashMap<>();
        for (Sphere sphere : Sphere.values()) {
            if (sphere == Sphere.NEUTRAL) {
                //Do not count Neutral as a sphere for this equation
                continue;
            }
            Long sphereCount = Stream.of(this.playerCards, this.heroes).flatMap(List::stream).map(Card::getSphere).filter(sphere::equals).count();
            sphereCounts.put(sphere, sphereCount.intValue());
        }
        return sphereCounts;
    }

    public List<Card> getHeroes() {
        return heroes;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public DraftStatus getStatus() {
        return status;
    }
}
