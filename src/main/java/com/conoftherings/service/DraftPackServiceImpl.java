package com.conoftherings.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conoftherings.config.DraftConfiguration;
import com.conoftherings.draft.Draft;
import com.conoftherings.draft.Pack;
import com.conoftherings.draft.Settings;
import com.conoftherings.util.JSONUtil;

@Service
public class DraftPackServiceImpl implements DraftPackService {

    private static final Logger logger = LoggerFactory.getLogger(DraftPackServiceImpl.class);

    //TODO: We will replace this with a database eventually, DraftPackServiceImplSpec will work around this in the meantime
//    @Value("classpath:static/json/draft-packs.json")
//    private Resource resourceFile;
    private DraftConfiguration draftConfiguration;
    private JSONUtil jsonUtil;

    @Autowired
    public DraftPackServiceImpl(JSONUtil jsonUtil, DraftConfiguration draftConfiguration) {
        this.jsonUtil = jsonUtil;
        this.draftConfiguration = draftConfiguration;
    }

    /**
     * This will go through the process of creating a randomized draft pool, with the following steps:
     * <br/>
     * <ol>
     *     <li>Grab (and remove) a pack from the full list of draft packs</li>
     *     <li>Check that pack against the current draft contents</li>
     *     <li>If it matches criteria defined within {@link Draft#isPackValidAddition(Pack)} then add to draft. Otherwise return to Step 1.</li>
     *     <li>Check draft validity. If valid, as defined by {@link Draft#isValid()}, then return the draft. Otherwise return to Step 1.</li>
     * </ol>
     *
     * @param draftSettings - The settings required for this draft
     *
     * @return the randomized {@link Draft}
     */
    @Override
    public Draft createDraft(Settings draftSettings) {
        String packs = retrieveAllDraftPacks();
        List<Pack> allDraftPacks = createRandomizedDraftPacks(packs);
        Draft draft = createInitialDraft(draftSettings);

        while (!draft.isValid()) {
            //If we run out of draft packs, error
            if (allDraftPacks.isEmpty()) {
                throw new RuntimeException("Couldn't finish building draft with packs available");
            }
            //Otherwise, add first pack to draft
            Pack nextPack = allDraftPacks.remove(0);
            draft.updateDraft(nextPack);
        }

        //We now have a valid draft with enough cards of each type
        logger.info("DRAFT SUCCESSFUL: heroes: " + draft.getHeroes().size() + " player cards: " + draft.getPlayerCards().size());
        return draft;
    }

    //Visible for testing
    String retrieveAllDraftPacks() {
        InputStream resource;
        String packs = "";
        try {
            resource = draftConfiguration.getResourceFile().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            packs = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error("Could not read draft-packs json", e);
        }
        return packs;
    }

    //Visible for testing
    List<Pack> createRandomizedDraftPacks(String packsJSON) {
        //Create Card objects from JSON/HTML
        //Build Pack objects from the Cards and JSON/HTML
        List<Pack> allDraftPacks = jsonUtil.parseDraftPacks(packsJSON);
        Collections.shuffle(allDraftPacks);
        return allDraftPacks;
    }

    Draft createInitialDraft(Settings draftSettings) {
        return new Draft(draftSettings);
    }

}
