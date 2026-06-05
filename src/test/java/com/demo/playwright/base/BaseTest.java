package com.demo.playwright.base;

import com.demo.playwright.config.ConfigManager;
import com.demo.playwright.pages.LoginPage;
import com.demo.playwright.utils.BrowserManager;
import com.demo.playwright.utils.ScreenshotUtil;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * All test classes extend this.
 * Handles browser setup/teardown and automatic screenshots on failure.
 */
public abstract class BaseTest {

    // ThreadLocal makes each parallel test thread get its own browser instance
    private final ThreadLocal<BrowserManager> browserManager = new ThreadLocal<>();

    protected Page page() {
        return browserManager.get().getPage();
    }

    protected LoginPage loginPage() {
        return new LoginPage(page());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        BrowserManager manager = new BrowserManager();
        manager.init();
        browserManager.set(manager);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            Allure.step("Capturing screenshot on failure");
            ScreenshotUtil.attachScreenshot(page(), "Failure - " + result.getName());
        }
        browserManager.get().tearDown();
        browserManager.remove();
    }

    protected String baseUrl() {
        return ConfigManager.get().baseUrl();
    }
}
