package com.conoftherings.draft;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.conoftherings.playercards.Card;
import com.conoftherings.playercards.CardType;
import com.conoftherings.playercards.Sphere;

public class Draft {

    private static final int PLAYER_CARD_MIN = 70;
    private static final int HERO_CARD_MIN = 45;
    private static final float SPHERE_BALANCE_MIN = 0.18f;

    private int playerCount;
    private List<Card> heroes;
    private List<Card> playerCards;
    private DraftStatus status;

    public Draft(int playerCount) {
        this.playerCount = playerCount;
        this.heroes = new ArrayList<>();
        this.playerCards = new ArrayList<>();
        this.status = DraftStatus.NEEDS_PLAYER_CARDS;
    }

    //TODO expand this method to be "canPackAddToDraft" and check the other constraints
    public boolean canPackAddToPlayerCards(Pack pack) {
        for (Card packCard: pack.getCards()) {
            if (packCard.getType() != CardType.HERO && this.playerCards.stream().filter(packCard::equals).count() < 3) {
                return true;
            }
        }
        return false;
    }

    public void addPack(Pack pack) {
        for (Card packCard: pack.getCards()) {
            if (packCard.getType() == CardType.HERO) {
                //Allow a single copy of a hero, so when not present add to draft
                if (!this.heroes.stream().filter(packCard::equals).findFirst().isPresent()) {
                    this.heroes.add(packCard);
                }
            } else {
                //Allow 3 copies of everything else, so when not yet at 3 add to draft
                if (this.playerCards.stream().filter(packCard::equals).count() < 3) {
                    this.playerCards.add(packCard);
                }
            }
        }
        determineDraftStatus();
    }

    public boolean isDraftValid() {
        return this.status == DraftStatus.DRAFT_VALID;
    }

    private void determineDraftStatus() {
        DraftStatus draftStatus = null;
        float totalCardCount = Float.valueOf(this.playerCards.size() + this.heroes.size());

        //1 - We need at least 70 * playerCount player cards
        int playerDeckCards = this.playerCards.size();
        if (playerDeckCards < (PLAYER_CARD_MIN * this.playerCount)) {
            //we have failed at achieving enough player deck cards to pass the draft
            draftStatus = DraftStatus.NEEDS_PLAYER_CARDS;
        }

        //2 - We need 45 heroes (9 heroes/page * 5 pages)
        int heroCards = this.heroes.size();
        if (heroCards < HERO_CARD_MIN) {
            //we have failed at achieving enough heroes to pass the draft
            draftStatus = DraftStatus.NEEDS_HEROES;
        }

        //3 - Each sphere should not have any less than 20% representation
        for (Sphere sphere : Sphere.values()) {
            if (sphere == Sphere.NEUTRAL) {
                //Do not count Neutral as a sphere for this equation
                continue;
            }
            long sphereCount = Stream.of(this.playerCards, this.heroes).flatMap(List::stream).filter(card -> card.getSphere() == sphere).count();
            if (sphereCount / totalCardCount < SPHERE_BALANCE_MIN) {
                //we have failed at achieving a good sphere balance ratio!
                //FUTURE: do something about this
                draftStatus = DraftStatus.NEEDS_SPHERE_BALANCE;
                break;
            }
        }

        if (draftStatus == null) {
            draftStatus = DraftStatus.DRAFT_VALID;
        }

        this.status = draftStatus;
    }
}
