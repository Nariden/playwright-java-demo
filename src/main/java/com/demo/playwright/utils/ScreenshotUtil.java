package com.demo.playwright.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class ScreenshotUtil {

    private ScreenshotUtil() {}

    /** Captures a screenshot and attaches it to the current Allure report step. */
    public static void attachScreenshot(Page page, String name) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
    }
}
