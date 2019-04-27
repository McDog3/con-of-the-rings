package com.conoftherings.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.conoftherings.draft.Pack;

@Service
public class DraftPackServiceImpl implements DraftPackService {

    @Value("classpath:static/json/draft-packs.json")
    Resource resourceFile;

    @Override
    public void createDraft() {
        //TODO: DraftPackManager logic

        //Parse JSON/HTML

        InputStream resource = null;
        try {
            resource = resourceFile.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));

            String packs = reader.lines().collect(Collectors.joining("\n"));
            System.out.println(packs);
            parseDraftPacks(packs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create Card objects from JSON/HTML

        //Build Pack objects from the Cards and JSON/HTML

        //Validate the full draft has enough cards and various other properties
    }

    private List<Pack> parseDraftPacks(String draftPacks) {
        BasicJsonParser parser = new BasicJsonParser();
        List<Object> decks = parser.parseList(draftPacks);

        List<Map<String, Object>> draftDecks = decks.stream().map(object -> parser.parseMap((String)object)).collect(Collectors.toList());
        //TODO: parse draftDecks into List<Pack>
        return new ArrayList<>();
    }
}
