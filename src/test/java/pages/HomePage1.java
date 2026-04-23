package pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.ShadowUtil;

public class HomePage1 {

	    private WebDriver driver;
	    private WebDriverWait wait;
	    private ShadowUtil shadow;

	    public HomePage1(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        this.shadow = new ShadowUtil(driver);

	        // Wait until homepage loads
	        wait.until(d ->
	            shadow.isDisplayed("shop-app", "shop-home")
	        );
	    }

	    // ===============================
	    // CART
	    // ===============================

	    public CartPage clickCart() {

	        shadow.click(
	            "shop-app",
	            "div.cart-btn-container",
	            "a"
	        );

	        CartPage cartPage = new CartPage(driver);
	        cartPage.waitForPageToLoad();

	        return cartPage;
	    }

	    public int getCartCount() {

	        String label = shadow.getShadowElement(
	            "shop-app",
	            "div.cart-btn-container",
	            "paper-icon-button"
	        ).getAttribute("aria-label");

	        // Example: Shopping cart: 2 items
	        String number = label.replaceAll("[^0-9]", "");

	        return number.isEmpty() ? 0 : Integer.parseInt(number);
	    }

	    // ===============================
	    // TOP MENU
	    // ===============================

	    public void clickMenMenu() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[href='/list/mens_outerwear']"
	        );
	    }

	    public void clickLadiesMenu() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[href='/list/ladies_outerwear']"
	        );
	    }

	    public void clickMenTshirt() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[href='/list/mens_tshirts']"
	        );
	    }

	    public void clickLadiesTshirt() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[href='/list/ladies_tshirts']"
	        );
	    }

	    // ===============================
	    // BANNERS
	    // ===============================

	    public void clickMenOuterwearBanner() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[aria-label*=\"Men's Outerwear Shop Now\"]"
	        );
	    }

	    public void clickLadiesOuterwearBanner() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[aria-label*=\"Ladies Outerwear Shop Now\"]"
	        );
	    }

	    public void clickMenTshirtBanner() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[aria-label*=\"Men's T-Shirts Shop Now\"]"
	        );
	    }

	    public void clickLadiesTshirtBanner() {
	        shadow.click(
	            "shop-app",
	            "shop-home",
	            "a[aria-label*=\"Ladies T-Shirts Shop Now\"]"
	        );
	    }

	    
	    public void clickMenuButton() {
	        shadow.click(
	            "shop-app",
	            "app-header",
	            "div.left-bar-item",
	            "paper-icon-button"
	        );
	    }

	    public boolean isMenuDrawerDisplayed() {
	        return shadow.isDisplayed(
	            "shop-app",
	            "app-drawer"
	        );
	    }
	    // ===============================
	    // SHOP NOW
	    // ===============================

	    public Productlist clickShopNow() {

	        clickMenMenu();

	        Productlist productlist = new Productlist(driver);
	        productlist.waitForPageToLoad();

	        return productlist;
	    }
	}

