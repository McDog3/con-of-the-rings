/**
 * DraftPackConstants
 * Copyright (c) 2019, FastBridge Learning LLC
 * Created on 2019-05-05
 */
package com.conoftherings.util;

import java.util.ArrayList;
import java.util.List;

public class DraftPackConstants {
//    private static final String DECK_1 = "11924";//public
    private static final String DECK_1 = "152586";//private
    private static final String DECK_2 = "152584";//private

    private static final List<String> ALL_DECK_IDS = new ArrayList<>();
    static {
        ALL_DECK_IDS.add(DECK_1);
        ALL_DECK_IDS.add(DECK_2);
    }

    public static List<String> getAllDeckIds() {
        return ALL_DECK_IDS;
    }
}
