package com.demo.playwright.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.List;

/**
 * Represents https://www.saucedemo.com/inventory.html
 * The product listing page shown after a successful login.
 */
public class InventoryPage extends BasePage {

    private static final String PAGE_TITLE       = ".title";
    private static final String PRODUCT_NAMES    = ".inventory_item_name";
    private static final String CART_BADGE       = ".shopping_cart_badge";
    private static final String ADD_TO_CART_BTN  = "//div[text()='%s']/ancestor::div[@class='inventory_item']//button";

    public InventoryPage(Page page) {
        super(page);
    }

    public boolean isLoaded() {
        return page.url().contains("/inventory.html");
    }

    @Step("Get page heading")
    public String getHeading() {
        return getText(PAGE_TITLE);
    }

    @Step("Get all product names")
    public List<String> getProductNames() {
        return locate(PRODUCT_NAMES).allInnerTexts();
    }

    public int getProductCount() {
        return locate(PRODUCT_NAMES).count();
    }

    @Step("Add '{productName}' to cart")
    public InventoryPage addToCart(String productName) {
        String xpath = String.format(ADD_TO_CART_BTN, productName);
        page.locator(xpath).click();
        return this;
    }

    public int getCartCount() {
        if (!isVisible(CART_BADGE)) return 0;
        return Integer.parseInt(getText(CART_BADGE));
    }
}
