package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
	WebDriver driver;
    WebDriverWait wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.urlContains("/cart"));
    }

    // ✅ Verify empty cart message
    public boolean isCartEmpty() {
    	 JavascriptExecutor js = (JavascriptExecutor) driver;
    	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    	    // shop-app
    	    WebElement shopApp = wait.until(
    	        ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
    	    );

    	    SearchContext appShadow = wait.until(d -> {
    	        Object shadow = js.executeScript("return arguments[0].shadowRoot", shopApp);
    	        return shadow != null ? (SearchContext) shadow : null;
    	    }
    	    );

    	    // shop-cart
    	    WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

    	    SearchContext cartShadow = wait.until(d -> {
    	        Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
    	        return shadow != null ? (SearchContext) shadow : null;
    	    }
    	    );

    	    // ✅ TARGET EXACT ELEMENT
    	    WebElement emptyMsg = cartShadow.findElement(By.cssSelector("p.empty-cart"));

    	    String text = emptyMsg.getText();
    	    System.out.println("🛒 Cart text: " + text);

    	    return cartShadow.findElement(By.cssSelector("p.empty-cart")).isDisplayed();

    }
    
    public void removeItem() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // shop-app
        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
        SearchContext appShadow = (SearchContext) js.executeScript(
            "return arguments[0].shadowRoot", shopApp
        );

        // shop-cart
        WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

        SearchContext cartShadow = wait.until(d -> {
            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
            return shadow != null ? (SearchContext) shadow : null;
        });

        // shop-cart-item
        WebElement cartItem = wait.until(d ->
            cartShadow.findElement(By.cssSelector("shop-cart-item"))
        );

        SearchContext itemShadow = wait.until(d -> {
            Object shadow = js.executeScript("return arguments[0].shadowRoot", cartItem);
            return shadow != null ? (SearchContext) shadow : null;
        });

        // ✅ CORRECT delete button
        WebElement deleteBtn = itemShadow.findElement(
            By.cssSelector("paper-icon-button.delete-button")
        );

        // ✅ JS click (important)
        js.executeScript("arguments[0].click();", deleteBtn);

        System.out.println("🗑 Item removed from cart");
    }
        
        private SearchContext getCartItemShadow() {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            WebElement cartItem = wait.until(d ->
                cartShadow.findElement(By.cssSelector("shop-cart-item"))
            );

            return wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", cartItem);
                return shadow != null ? (SearchContext) shadow : null;
            });
        }
        public String getCartProductName() {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // shop-app
            WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            // shop-cart
            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // shop-cart-item
            WebElement cartItem = wait.until(d ->
                cartShadow.findElement(By.cssSelector("shop-cart-item"))
            );

            SearchContext itemShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", cartItem);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // ✅ CORRECT locator from your console
            WebElement name = itemShadow
                .findElement(By.cssSelector("div.name"))
                .findElement(By.cssSelector("a"));

            String text = name.getText();

            System.out.println("🛒 Cart Product Name: " + text);

            return text;
        
        }
        public String getCartPrice() {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // shop-app
            WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            // shop-cart
            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // shop-cart-item
            WebElement cartItem = wait.until(d ->
                cartShadow.findElement(By.cssSelector("shop-cart-item"))
            );

            SearchContext itemShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", cartItem);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // ✅ CORRECT locator from your console
            WebElement price = itemShadow.findElement(By.cssSelector("div.price"));

            String text = price.getText();

            System.out.println("💲 Cart Price: " + text);

            return text;
        
        }
        
        public double getCartPriceValue() {

            String priceText = getCartPrice();   // "$50.20"

            String value = priceText.replaceAll("[^0-9.]", "");

            return Double.parseDouble(value);
        }
        public int getCartQuantity() {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // shop-app
            WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            // shop-cart
            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // shop-cart-item
            WebElement cartItem = wait.until(d ->
                cartShadow.findElement(By.cssSelector("shop-cart-item"))
            );

            SearchContext itemShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", cartItem);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // ✅ CORRECT locator from your console
            WebElement qtyDropdown = itemShadow
                .findElement(By.cssSelector("div.quantity"))
                .findElement(By.cssSelector("select"));

            String value = qtyDropdown.getAttribute("value");

            System.out.println("🔢 Cart Quantity: " + value);

            return Integer.parseInt(value);
        }
        
        public int getCartQuantityValue() {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // shop-app
            WebElement shopApp = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
            );

            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            // shop-cart
            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // shop-cart-item
            WebElement cartItem = wait.until(d ->
                cartShadow.findElement(By.cssSelector("shop-cart-item"))
            );

            SearchContext itemShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", cartItem);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // ✅ quantity dropdown
            WebElement qtyDropdown = itemShadow.findElement(
                By.cssSelector("div.quantity select")
            );

            String value = qtyDropdown.getAttribute("value");  // e.g. "1"

            System.out.println("🔢 Cart Quantity: " + value);

            return Integer.parseInt(value);
        }
        public double getCartTotal() {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // shop-app
            WebElement shopApp = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
            );

            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            // shop-cart
            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // ✅ total element (FINAL CORRECT LOCATOR)
            WebElement totalElement = wait.until(d ->
                cartShadow.findElement(By.cssSelector("div.checkout-box span"))
            );

            String text = totalElement.getText();   // e.g. "$50.20"

            System.out.println("🧾 Cart Total Text: " + text);

            // ✅ extract numeric value
            String value = text.replaceAll("[^0-9.]", "");

            return Double.parseDouble(value);
        }
        
        public void updateCartQuantity(int qty) {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // shop-app
            WebElement shopApp = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
            );

            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            // shop-cart
            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // shop-cart-item
            WebElement cartItem = wait.until(d ->
                cartShadow.findElement(By.cssSelector("shop-cart-item"))
            );

            SearchContext itemShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", cartItem);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // dropdown
            WebElement dropdown = itemShadow.findElement(
                By.cssSelector("div.quantity select")
            );

            // ✅ change value
            Select select = new Select(dropdown);
            select.selectByValue(String.valueOf(qty));

            System.out.println("🔄 Quantity updated to: " + qty);
        }
        
        public List<String> getAllCartProductNames() {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement shopApp = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
            );

            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopCart
            );

            List<WebElement> items = cartShadow.findElements(By.cssSelector("shop-cart-item"));

            List<String> names = new ArrayList<>();

            for (WebElement item : items) {

                SearchContext itemShadow = (SearchContext) js.executeScript(
                    "return arguments[0].shadowRoot", item
                );

                String name = itemShadow
                    .findElement(By.cssSelector("div.name a"))
                    .getText();

                names.add(name);
            }

            return names;
        }
        
        public CheckoutPage clickCheckout() {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // shop-app
            WebElement shopApp = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
            );

            SearchContext appShadow = (SearchContext) js.executeScript(
                "return arguments[0].shadowRoot", shopApp
            );

            // shop-cart
            WebElement shopCart = appShadow.findElement(By.cssSelector("shop-cart"));

            SearchContext cartShadow = wait.until(d -> {
                Object shadow = js.executeScript("return arguments[0].shadowRoot", shopCart);
                return shadow != null ? (SearchContext) shadow : null;
            });

            // ✅ checkout button (anchor inside shop-button)
            WebElement checkoutBtn = cartShadow
                .findElement(By.cssSelector("shop-button a"));

            // ✅ JS click (safer for shadow DOM)
            js.executeScript("arguments[0].click();", checkoutBtn);

            System.out.println("🛒 Clicked Checkout");

            // ✅ wait for checkout page to load
            wait.until(d -> {
                try {
                    WebElement newApp = driver.findElement(By.cssSelector("shop-app"));

                    SearchContext newShadow = (SearchContext) js.executeScript(
                        "return arguments[0].shadowRoot", newApp
                    );

                    WebElement checkout = newShadow.findElement(By.cssSelector("shop-checkout"));

                    return checkout.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });

            return new CheckoutPage(driver);
        }
        }
