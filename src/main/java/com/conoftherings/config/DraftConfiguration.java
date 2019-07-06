/**
 * DraftConfiguration
 * Copyright (c) 2019, FastBridge Learning LLC
 * Created on 2019-07-02
 */
package com.conoftherings.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class DraftConfiguration {

    @Value("classpath:static/json/draft-packs.json")
    private Resource resourceFile;

    public Resource getResourceFile() {
        return resourceFile;
    }
}
