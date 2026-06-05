package com.demo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Every page object extends this.
 * Holds the shared Page reference and wraps Playwright's raw API
 * into helper methods that read like plain English.
 */
public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        page.locator(selector).click();
    }

    protected void fill(String selector, String value) {
        page.locator(selector).fill(value);
    }

    protected String getText(String selector) {
        return page.locator(selector).innerText();
    }

    protected boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    protected Locator locate(String selector) {
        return page.locator(selector);
    }

    public String getTitle() {
        return page.title();
    }
}
