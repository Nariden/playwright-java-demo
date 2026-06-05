package com.demo.playwright.tests;

import com.demo.playwright.base.BaseTest;
import com.demo.playwright.pages.InventoryPage;
import com.demo.playwright.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {

    private static final String VALID_USER     = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";

    // -------------------------------------------------------------------------
    // SMOKE
    // -------------------------------------------------------------------------

    @Test(groups = "smoke")
    @Story("Valid login")
    @Description("Standard user logs in and lands on the inventory page")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessfulLogin() {
        InventoryPage inventoryPage = loginPage()
                .open(baseUrl())
                .loginAs(VALID_USER, VALID_PASSWORD);

        assertThat(inventoryPage.isLoaded())
                .as("Should land on inventory page after login")
                .isTrue();

        assertThat(inventoryPage.getHeading())
                .as("Page heading")
                .isEqualTo("Products");
    }

    // -------------------------------------------------------------------------
    // REGRESSION
    // -------------------------------------------------------------------------

    @Test(groups = "regression")
    @Story("Locked out user")
    @Severity(SeverityLevel.CRITICAL)
    public void testLockedOutUser() {
        LoginPage login = loginPage()
                .open(baseUrl())
                .attemptLoginAs("locked_out_user", VALID_PASSWORD);

        assertThat(login.isErrorDisplayed())
                .as("Error banner should be visible")
                .isTrue();

        assertThat(login.getErrorMessage())
                .as("Error message text")
                .contains("Sorry, this user has been locked out");
    }

    @Test(groups = "regression", dataProvider = "invalidCredentials")
    @Story("Invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidCredentials(String username, String password, String expectedError) {
        LoginPage login = loginPage()
                .open(baseUrl())
                .attemptLoginAs(username, password);

        assertThat(login.isErrorDisplayed())
                .as("Error banner should be visible for user '%s'", username)
                .isTrue();

        assertThat(login.getErrorMessage())
                .as("Error message")
                .contains(expectedError);
    }

    @Test(groups = "regression")
    @Story("Empty credentials")
    @Severity(SeverityLevel.MINOR)
    public void testEmptyUsernameShowsError() {
        LoginPage login = loginPage()
                .open(baseUrl())
                .attemptLoginAs("", VALID_PASSWORD);

        assertThat(login.getErrorMessage())
                .contains("Username is required");
    }

    @Test(groups = "regression")
    @Story("Empty credentials")
    @Severity(SeverityLevel.MINOR)
    public void testEmptyPasswordShowsError() {
        LoginPage login = loginPage()
                .open(baseUrl())
                .attemptLoginAs(VALID_USER, "");

        assertThat(login.getErrorMessage())
                .contains("Password is required");
    }

    // -------------------------------------------------------------------------
    // DATA PROVIDERS
    // -------------------------------------------------------------------------

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
            // username          password          expected error fragment
            { "wrong_user",     VALID_PASSWORD,   "Username and password do not match" },
            { VALID_USER,       "wrong_pass",     "Username and password do not match" },
            { "wrong_user",     "wrong_pass",     "Username and password do not match" },
        };
    }
}
