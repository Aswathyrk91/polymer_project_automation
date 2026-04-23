package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.ShadowUtil;

public class HomePage {

    WebDriver driver;
    WebDriverWait wait;
    ShadowUtil shadow;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.shadow = new ShadowUtil(driver);

        // ✅ Ensure homepage is loaded (SAFE WAIT)
        wait.until(d -> {
            try {
                return shadow.isDisplayed("shop-app", "shop-home");
            } catch (Exception e) {
                return false;
            }
        });
    }
    
    public CartPage clickCart() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement shopApp = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
        );

        SearchContext shopAppShadow = (SearchContext) js.executeScript(
            "return arguments[0].shadowRoot", shopApp
        );

        WebElement cartLink = shopAppShadow
            .findElement(By.cssSelector("app-header"))
            .findElement(By.cssSelector("app-toolbar"))
            .findElement(By.cssSelector("div.cart-btn-container"))
            .findElement(By.cssSelector("a"));

        // ✅ FIX 1: wait until visible
        wait.until(ExpectedConditions.visibilityOf(cartLink));

        // ✅ FIX 2: scroll into view (CRITICAL)
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", cartLink);

        // small stabilization (Polymer animation)
        try { Thread.sleep(300); } catch (InterruptedException e) {}

        // ✅ FIX 3: JS click (MOST IMPORTANT)
        js.executeScript("arguments[0].click();", cartLink);

        CartPage cartPage = new CartPage(driver);
        cartPage.waitForPageToLoad();

        return cartPage;
    }
    public int getCartCount() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // shop-app
        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
        SearchContext appShadow = (SearchContext) js.executeScript(
            "return arguments[0].shadowRoot", shopApp
        );

        // app-toolbar (NOT app-header)
        WebElement toolbar = appShadow.findElement(By.cssSelector("app-toolbar"));

        // paper-icon-button
        WebElement cartButton = toolbar
            .findElement(By.cssSelector("div.cart-btn-container"))
            .findElement(By.cssSelector("paper-icon-button"));

        // ✅ get aria-label
        String label = cartButton.getAttribute("aria-label");

        System.out.println("🛒 Cart label: " + label);

        // Example: "Shopping cart: 1 item"
        String number = label.replaceAll("[^0-9]", "");

        return number.isEmpty() ? 0 : Integer.parseInt(number);
    }

    // ✅ Top menu clicks
    public void clickMenMenu() {
        shadow.click("shop-app", "shop-home", "a[href='/list/mens_outerwear']");
    }

    public void clickLadiesMenu() {
        shadow.click("shop-app", "shop-home", "a[href='/list/ladies_outerwear']");
    }

    public void clickMenTshirt() {
        shadow.click("shop-app", "shop-home", "a[href='/list/mens_tshirts']");
    }

    public void clickLadiesTshirt() {
        shadow.click("shop-app", "shop-home", "a[href='/list/ladies_tshirts']");
    }

    // ✅ Banner clicks
    public void clickMenOuterwearBanner() {
        shadow.click("shop-app", "shop-home",
            "a[aria-label*=\"Men's Outerwear Shop Now\"]");
    }

    public void clickLadiesOuterwearBanner() {
        shadow.click("shop-app", "shop-home",
            "a[aria-label*=\"Ladies Outerwear Shop Now\"]");
    }

    public void clickMenTshirtBanner() {
        shadow.click("shop-app", "shop-home",
            "a[aria-label*=\"Men's T-Shirts Shop Now\"]");
    }

    public void clickLadiesTshirtBanner() {
        shadow.click("shop-app", "shop-home",
            "a[aria-label*=\"Ladies T-Shirts Shop Now\"]");
    }

    // ✅ Navigate to product list page (NO WAIT HERE)
    public Productlist clickShopNow() {

        shadow.click("shop-app", "shop-home",
            "a[href='/list/mens_outerwear']");

        Productlist productlist = new Productlist(driver);

        productlist.waitForPageToLoad();  // ✅ WAIT HERE

        return productlist;
    }
}