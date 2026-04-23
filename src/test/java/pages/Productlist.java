package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.ShadowUtil;

public class Productlist {

	 WebDriver driver;
	    ShadowUtil shadowUtil;
	    WebDriverWait wait;

	    public Productlist(WebDriver driver) {
	        this.driver = driver;
	        this.shadowUtil = new ShadowUtil(driver);
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }

	    // ✅ ADD IT HERE (below constructor)
	    public void waitForPageToLoad() {
	        wait.until(driver -> driver.getCurrentUrl().contains("/list/"));
	        System.out.println("✅ Navigated to: " + driver.getCurrentUrl());
	    }
	    
	    public void waitForProductsToLoad() {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

	        WebElement shopApp = wait.until(
	            ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
	        );

	        // ✅ Wait for shop-app shadowRoot
	        SearchContext shopAppShadow = wait.until(driver -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopApp);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        WebElement shopList = shopAppShadow.findElement(By.cssSelector("shop-list"));

	        // ✅ WAIT FOR shop-list shadowRoot (THIS IS YOUR ISSUE)
	        SearchContext shopListShadow = wait.until(driver -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopList);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        // ✅ NOW wait for products
	        wait.until(driver ->
	            shopListShadow.findElements(By.cssSelector("shop-list-item")).size() > 0
	        );

	        System.out.println("✅ Products loaded successfully");
	    }
  /*  // ✅ Page Heading
    public String getListPageHead() {
        return shadowUtil.getText(
            "shop-app",
            "shop-list",
            "header h1"
            
        );
    }*/
	    public String getListPageHead()
	    {
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
	        SearchContext shopAppShadow = (SearchContext) js.executeScript("return arguments[0].shadowRoot", shopApp);

	        WebElement shopList = shopAppShadow.findElement(By.cssSelector("shop-list"));
	        SearchContext shopListShadow = (SearchContext) js.executeScript("return arguments[0].shadowRoot", shopList);

	        WebElement h1 = shopListShadow.findElement(By.cssSelector("header h1"));

	        return h1.getText();
	    }
    // ✅ First Product Name
	    public String getFirstProductName() {

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
	        SearchContext appShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopApp
	        );

	        WebElement shopList = appShadow.findElement(By.cssSelector("shop-list"));

	        SearchContext listShadow = wait.until(d -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopList);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        WebElement link = listShadow
	            .findElement(By.cssSelector("ul"))
	            .findElement(By.cssSelector("a"));

	        String fullText = link.getText();

	        // ✅ FIX: take only first line (product name)
	        String name = fullText.split("\n")[0].trim();

	        System.out.println("🧾 Expected Product Name: " + name);

	        return name;
	    }
    // ✅ First Product Image
	    public boolean isFirstProductImageDisplayed() {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

	        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
	        SearchContext shopAppShadow = (SearchContext) js.executeScript("return arguments[0].shadowRoot", shopApp);

	        WebElement shopList = shopAppShadow.findElement(By.cssSelector("shop-list"));
	        SearchContext shopListShadow = (SearchContext) js.executeScript("return arguments[0].shadowRoot", shopList);

	        WebElement listItem = shopListShadow
	            .findElement(By.cssSelector("ul"))
	            .findElement(By.cssSelector("shop-list-item"));

	        SearchContext itemShadow = wait.until(d -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", listItem);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        WebElement shopImage = itemShadow.findElement(By.cssSelector("shop-image"));

	        SearchContext imageShadow = wait.until(d -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopImage);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        WebElement img = imageShadow.findElement(By.cssSelector("img"));

	        // ✅ WAIT for visibility
	        wait.until(ExpectedConditions.visibilityOf(img));

	        return true;
	    }
    
	    
	    public ProductDesc clickFirstProduct() {

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

	        // shop-app
	        WebElement shopApp = wait.until(
	            ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
	        );

	        SearchContext appShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopApp
	        );

	        // shop-list
	        WebElement shopList = appShadow.findElement(By.cssSelector("shop-list"));

	        SearchContext listShadow = wait.until(d -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", shopList);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        // click first product
	        WebElement link = listShadow
	            .findElement(By.cssSelector("ul"))
	            .findElement(By.cssSelector("a"));

	        js.executeScript("arguments[0].click();", link);

	        // ✅ NEW: wait for detail page element instead of URL
	        wait.until(d -> {
	            try {
	                WebElement newApp = driver.findElement(By.cssSelector("shop-app"));


	                SearchContext newShadow = (SearchContext) js.executeScript(
	                    "return arguments[0].shadowRoot", newApp
	                );

	                WebElement detail = newShadow.findElement(By.cssSelector("shop-detail"));

	                return detail.isDisplayed();
	            } catch (Exception e) {
	                return false;
	            }
	        });

	        return new ProductDesc(driver);
	    }
	    
	   /* public void clickProductByIndex(int index) {

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
	        SearchContext appShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopApp
	        );

	        WebElement shopList = appShadow.findElement(By.cssSelector("shop-list"));

	        SearchContext listShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopList
	        );

	        List<WebElement> products = listShadow
	            .findElement(By.cssSelector("ul"))
	            .findElements(By.cssSelector("a"));

	        WebElement product = products.get(index);

	        js.executeScript("arguments[0].click();", product);}*/
	    
	    
	    public ProductDesc clickProductByIndex(int index) {

	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // shop-app
	        WebElement shopApp = wait.until(
	            ExpectedConditions.presenceOfElementLocated(
	                By.cssSelector("shop-app"))
	        );

	        SearchContext appShadow = wait.until(d -> {
	            Object shadow = js.executeScript(
	                "return arguments[0].shadowRoot", shopApp);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        // shop-list
	        WebElement shopList = appShadow.findElement(
	            By.cssSelector("shop-list"));

	        SearchContext listShadow = wait.until(d -> {
	            Object shadow = js.executeScript(
	                "return arguments[0].shadowRoot", shopList);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        // products
	        List<WebElement> products =
	            listShadow.findElements(By.cssSelector("ul li"));

	        WebElement product = products.get(index);

	        WebElement link = product.findElement(By.cssSelector("a"));

	        js.executeScript("arguments[0].click();", link);

	        wait.until(ExpectedConditions.urlContains("/detail/"));

	        return new ProductDesc(driver);
	    }
	    public String getProductNameByIndex(int index) {

	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        WebElement shopApp = driver.findElement(By.cssSelector("shop-app"));
	        SearchContext appShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopApp
	        );

	        WebElement shopList = appShadow.findElement(By.cssSelector("shop-list"));
	        SearchContext listShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopList
	        );

	        List<WebElement> products = listShadow
	            .findElement(By.cssSelector("ul"))
	            .findElements(By.cssSelector("a"));

	        String fullText = products.get(index).getText();

	        return fullText.split("\n")[0].trim();  // remove price
	    }
}

/*
public void verifyMenuButton() {

  // Step 1: Get shop-app shadow root
    SearchContext shadow1 = shadowUtil.getShadowRoot(By.cssSelector("shop-app"));
    SearchContext shadow2 = shadowUtil.getShadowRoot(shadow1,By.cssSelector("app-header"));
    

    // Step 2: Locate menu button (light DOM inside shadow)
    WebElement menuBtntool = shadow2.findElement(By.cssSelector("div.left-bar-item"));
    WebElement menuButton = menuBtntool.findElement(By.cssSelector("paper-icon-button"));
    menuButton.click();

    // Step 3: Handle drawer (may or may not have shadow root)
    SearchContext drawerContext;

    try {
        drawerContext = shadowUtil.getShadowRoot(shadow1, By.cssSelector("app-drawer"));
    } catch (Exception e) {
        drawerContext = shadow1;
    }

    // Step 4: Get menu items
    java.util.List<WebElement> menuItems =
            drawerContext.findElements(By.cssSelector("a"));

    // Step 5: Expected values
    java.util.List<String> expectedItems = java.util.Arrays.asList(
            "Men's Outerwear",
            "Ladies Outerwear",
            "Men's T-Shirts",
            "Ladies T-Shirts"
    );

    // Step 6: Validation
    for (int i = 0; i < expectedItems.size(); i++) {

        String actual = menuItems.get(i).getText().trim();

        if (!actual.equals(expectedItems.get(i))) {
            throw new AssertionError(
                    "Mismatch at index " + i +
                    " Expected: " + expectedItems.get(i) +
                    " but found: " + actual
            );
        }
    }

    System.out.println("✅ Menu validation successful");
} */

