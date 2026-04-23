package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.ShadowUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;


public class ProductDesc {

	
	WebDriver driver;
    WebDriverWait wait;
    ShadowUtil shadowUtil;

    public ProductDesc(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.shadowUtil = new ShadowUtil(driver);
    }

    // ✅ Wait for detail page
    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.urlContains("/detail/"));
        System.out.println("✅ Product detail page loaded");
    }

    // ✅ Example: get product title
    public String getProductTitle() {
        // (you will inspect DOM and implement similar to previous)
        return driver.getTitle(); // placeholder for now
    }
    
    private SearchContext getDetailShadow() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement shopApp = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
        );

        SearchContext appShadow = (SearchContext) js.executeScript(
            "return arguments[0].shadowRoot", shopApp
        );

        WebElement shopDetail = appShadow.findElement(By.cssSelector("shop-detail"));

        return wait.until(d -> {
            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopDetail);
            return shadow != null ? (SearchContext) shadow : null;
        });
    }
    
    public String singleProductTitle() {
        SearchContext shadow = getDetailShadow();

        String title = shadow
            .findElement(By.cssSelector("div.detail h1"))
            .getText();

        System.out.println("🧾 Title: " + title);
        return title;
    }
    public String getProductPrice() {
        SearchContext shadow = getDetailShadow();

        String price = shadow
            .findElement(By.cssSelector("div.price"))
            .getText();

        System.out.println("💲 Price: " + price);
        return price;
    }
    
    public double getProductPriceValue() {

        String priceText = getProductPrice();   // "$50.20"

        String value = priceText.replaceAll("[^0-9.]", "");

        return Double.parseDouble(value);
    }
    
    public String getSizeLabel() {
        SearchContext shadow = getDetailShadow();

        return shadow
            .findElement(By.cssSelector("#sizeLabel"))
            .getText();
    }
    public String getSelectedSize() {
        SearchContext shadow = getDetailShadow();

        return shadow
            .findElement(By.cssSelector("#sizeSelect"))
            .getAttribute("value");
    }
    public String getQuantityLabel() {
        SearchContext shadow = getDetailShadow();

        return shadow
            .findElement(By.cssSelector("#quantityLabel"))
            .getText();
    }
    public String getQuantity() {
        SearchContext shadow = getDetailShadow();

        return shadow
            .findElement(By.cssSelector("#quantitySelect"))
            .getAttribute("value");
    }
    
    public void verifyAllSizesSelectable() {
        SearchContext shadow = getDetailShadow();

        Select select = new Select(
            shadow.findElement(By.cssSelector("#sizeSelect"))
        );

        for (WebElement option : select.getOptions()) {
            String value = option.getAttribute("value");

            select.selectByValue(value);

            System.out.println("✅ Selected size: " + value);
        }
    }
    
    public String getDescriptionLabel() {
        SearchContext shadow = getDetailShadow();

        return shadow
            .findElement(By.cssSelector("div.description h2, div.description label"))
            .getText();
    }
    
    public String getDescriptionText() {
        SearchContext shadow = getDetailShadow();

        String desc = shadow
            .findElement(By.cssSelector("div.description p"))
            .getText();

        System.out.println("📄 Description: " + desc);
        return desc;
    }
    
   /* public boolean isImageLoaded() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        SearchContext shadow = getDetailShadow();

        WebElement img = shadow.findElement(By.cssSelector("shop-image img"));

        return (Boolean) js.executeScript(
            "return arguments[0].complete && arguments[0].naturalWidth > 0", img
        );
    }*/
    public void addToCart() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
        SearchContext appShadow = (SearchContext) js.executeScript("return arguments[0].shadowRoot", shopApp);

        WebElement shopDetail = appShadow.findElement(By.cssSelector("shop-detail"));
        SearchContext detailShadow = wait.until(d -> {
            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopDetail);
            return shadow != null ? (SearchContext) shadow : null;
        });

        WebElement addToCartBtn = detailShadow.findElement(By.cssSelector("button"));

        addToCartBtn.click();

        System.out.println("✅ Product added to cart");
    }
    
    private SearchContext getCartPopupShadow() {

        WebElement popup = driver.findElement(
            By.cssSelector("shop-cart-modal.opened")
        );

        JavascriptExecutor js =
            (JavascriptExecutor) driver;

        return (SearchContext) js.executeScript(
            "return arguments[0].shadowRoot",
            popup
        );
    }
    public boolean isCartPopupDisplayed() {

        try {
            return shadowUtil
                .getVisibleShadowElement(
                    "shop-app",
                    "shop-cart-modal.opened"
                )
                .isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }
    
    public String getCartPopupMessage() {

        return shadowUtil
            .getVisibleShadowElement(
                "shop-app",
                "shop-cart-modal.opened",
                "div.label"
            )
            .getText()
            .trim();
    }
    
    public CartPage clickViewCartFromPopup() {

        WebDriverWait wait =
            new WebDriverWait(driver, Duration.ofSeconds(10));

        JavascriptExecutor js =
            (JavascriptExecutor) driver;

        WebElement shopApp =
            driver.findElement(By.cssSelector("shop-app"));

        SearchContext appShadow =
            shopApp.getShadowRoot();

        WebElement popup =
            wait.until(d ->
                appShadow.findElement(
                    By.cssSelector("shop-cart-modal.opened"))
            );

        SearchContext popupShadow =
            popup.getShadowRoot();

        WebElement viewCartLink =
            popupShadow.findElement(
                By.cssSelector("#viewCartAnchor")
            );

        js.executeScript(
            "arguments[0].click();",
            viewCartLink
        );

        wait.until(
            ExpectedConditions.urlContains("/cart")
        );

        return new CartPage(driver);
    }
    public CheckoutPage clickCheckoutFromPopup() {

        WebDriverWait wait =
            new WebDriverWait(driver, Duration.ofSeconds(10));

        JavascriptExecutor js =
            (JavascriptExecutor) driver;

        WebElement shopApp =
            driver.findElement(By.cssSelector("shop-app"));

        SearchContext appShadow =
            shopApp.getShadowRoot();

        WebElement popup =
            wait.until(d ->
                appShadow.findElement(
                    By.cssSelector("shop-cart-modal.opened"))
            );

        SearchContext popupShadow =
            popup.getShadowRoot();

        WebElement checkoutLink =
            popupShadow.findElement(
                By.cssSelector("a[href='/checkout']")
            );

        js.executeScript(
            "arguments[0].click();",
            checkoutLink
        );

        wait.until(
            ExpectedConditions.urlContains("/checkout")
        );

        return new CheckoutPage(driver);
    }
    public void closeCartPopup() {

        shadowUtil.click(
            "shop-app",
            "shop-cart-modal.opened",
            "#closeBtn"
        );
    }
}

