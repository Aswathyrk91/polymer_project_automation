package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage {
	 WebDriver driver;
	    JavascriptExecutor js;
	    WebDriverWait wait;

	    public CheckoutPage(WebDriver driver) {
	        this.driver = driver;
	        this.js = (JavascriptExecutor) driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    }
	    
	    private SearchContext getCheckoutShadow() {

	        WebElement shopApp = wait.until(
	            ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
	        );

	        SearchContext appShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopApp
	        );

	        WebElement checkout = appShadow.findElement(By.cssSelector("shop-checkout"));

	        return wait.until(d -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", checkout);
	            return shadow != null ? (SearchContext) shadow : null;
	        });
	    }
	    public String getCheckoutPageTitle() {

	        return getCheckoutShadow()
	            .findElement(By.cssSelector("h1"))
	            .getText()
	            .trim();
	    }
	    
	    public HomePage clickShopLogo() {

	        WebElement shopApp =
	            driver.findElement(By.cssSelector("shop-app"));

	        SearchContext shadow =
	            shopApp.getShadowRoot();

	        WebElement logo =
	            shadow.findElement(
	                By.cssSelector("a[aria-label='SHOP Home']")
	            );

	        logo.click();

	        return new HomePage(driver);
	    }
	    
	    public boolean isPaymentSectionDisplayed() {

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        WebElement shopApp = wait.until(
	            ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
	        );

	        SearchContext appShadow = (SearchContext) js.executeScript(
	            "return arguments[0].shadowRoot", shopApp
	        );

	        WebElement checkout = appShadow.findElement(By.cssSelector("shop-checkout"));

	        SearchContext checkoutShadow = wait.until(d -> {
	            Object shadow = js.executeScript("return arguments[0].shadowRoot", checkout);
	            return shadow != null ? (SearchContext) shadow : null;
	        });

	        // ✅ USE IDs (correct from DOM)
	        return checkoutShadow.findElement(By.cssSelector("#ccName")).isDisplayed()
	            && checkoutShadow.findElement(By.cssSelector("#ccNumber")).isDisplayed()
	            && checkoutShadow.findElement(By.cssSelector("#ccCVV")).isDisplayed();
	    }
	    
	  
	public WebElement getCardholderNameField() {

	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    WebElement shopApp = wait.until(
	        ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
	    );

	    SearchContext appShadow = (SearchContext) js.executeScript(
	        "return arguments[0].shadowRoot", shopApp
	    );

	    WebElement checkout = appShadow.findElement(By.cssSelector("shop-checkout"));

	    SearchContext checkoutShadow = (SearchContext) js.executeScript(
	        "return arguments[0].shadowRoot", checkout
	    );

	    // ✅ DIRECT ACCESS using id
	    return checkoutShadow.findElement(By.cssSelector("input#ccName"));
	}
	
	public WebElement getCardNumberField() {

	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    WebElement shopApp = wait.until(
	        ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
	    );

	    SearchContext appShadow = (SearchContext) js.executeScript(
	        "return arguments[0].shadowRoot", shopApp
	    );

	    WebElement checkout = appShadow.findElement(By.cssSelector("shop-checkout"));

	    SearchContext checkoutShadow = (SearchContext) js.executeScript(
	        "return arguments[0].shadowRoot", checkout
	    );

	    // ✅ FROM YOUR DOM
	    return checkoutShadow.findElement(By.cssSelector("input#ccNumber"));
	}
	
	public WebElement getCVVField() {

	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    WebElement shopApp = wait.until(
	        ExpectedConditions.presenceOfElementLocated(By.cssSelector("shop-app"))
	    );

	    SearchContext appShadow = (SearchContext) js.executeScript(
	        "return arguments[0].shadowRoot", shopApp
	    );

	    WebElement checkout = appShadow.findElement(By.cssSelector("shop-checkout"));

	    SearchContext checkoutShadow = (SearchContext) js.executeScript(
	        "return arguments[0].shadowRoot", checkout
	    );

	    // ✅ CVV input
	    return checkoutShadow.findElement(By.cssSelector("input#ccCVV"));
	}
	
	public void enterCardholderName(String name) {
	    WebElement input = getCardholderNameField();
	    input.clear();
	    input.sendKeys(name);
	}

	public void enterCardNumber(String number) {
	    WebElement input = getCardNumberField();
	    input.clear();
	    input.sendKeys(number);
	}
	public void enterCVV(String cvv) {

	    WebElement input = getCVVField();

	    input.clear();
	    input.sendKeys(cvv);

	    System.out.println("🔒 Entered CVV: " + cvv);
	}
	public String getSelectedExpiryMonth() {

	    return getCheckoutShadow()
	        .findElement(By.cssSelector("#ccExpMonth"))
	        .getAttribute("value")
	        .trim();
	}
	
	public String getSelectedExpiryYear() {

	    return getCheckoutShadow()
	        .findElement(By.cssSelector("#ccExpYear"))
	        .getAttribute("value")
	        .trim();
	}
	public String getCardNumber() {
	    return getCardNumberField().getAttribute("value");
	}
	
	public String getCardholderName() {
	    return getCardholderNameField().getAttribute("value");
	}
	public String getCVV() {
	    return getCVVField().getAttribute("value");
	}
	
	public boolean isCardNumberInvalid() {

	    WebElement field = getCardNumberField();

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    return (Boolean) js.executeScript(
	        "return !arguments[0].checkValidity();",
	        field
	    );
	}
	
	public double getCheckoutTotal() {

	    SearchContext shadow = getCheckoutShadow();

	    WebElement total = shadow.findElement(
	        By.cssSelector("div.row.total-row > div:nth-of-type(2)")
	    );

	    String text = total.getText()
	                       .replace("$", "")
	                       .trim();

	    return Double.parseDouble(text);
	}
	
	public WebElement getEmailField() {

	    return getCheckoutShadow()
	        .findElement(By.cssSelector("input#accountEmail"));
	}

	public void enterEmail(String email) {

	    WebElement field = getEmailField();

	    field.clear();
	    field.sendKeys(email);
	}

	public String getEmail() {

	    return getEmailField().getAttribute("value");
	}
	
	public WebElement getPhoneField() {

	    return getCheckoutShadow()
	        .findElement(By.cssSelector("input#accountPhone"));
	}

	public void enterPhone(String phone) {

	    WebElement field = getPhoneField();

	    field.clear();
	    field.sendKeys(phone);
	}
	


	public String getPhone() {

	    return getPhoneField().getAttribute("value");
	}
	
	public boolean isEmailInvalid() {
	    return isFieldInvalid(getEmailField());
	}
	
	public boolean isPhoneInvalid() {
	    return isFieldInvalid(getPhoneField());
	}
	
	public WebElement getAddressField() {
	    return getCheckoutShadow()
	        .findElement(By.cssSelector("input#shipAddress"));
	}

	public void enterAddress(String address) {
	    WebElement field = getAddressField();
	    field.clear();
	    field.sendKeys(address);
	}

	public String getAddress() {
	    return getAddressField().getAttribute("value");
	}
	public boolean isAddressInvalid() {
	    return isFieldInvalid(getAddressField());
	}
	public WebElement getCityField() {
	    return getCheckoutShadow()
	        .findElement(By.cssSelector("input#shipCity"));
	}

	public void enterCity(String city) {
	    WebElement field = getCityField();
	    field.clear();
	    field.sendKeys(city);
	}

	public String getCity() {
	    return getCityField().getAttribute("value");
	}
	public boolean isCityInvalid() {
	    return isFieldInvalid(getCityField());
	}
	public WebElement getStateField() {
	    return getCheckoutShadow()
	        .findElement(By.cssSelector("input#shipState"));
	}

	public void enterState(String state) {
	    WebElement field = getStateField();
	    field.clear();
	    field.sendKeys(state);
	}

	public String getState() {
	    return getStateField().getAttribute("value");
	}
	
	public WebElement getZipField() {
	    return getCheckoutShadow()
	        .findElement(By.cssSelector("input#shipZip"));
	}

	public void enterZip(String zip) {
	    WebElement field = getZipField();
	    field.clear();
	    field.sendKeys(zip);
	}

	public String getZip() {
	    return getZipField().getAttribute("value");
	}
	
	public Select getCountryDropdown() {

	    WebElement dropdown = getCheckoutShadow()
	        .findElement(By.cssSelector("select#shipCountry"));

	    return new Select(dropdown);
	}

	public void selectCountry(String country) {
	    getCountryDropdown().selectByVisibleText(country);
	}

	public String getSelectedCountry() {
	    return getCountryDropdown()
	        .getFirstSelectedOption()
	        .getText();
	}
	
	private boolean isFieldInvalid(WebElement field) {

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    return !(Boolean) js.executeScript(
	        "return arguments[0].checkValidity();",
	        field
	    );
	}
	
	public WebElement getBillingCheckbox() {

	    return getCheckoutShadow()
	        .findElement(By.cssSelector("input#setBilling"));
	}
	public void selectDifferentBillingAddress() {

	    WebElement checkbox = getBillingCheckbox();

	    if (!checkbox.isSelected()) {
	        checkbox.click();
	    }
	}
	public boolean isBillingAddressSectionVisible() {

	    WebDriverWait wait =
	        new WebDriverWait(driver, Duration.ofSeconds(5));

	    List<WebElement> fields = wait.until(d -> {

	        List<WebElement> elems =
	            getCheckoutShadow().findElements(
	              By.cssSelector(
	                "div.billing-address-picker + div input"
	              )
	            );

	        return elems.size() > 0 ? elems : null;
	    });

	    return fields.size() > 0;
	}
	public boolean isBillingCheckboxSelected() {

	    return getBillingCheckbox().isSelected();
	}
	
	public void unselectDifferentBillingAddress() {

	    WebElement checkbox = getBillingCheckbox();

	    if (checkbox.isSelected()) {
	        checkbox.click();
	    }
	}
	
	public void clickPlaceOrder() {

	    WebElement btn =
	        getCheckoutShadow()
	        .findElement(
	            By.cssSelector(
	                "#submitBox input[value='Place Order']"
	            )
	        );

	    ((JavascriptExecutor) driver)
	        .executeScript(
	            "arguments[0].click();",
	            btn
	        );

	    WebDriverWait wait =
	        new WebDriverWait(driver,
	            Duration.ofSeconds(10));

	    wait.until(
	        ExpectedConditions.urlContains(
	            "/checkout/success"
	        )
	    );
	}
	public boolean isOrderSuccessDisplayed() {

	    return driver.getCurrentUrl()
	        .contains("/checkout/success");
	}
	
	public void enterShippingAddress(
	        String address,
	        String city,
	        String state,
	        String zip,
	        String country) {

	    enterAddress(address);
	    enterCity(city);
	    enterState(state);
	    enterZip(zip);
	    selectCountry(country);
	}
	
	public void fillMandatoryFields() {

	    enterCardholderName("John Doe");
	    enterCardNumber("4111111111111111");
	    enterCVV("123");

	    enterEmail("test@gmail.com");
	    enterPhone("9876543210");

	    enterShippingAddress(
	        "Street 1",
	        "Dubai",
	        "Dubai",
	        "12345",
	        "Canada"
	    );
	}
}
