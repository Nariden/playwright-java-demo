package com.demo.playwright.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Represents https://www.saucedemo.com (the login screen).
 *
 * Rule of Page Object Model:
 *  - This class knows WHERE elements are (selectors).
 *  - Tests know WHAT to do (call methods).
 *  - Tests never touch selectors directly.
 */
public class LoginPage extends BasePage {

    // Selectors — centralised here so a UI change means editing one place only
    private static final String USERNAME_INPUT  = "#user-name";
    private static final String PASSWORD_INPUT  = "#password";
    private static final String LOGIN_BUTTON    = "#login-button";
    private static final String ERROR_MESSAGE   = "[data-test='error']";

    public LoginPage(Page page) {
        super(page);
    }

    @Step("Navigate to login page")
    public LoginPage open(String baseUrl) {
        page.navigate(baseUrl);
        return this;
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        fill(USERNAME_INPUT, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        fill(PASSWORD_INPUT, password);
        return this;
    }

    @Step("Click login button")
    public InventoryPage clickLogin() {
        click(LOGIN_BUTTON);
        return new InventoryPage(page);
    }

    /**
     * Convenience method: combines the three steps above.
     * Returns InventoryPage because a successful login always lands there.
     */
    @Step("Login as {username}")
    public InventoryPage loginAs(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    /**
     * Use this when you expect login to FAIL (wrong credentials).
     * Returns LoginPage because the page doesn't change on failure.
     */
    @Step("Attempt login as {username} (expecting failure)")
    public LoginPage attemptLoginAs(String username, String password) {
        fill(USERNAME_INPUT, username);
        fill(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return this;
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}
