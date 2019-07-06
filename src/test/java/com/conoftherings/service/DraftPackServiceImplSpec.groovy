package com.conoftherings.service

import com.conoftherings.config.DraftConfiguration
import com.conoftherings.draft.Draft
import com.conoftherings.draft.Pack
import com.conoftherings.draft.Settings
import com.conoftherings.playercards.Card
import com.conoftherings.playercards.CardType
import com.conoftherings.playercards.Sphere
import com.conoftherings.util.JSONUtil
import spock.lang.Specification


class DraftPackServiceImplSpec extends Specification {

	JSONUtil jsonUtil = Mock()
	DraftConfiguration draftConfiguration = Mock()

//	DraftPackService draftPackService = new DraftPackServiceImpl(jsonUtil, draftConfiguration)
	DraftPackServiceImpl draftPackServiceSpy = Spy(new DraftPackServiceImpl(jsonUtil, draftConfiguration))

	//TODO: implement tests
	def "createDraft throws exception when it can't build a valid draft"() {
		given:
		Settings settings = Mock()
		String draftPacksString = "allPacksString"
		List<Pack> draftPacks = []
		Draft draft = Mock()

		when:
		draftPackServiceSpy.createDraft(settings)

		then:
		1 * draftPackServiceSpy.createDraft(settings)
		1 * draftPackServiceSpy.retrieveAllDraftPacks() >> draftPacksString
		1 * draftPackServiceSpy.createRandomizedDraftPacks(draftPacksString) >> draftPacks
		1 * draftPackServiceSpy.createInitialDraft(settings) >> draft
		1 * draft.isValid() >> false
		0 * _

		def exception = thrown(RuntimeException.class)
		exception.getMessage() == "Couldn't finish building draft with packs available"
	}

	def "createDraft creates a valid draft"() {
		given:
		Settings settings = new Settings(2, 1, 4, 0.20f, 3)
		Draft initialDraft = new Draft(settings)
		String draftPacksString = "allPacksString"
		List<Pack> draftPacks = createDraftPacks()

		when:
		Draft draft = draftPackServiceSpy.createDraft(settings)

		then:
		1 * draftPackServiceSpy.createDraft(settings)
		1 * draftPackServiceSpy.retrieveAllDraftPacks() >> draftPacksString
		1 * draftPackServiceSpy.createRandomizedDraftPacks(draftPacksString) >> draftPacks
		1 * draftPackServiceSpy.createInitialDraft(settings) >> initialDraft
		0 * _

		draft.getPlayerCards().size() == 12 //3 copies of a card in each sphere, based on createDraftPacks
		draft.getHeroes().size() == 4 //4 total, based on createDraftPacks
		draft.isValid() == true
	}

	//TODO: Make the data less homogeneous. This lacks the variety we would have in the full scale drafting
	List<Pack> createDraftPacks() {
		//Create some cards
		Card aragorn = new Card(0, "Aragorn", Sphere.TACTICS, CardType.HERO, "imageURL")
		Card feint = new Card(1, "Feint", Sphere.TACTICS, CardType.EVENT, "imageURL")
		Card eowyn = new Card(0, "Eowyn", Sphere.SPIRIT, CardType.HERO, "imageURL")
		Card testOWill = new Card(1, "Test of Will", Sphere.SPIRIT, CardType.EVENT, "imageURL")
		Card radagast = new Card(0, "Radagast", Sphere.LORE, CardType.HERO, "imageURL")
		Card hound = new Card(2, "Loyal Hound", Sphere.LORE, CardType.ALLY, "imageURL")
		Card gildor = new Card(0, "Gildor", Sphere.LEADERSHIP, CardType.HERO, "imageURL")
		Card steward = new Card(0, "Steward of Gondor", Sphere.LEADERSHIP, CardType.ATTACHMENT, "imageURL")
		Pack pack1 = new Pack(1000l, "Me", "pack1", [aragorn, feint, feint, feint])
		Pack pack2 = new Pack(2000l, "Me", "pack2", [eowyn, testOWill, testOWill, testOWill])
		Pack pack3 = new Pack(3000l, "Me", "pack3", [radagast, hound, hound, hound])
		Pack pack4 = new Pack(4000l, "Me", "pack4", [gildor, steward, steward, steward])
		List<Pack> allDraftPacks = [pack1, pack2, pack3, pack4]
		return allDraftPacks
	}




}
