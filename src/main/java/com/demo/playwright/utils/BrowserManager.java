package com.demo.playwright.utils;

import com.demo.playwright.config.ConfigManager;
import com.microsoft.playwright.*;

/**
 * Owns the Playwright → Browser → BrowserContext → Page lifecycle.
 * One instance per test thread — keeps tests isolated.
 */
public class BrowserManager {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public void init() {
        ConfigManager config = ConfigManager.get();

        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(config.headless())
                .setSlowMo(config.slowMo());

        browser = switch (config.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(launchOptions);
            case "webkit"  -> playwright.webkit().launch(launchOptions);
            default        -> playwright.chromium().launch(launchOptions);
        };

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1280, 720)
                .setIgnoreHTTPSErrors(true));

        context.setDefaultTimeout(config.timeoutMs());

        page = context.newPage();
    }

    public Page getPage() {
        return page;
    }

    public BrowserContext getContext() {
        return context;
    }

    public void tearDown() {
        if (context != null) context.close();
        if (browser  != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
