package com.demo.playwright.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

/**
 * Central config source. Values come from (in priority order):
 *   1. System properties  (-Dbrowser=firefox)
 *   2. config.properties  (src/test/resources/config.properties)
 */
@Config.Sources("classpath:config.properties")
@Config.LoadPolicy(Config.LoadType.MERGE)
public interface ConfigManager extends Config {

    @Key("browser")
    @DefaultValue("chromium")
    String browser();

    @Key("headless")
    @DefaultValue("false")
    String headless();

    @Key("slow_mo")
    @DefaultValue("0")
    int slowMo();

    @Key("base_url")
    @DefaultValue("https://www.saucedemo.com")
    String baseUrl();

    @Key("timeout_ms")
    @DefaultValue("30000")
    int timeoutMs();

    static ConfigManager get() {
        return ConfigFactory.create(ConfigManager.class, System.getProperties());
    }
}
