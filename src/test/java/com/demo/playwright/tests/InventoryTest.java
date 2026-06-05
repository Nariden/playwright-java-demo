package com.demo.playwright.tests;

import com.demo.playwright.base.BaseTest;
import com.demo.playwright.pages.InventoryPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Shopping")
@Feature("Inventory")
public class InventoryTest extends BaseTest {

    private static final String VALID_USER     = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";

    private InventoryPage inventoryPage;

    // Log in once before each test in this class
    @BeforeMethod(alwaysRun = true)
    public void login() {
        inventoryPage = loginPage()
                .open(baseUrl())
                .loginAs(VALID_USER, VALID_PASSWORD);
    }

    // -------------------------------------------------------------------------
    // SMOKE
    // -------------------------------------------------------------------------

    @Test(groups = "smoke")
    @Story("Page loads")
    @Severity(SeverityLevel.BLOCKER)
    public void testInventoryPageLoads() {
        assertThat(inventoryPage.isLoaded())
                .as("Inventory page URL check")
                .isTrue();

        assertThat(inventoryPage.getHeading())
                .isEqualTo("Products");
    }

    @Test(groups = "smoke")
    @Story("Products displayed")
    @Severity(SeverityLevel.CRITICAL)
    public void testSixProductsAreDisplayed() {
        assertThat(inventoryPage.getProductCount())
                .as("Sauce Demo always shows 6 products")
                .isEqualTo(6);
    }

    // -------------------------------------------------------------------------
    // REGRESSION
    // -------------------------------------------------------------------------

    @Test(groups = "regression")
    @Story("Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddSingleItemToCart() {
        inventoryPage.addToCart("Sauce Labs Backpack");

        assertThat(inventoryPage.getCartCount())
                .as("Cart badge count after adding one item")
                .isEqualTo(1);
    }

    @Test(groups = "regression", dataProvider = "cartScenarios")
    @Story("Add to cart")
    @Severity(SeverityLevel.NORMAL)
    public void testAddMultipleItemsToCart(String[] products, int expectedCount) {
        for (String product : products) {
            inventoryPage.addToCart(product);
        }

        assertThat(inventoryPage.getCartCount())
                .as("Cart badge after adding %d items", expectedCount)
                .isEqualTo(expectedCount);
    }

    @Test(groups = "regression")
    @Story("Products displayed")
    @Severity(SeverityLevel.NORMAL)
    public void testProductNamesAreNotEmpty() {
        assertThat(inventoryPage.getProductNames())
                .as("All product names should be non-blank")
                .isNotEmpty()
                .allSatisfy(name -> assertThat(name).isNotBlank());
    }

    // -------------------------------------------------------------------------
    // DATA PROVIDERS
    // -------------------------------------------------------------------------

    @DataProvider(name = "cartScenarios")
    public Object[][] cartScenarios() {
        return new Object[][] {
            { new String[]{ "Sauce Labs Backpack", "Sauce Labs Bike Light" },                      2 },
            { new String[]{ "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt" }, 3 },
        };
    }
}
