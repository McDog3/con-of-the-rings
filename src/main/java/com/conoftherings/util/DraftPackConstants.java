/**
 * DraftPackConstants
 * Copyright (c) 2019, FastBridge Learning LLC
 * Created on 2019-05-05
 */
package com.conoftherings.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DraftPackConstants {

    private static final List<String> ALL_DECK_IDS = new ArrayList<>();
    static {
        List<String> erikPacks = Arrays.asList("154372", "154402", "154403", "154400", "154398", "154392", "154373");
        List<String> robPacks = Arrays.asList("153978");
        List<String> myPacks = Arrays.asList("138880", "138877", "138875", "138873", "138872", "138871", "138867", "138870", "138869", "152255", "152256", "152257", "152258", "152259", "152260", "152262", "152578", "152581", "152584", "152585", "152586", "153659", "153660", "153661");
        ALL_DECK_IDS.addAll(erikPacks);
        ALL_DECK_IDS.addAll(robPacks);
        ALL_DECK_IDS.addAll(myPacks);
    }

    public static List<String> getAllDeckIds() {
        return ALL_DECK_IDS;
    }
}
