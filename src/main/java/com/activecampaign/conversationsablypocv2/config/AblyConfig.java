package com.activecampaign.conversationsablypocv2.config;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AblyConfig {
    @Value("DznNIA.m22xtw:_sjlcy1vntHaDzWL")
    private String ablyApiKey;

    @Bean
    public AblyRest ablyRest() {
        try {
            return new AblyRest(ablyApiKey);
        } catch (AblyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public AblyRealtime ablyRealtime() {
        try {
            return new AblyRealtime(ablyApiKey);
        } catch (AblyException e) {
            e.printStackTrace();
        }
        return null;
    }
}