package com.conoftherings.draft

import com.conoftherings.playercards.Card
import com.conoftherings.playercards.CardType
import com.conoftherings.playercards.Sphere
import spock.lang.Specification
import spock.lang.Unroll


class DraftSpec extends Specification {

	private final static Card GANDALF_HERO = new Card(0, "Gandalf", Sphere.NEUTRAL, CardType.HERO, "imageURL")
	private final static Card ARAGORN_HERO = new Card(0, "Aragorn", Sphere.TACTICS, CardType.HERO, "imageURL")
	private final static Card SMEAGOL_HERO = new Card(0, "Smeagol", Sphere.LORE, CardType.HERO, "imageURL")
	private final static Card FARAMIR_PC = new Card(4, "Faramir", Sphere.LEADERSHIP, CardType.ALLY, "imageURL")
	private final static Card SOG_PC = new Card(2, "Steward of Gondor", Sphere.LEADERSHIP, CardType.ATTACHMENT, "imageURL")
	private final static Card TOW_PC = new Card(1, "Test of Will", Sphere.SPIRIT, CardType.EVENT, "imageURL")
	private final static Card STORM_PC = new Card(0, "The Storm Comes", Sphere.NEUTRAL, CardType.SIDE_QUEST, "imageURL")

	@Unroll
	def "isPackValidAddition returns #expectedValidity when #scenario"() {
		given:
		int maxPlayerCardDuplicates = 3
		Settings settings = Mock()
		Draft draft = new Draft(settings: settings, status: status, heroes: heroes, playerCards: playerCards)
		Pack pack = Mock()

		when:
		boolean isPackValidAddition = draft.isPackValidAddition(pack)

		then:
		switch (status) {
			case DraftStatus.NEEDS_HEROES:
				1 * pack.getCards() >> packCards
				break
			case DraftStatus.NEEDS_PLAYER_CARDS:
				1 * settings.getMaxPlayerCardDuplication() >> maxPlayerCardDuplicates
				1 * pack.getCards() >> packCards
				break
			case DraftStatus.NEEDS_SPHERE_BALANCE:
				1 * pack.getCards() >> packCards
				break
		}
		0 * _
		isPackValidAddition == expectedValidity

		where:
		status                           | heroes                       | playerCards              | packCards                    | scenario                                                          || expectedValidity
		DraftStatus.NEEDS_HEROES         | []                           | []                       | [GANDALF_HERO]               | "pack contains a unique hero"                                     || true
		DraftStatus.NEEDS_HEROES         | [GANDALF_HERO]               | []                       | [GANDALF_HERO]               | "pack contains a duplicate hero"                                  || false
		DraftStatus.NEEDS_HEROES         | [GANDALF_HERO]               | []                       | [GANDALF_HERO, ARAGORN_HERO] | "pack contains a mixture of unique and duplicate heroes"          || true
		DraftStatus.NEEDS_PLAYER_CARDS   | []                           | []                       | [FARAMIR_PC]                 | "pack contains a distinct player card"                            || true
		DraftStatus.NEEDS_PLAYER_CARDS   | []                           | [FARAMIR_PC]             | [FARAMIR_PC]                 | "pack contains an additional copy of a player card below the max" || true
		DraftStatus.NEEDS_PLAYER_CARDS   | []                           | [TOW_PC, TOW_PC, TOW_PC] | [TOW_PC]                     | "pack contains an additional copy of a player card above the max" || false
		DraftStatus.NEEDS_PLAYER_CARDS   | []                           | [TOW_PC, TOW_PC, TOW_PC] | [TOW_PC, STORM_PC]           | "pack contains a distinct player card along with duplicates"      || true
		DraftStatus.NEEDS_SPHERE_BALANCE | [ARAGORN_HERO, SMEAGOL_HERO] | [SOG_PC]                 | [TOW_PC]                     | "pack contains the least used sphere"                             || true
		DraftStatus.NEEDS_SPHERE_BALANCE | [ARAGORN_HERO, SMEAGOL_HERO] | [SOG_PC]                 | [STORM_PC]                   | "pack does not contain the least used sphere"                     || false
	}

	@Unroll
	def "updateDraft adds cards appropriately when #scenario"() {
		given:
		int maxPlayerCardDuplication = 3
		Settings settings = Mock()
		Draft draftSpy = Spy(new Draft(settings: settings, status: DraftStatus.NEEDS_HEROES, heroes: heroes, playerCards: playerCards))
		Pack pack = Mock()

		when:
		draftSpy.updateDraft(pack)

		then:
		1 * draftSpy.updateDraft(pack)
		1 * draftSpy.isPackValidAddition(pack) >> isPackValid
		if (isPackValid) {
			1 * pack.getCards() >> packCards
			1 * draftSpy.determineDraftStatus() >> null
			_ * settings.getMaxPlayerCardDuplication() >> maxPlayerCardDuplication
		}
		0 * _

		draftSpy.getHeroes() == expectedDraftHeroes
		draftSpy.getPlayerCards() == expectedDraftPlayerCards


		where:
		heroes                       | playerCards              | packCards                    | isPackValid | scenario                                                          || expectedDraftHeroes          | expectedDraftPlayerCards
		[]                           | []                       | []                           | false       | "pack isn't valid"                                                || []                           | []
		[]                           | []                       | [GANDALF_HERO]               | true        | "pack contains a unique hero"                                     || [GANDALF_HERO]               | []
		[GANDALF_HERO]               | []                       | [GANDALF_HERO]               | true        | "pack contains a duplicate hero"                                  || [GANDALF_HERO]               | []
		[GANDALF_HERO]               | []                       | [GANDALF_HERO, ARAGORN_HERO] | true        | "pack contains a mixture of unique and duplicate heroes"          || [GANDALF_HERO, ARAGORN_HERO] | []
		[]                           | []                       | [FARAMIR_PC]                 | true        | "pack contains a distinct player card"                            || []                           | [FARAMIR_PC]
		[]                           | [FARAMIR_PC]             | [FARAMIR_PC]                 | true        | "pack contains an additional copy of a player card below the max" || []                           | [FARAMIR_PC, FARAMIR_PC]
		[]                           | [TOW_PC, TOW_PC, TOW_PC] | [TOW_PC]                     | true        | "pack contains an additional copy of a player card above the max" || []                           | [TOW_PC, TOW_PC, TOW_PC]
		[]                           | [TOW_PC, TOW_PC, TOW_PC] | [TOW_PC, STORM_PC]           | true        | "pack contains a distinct player card along with duplicates"      || []                           | [TOW_PC, TOW_PC, TOW_PC, STORM_PC]
		[ARAGORN_HERO, SMEAGOL_HERO] | [SOG_PC]                 | [TOW_PC]                     | true        | "pack contains the least used sphere"                             || [ARAGORN_HERO, SMEAGOL_HERO] | [SOG_PC, TOW_PC]
	}

	@Unroll
	def "determineDraftStatus will set draft's status to #expectedStatus when #scenario"() {
		given:
		int playerCount = 2
		Settings settings = Mock()
		Draft draft = new Draft(settings: settings, status: DraftStatus.DRAFT_VALID, heroes: heroes, playerCards: playerCards)

		when:
		draft.determineDraftStatus()

		then:
		1 * settings.getMinPlayerCardsPerPlayer() >> 1
		1 * settings.getPlayerCount() >> playerCount
		if (playerCards.size() > 1) {
			1 * settings.getMinHeroCards() >> 1
		}
		if (heroes.size() > 0) {
			1 * settings.getMinSphereBalanceRatio() >> 0.20f
		}
		0 * _

		draft.getStatus() == expectedStatus

		where:
		heroes                       | playerCards      | scenario                                    || expectedStatus
		[]                           | [TOW_PC]         | "draft doesn't contain enough player cards" || DraftStatus.NEEDS_PLAYER_CARDS
		[]                           | [TOW_PC, SOG_PC] | "draft doesn't contain enough hero cards"   || DraftStatus.NEEDS_HEROES
		[GANDALF_HERO]               | [TOW_PC, SOG_PC] | "draft doesn't contain sphere balance"      || DraftStatus.NEEDS_SPHERE_BALANCE
		[SMEAGOL_HERO, ARAGORN_HERO] | [TOW_PC, SOG_PC] | "draft is finished"                         || DraftStatus.DRAFT_VALID
	}

	@Unroll
	def "isValid returns #expectedValue when status is #status"() {
		given:
		Settings settings = Mock()
		Draft draft = new Draft(settings: settings, status: status, heroes: [], playerCards: [])

		when:
		boolean isValid = draft.isValid()

		then:
		isValid == expectedValue

		where:
		status                           || expectedValue
		DraftStatus.DRAFT_VALID          || true
		DraftStatus.NEEDS_HEROES         || false
		DraftStatus.NEEDS_PLAYER_CARDS   || false
		DraftStatus.NEEDS_SPHERE_BALANCE || false
	}
}
