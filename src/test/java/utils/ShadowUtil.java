package utils;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ShadowUtil {

    private WebDriver driver;
    private WebDriverWait wait;

    public ShadowUtil(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // 🔥 CORE METHOD → Handles Shadow DOM traversal safely
    public WebElement getShadowElement(String... selectors) {

        SearchContext context = driver;
        WebElement element = null;

        for (int i = 0; i < selectors.length; i++) {

            String selector = selectors[i];
            System.out.println("➡️ Finding: " + selector); // ✅ Debug log

            // ✅ FIX: Use final variable inside lambda
            final SearchContext ctx = context;

            try {
                element = wait.until(d -> ctx.findElement(By.cssSelector(selector)));
            } catch (Exception e) {
                throw new NoSuchElementException("❌ Failed at selector: " + selector);
            }

            // 🔥 Move into next context (shadow or normal DOM)
            if (i < selectors.length - 1) {
                try {
                    SearchContext shadowRoot = element.getShadowRoot();

                    if (shadowRoot != null) {
                        context = shadowRoot;
                    } else {
                        context = element;
                    }

                } catch (Exception e) {
                    context = element; // fallback for non-shadow elements
                }
            }
        }

        return element;
    }

    // 🔥 Get visible element
    public WebElement getVisibleShadowElement(String... selectors) {
        WebElement element = getShadowElement(selectors);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // 🔥 Get clickable element
    public WebElement getClickableShadowElement(String... selectors) {
        WebElement element = getShadowElement(selectors);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // 🔥 Click (JS click is safest for Shadow DOM)
    public void click(String... selectors) {

        WebElement element = getShadowElement(selectors);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            // 🔥 Fallback to JS click
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
        }
    }

    // 🔥 Get text safely
    public String getText(String... selectors) {
        return getVisibleShadowElement(selectors).getText();
    }

    // 🔥 Check visibility
    public boolean isDisplayed(String... selectors) {
        try {
            return getVisibleShadowElement(selectors).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}