package tests;

import java.util.List;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.DriverSetup;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.HomePage1;
import pages.ProductDesc;

import pages.Productlist;

import utils.CartHelper;

public class HomePageTest extends DriverSetup {
    
    @Test
    public void verifyHomeTitle() {
        String title = driver.getTitle();
        Assert.assertEquals(title.trim(), "Home - SHOP");
    }
    
    @Test
    public void verifyCartIsEmpty() {

        HomePage1 home = new HomePage1(driver);

        CartPage cartPage = home.clickCart();

        Assert.assertTrue(cartPage.isCartEmpty(), "❌ Cart is not empty");
    }
    
    @Test
    public void verifyNavigation() {

        HomePage1 home = new HomePage1(driver);
        
        home.clickMenMenu();
        Assert.assertTrue(driver.getCurrentUrl().contains("mens_outerwear"));

        home.clickLadiesMenu();
        Assert.assertTrue(driver.getCurrentUrl().contains("ladies_outerwear"));

        home.clickMenTshirt();
        Assert.assertTrue(driver.getCurrentUrl().contains("mens_tshirts"));

        home.clickLadiesTshirt();
        Assert.assertTrue(driver.getCurrentUrl().contains("ladies_tshirts"));

        home.clickMenOuterwearBanner();
        Assert.assertTrue(driver.getCurrentUrl().contains("mens_outerwear"));

        home.clickLadiesOuterwearBanner();
        Assert.assertTrue(driver.getCurrentUrl().contains("ladies_outerwear"));

        home.clickMenTshirtBanner();
        Assert.assertTrue(driver.getCurrentUrl().contains("mens_tshirts"));

        home.clickLadiesTshirtBanner();
        Assert.assertTrue(driver.getCurrentUrl().contains("ladies_tshirts"));
    }

    @Test
    public void verifyProductListingUI() {

        HomePage1 home = new HomePage1(driver);

        Productlist productlist = home.clickShopNow();
        
        productlist.waitForPageToLoad();     
        productlist.waitForProductsToLoad();

      Assert.assertEquals(productlist.getListPageHead(), "Men's Outerwear");

      Assert.assertTrue(productlist.isFirstProductImageDisplayed());

      Assert.assertTrue(productlist.getFirstProductName().contains("Tech Shell"));
      productlist.clickFirstProduct();
  
    }
    @Test
    public void verifyProductDetailPage() {

        HomePage1 home = new HomePage1(driver);
        Productlist productlist = home.clickShopNow();

        productlist.waitForProductsToLoad();

        // ✅ Get expected product name BEFORE click
        String expectedProductName = productlist.getProductNameByIndex(0);

        // ✅ Navigate to detail page
        ProductDesc detailPage = productlist.clickFirstProduct();

        // ✅ Basic navigation validation
        Assert.assertTrue(driver.getCurrentUrl().contains("/detail/"));

        // ✅ Title validation (dynamic)
        String actualProductTitle =
        	    detailPage.getProductTitle();

        Assert.assertTrue(
        	    actualProductTitle.contains(expectedProductName));

        // ✅ Product name validation
        Assert.assertFalse(detailPage.getProductTitle().isEmpty());

        // ✅ Price validation
        Assert.assertTrue(detailPage.getProductPrice().contains("$"));

        // ✅ Size validation
        Assert.assertEquals(detailPage.getSizeLabel(), "Size");
        Assert.assertNotNull(detailPage.getSelectedSize());

        // ✅ Quantity validation
        Assert.assertEquals(detailPage.getQuantityLabel(), "Quantity");
        Assert.assertEquals(detailPage.getQuantity(), "1");

        // ✅ Description validation
        Assert.assertTrue(detailPage.getDescriptionLabel().contains("Description"));
        Assert.assertFalse(detailPage.getDescriptionText().isEmpty());

        // ✅ Dropdown functionality
        detailPage.verifyAllSizesSelectable();
    }
    
    
    
    
    
    @Test
    public void verifyAddRemoveCartFlow() {

        HomePage home = new HomePage(driver);

        // Step 1: Go to product list
        Productlist productlist = home.clickShopNow();
        productlist.waitForPageToLoad();
        productlist.waitForProductsToLoad();

        // Step 2: Go to detail page
        ProductDesc detailPage = productlist.clickFirstProduct();

        // Step 3: Add to cart
        detailPage.addToCart();

        // Step 4: Verify cart count
      int count = home.getCartCount();
        System.out.println("🛒 Cart count: " + count);

        Assert.assertTrue(count > 0, "❌ Cart count not updated");

        // Step 5: Go to cart
        CartPage cartPage = home.clickCart();

        // Step 6: Remove item
        cartPage.removeItem();

        // Step 7: Verify empty
        Assert.assertTrue(cartPage.isCartEmpty(), "❌ Cart not empty after removal");
    }
    
    @Test
    public void verifyCartDetails() {

        HomePage home = new HomePage(driver);

        Productlist productlist = home.clickShopNow();

        productlist.waitForProductsToLoad();

        // ✅ ONLY ONCE
        ProductDesc detail = productlist.clickFirstProduct();
        

        String expectedName = productlist.getFirstProductName();
        String expectedPrice = detail.getProductPrice();

        detail.addToCart();

        CartPage cart = home.clickCart();
        
        String actualName = cart.getCartProductName();

        System.out.println("Expected Name: " + expectedName);
        System.out.println("Actual Name: " + actualName);

        // ✅ validations
        Assert.assertTrue(cart.getCartProductName().contains(expectedName));
        Assert.assertEquals(cart.getCartPrice(), expectedPrice);
        Assert.assertEquals(cart.getCartQuantity(), 1);
        
        double price = cart.getCartPriceValue();
        int qty = cart.getCartQuantity();
        double actualTotal = cart.getCartTotal();

        double expectedTotal = price * qty;

        System.out.println("Price: " + price);
        System.out.println("Quantity: " + qty);
        System.out.println("Expected Total: " + expectedTotal);
        System.out.println("Actual Total: " + actualTotal);

        Assert.assertEquals(actualTotal, expectedTotal, 0.01);
    }
    
    @Test
    public void verifyQuantityUpdate() {
    	
    	 HomePage home = new HomePage(driver);

         Productlist productlist = home.clickShopNow();

        ProductDesc detail = productlist.clickFirstProduct();
        detail.addToCart();

        CartPage cart = home.clickCart();

        double price = cart.getCartPriceValue();

        // 🔄 change quantity
        cart.updateCartQuantity(3);

        // wait a bit for UI update (important)
        try { Thread.sleep(2000); } catch (Exception e) {}

        int updatedQty = cart.getCartQuantityValue();
        double actualTotal = cart.getCartTotal();

        double expectedTotal = price * updatedQty;

        System.out.println("Updated Qty: " + updatedQty);
        System.out.println("Expected Total: " + expectedTotal);
        System.out.println("Actual Total: " + actualTotal);

        // ✅ validations
        Assert.assertEquals(updatedQty, 3);
        Assert.assertEquals(actualTotal, expectedTotal, 0.01);
    }
    
    
    @Test
    public void verifyMultipleProductsInCart() {

        HomePage home = new HomePage(driver);

        // Open product listing page
        Productlist productlist = home.clickShopNow();
        productlist.waitForProductsToLoad();

        // ===============================
        // FIRST PRODUCT
        // ===============================
        String name1 = productlist.getProductNameByIndex(0);

        productlist.clickProductByIndex(0);

        ProductDesc detail1 = new ProductDesc(driver);

        double price1 = detail1.getProductPriceValue();

        detail1.addToCart();

        // Return to product list using app navigation
        productlist = home.clickShopNow();
        productlist.waitForProductsToLoad();

        // ===============================
        // SECOND PRODUCT
        // ===============================
        String name2 = productlist.getProductNameByIndex(1);

        productlist.clickProductByIndex(1);

        ProductDesc detail2 = new ProductDesc(driver);

        double price2 = detail2.getProductPriceValue();

        detail2.addToCart();

        // ===============================
        // OPEN CART
        // ===============================
        CartPage cart = home.clickCart();

        // ===============================
        // VALIDATE TOTAL
        // ===============================
        double expectedTotal =
                CartHelper.calculateExpectedTotal(price1, price2);

        double actualTotal = cart.getCartTotal();

        System.out.println("Expected Total = " + expectedTotal);
        System.out.println("Actual Total   = " + actualTotal);

        Assert.assertEquals(actualTotal, expectedTotal, 0.01);

        // ===============================
        // VALIDATE PRODUCT NAMES
        // ===============================
        List<String> cartNames = cart.getAllCartProductNames();

        Assert.assertTrue(cartNames.contains(name1));
        Assert.assertTrue(cartNames.contains(name2));

        // ===============================
        // VALIDATE ITEM COUNT
        // ===============================
        Assert.assertEquals(cartNames.size(), 2);
    } 
    @Test
    public void verifyCheckoutPage() {

        HomePage home = new HomePage(driver);

        // ✅ Add at least one product (IMPORTANT)
        Productlist productlist = home.clickShopNow();
        productlist.waitForProductsToLoad();

        productlist.clickProductByIndex(0);

        ProductDesc detail = new ProductDesc(driver);
        detail.addToCart();

        // ✅ Go to cart → checkout
        CartPage cart = home.clickCart();

        CheckoutPage checkout = cart.clickCheckout();

        // ✅ Payment section visible
        Assert.assertTrue(checkout.isPaymentSectionDisplayed());

        // ✅ Enter values
        checkout.enterCardholderName("John Doe");
        checkout.enterCardNumber("4111111111111111");
        checkout.enterCVV("123");

        // ✅ Correct validations (use getters, NOT elements)
        Assert.assertEquals(checkout.getCardholderName(), "John Doe");
        Assert.assertEquals(checkout.getCardNumber(), "4111111111111111");
        Assert.assertEquals(checkout.getCVV(), "123");
    }
    
    @Test
    public void verifyCardNumberShowsErrorForAlphabets() {

        HomePage home = new HomePage(driver);

        Productlist list = home.clickShopNow();
        list.waitForProductsToLoad();
        list.clickProductByIndex(0);

        ProductDesc detail = new ProductDesc(driver);
        detail.addToCart();

        CartPage cart = home.clickCart();
        CheckoutPage checkout = cart.clickCheckout();

        // Enter invalid card number
        checkout.enterCardNumber("ABCD1234");

        // Move focus to another field (blur trigger)
        checkout.enterCVV("123");

        // Validate card number
     Assert.assertTrue(checkout.isCardNumberInvalid());
    
    }
    
    @Test
    public void verifyCheckoutTotalMatchesCartTotal() {

        HomePage home = new HomePage(driver);

        Productlist list = home.clickShopNow();
        list.waitForProductsToLoad();

        list.clickProductByIndex(0);

        ProductDesc detail = new ProductDesc(driver);
        detail.addToCart();

        CartPage cart = home.clickCart();

        double cartTotal = cart.getCartTotal();

        CheckoutPage checkout = cart.clickCheckout();

        double checkoutTotal = checkout.getCheckoutTotal();

        Assert.assertEquals(checkoutTotal, cartTotal, 0.01);
    }
    
    @Test
    public void verifyEmailAndPhoneFields() {
    	
    	 HomePage home = new HomePage(driver);

         Productlist list = home.clickShopNow();
         list.waitForProductsToLoad();

         list.clickProductByIndex(0);

         ProductDesc detail = new ProductDesc(driver);
         detail.addToCart();

         CartPage cart = home.clickCart();

         CheckoutPage checkout = cart.clickCheckout();


        checkout.enterEmail("john@test.com");
        checkout.enterPhone("9876543210");

        Assert.assertEquals(
            checkout.getEmail(),
            "john@test.com"
        );

        Assert.assertEquals(
            checkout.getPhone(),
            "9876543210"
        );
    }
    
    @Test
    public void verifyInvalidEmailShowsError() {
    	 HomePage home = new HomePage(driver);

         Productlist list = home.clickShopNow();
         list.waitForProductsToLoad();

         list.clickProductByIndex(0);

         ProductDesc detail = new ProductDesc(driver);
         detail.addToCart();

         CartPage cart = home.clickCart();


         CheckoutPage checkout = cart.clickCheckout();
        checkout.enterEmail("john.com");

        checkout.enterPhone("9876543210"); // blur trigger

        Assert.assertTrue(checkout.isEmailInvalid());
    }
    
    @Test
    public void verifyShippingAddressFields() {
    	
    	 HomePage home = new HomePage(driver);

         Productlist list = home.clickShopNow();
         list.waitForProductsToLoad();

         list.clickProductByIndex(0);

         ProductDesc detail = new ProductDesc(driver);
         detail.addToCart();

         CartPage cart = home.clickCart();

        CheckoutPage checkout = cart.clickCheckout();

        checkout.enterAddress("123 Main Street");
        checkout.enterCity("Dallas");
        checkout.enterState("Texas");
        checkout.enterZip("75001");
        checkout.selectCountry("United States");

        Assert.assertEquals(
            checkout.getAddress(),
            "123 Main Street"
        );

        Assert.assertEquals(
            checkout.getCity(),
            "Dallas"
        );

        Assert.assertEquals(
            checkout.getState(),
            "Texas"
        );

        Assert.assertEquals(
            checkout.getZip(),
            "75001"
        );

        Assert.assertEquals(
            checkout.getSelectedCountry(),
            "United States"
        );
        
        checkout.enterAddress("12");

        Assert.assertTrue(checkout.isEmailInvalid());
        
        checkout.enterCity("A");

        Assert.assertTrue(checkout.isCityInvalid());
    }
    
   
    	
    @Test
    public void verifyBillingCheckbox() {
    	 HomePage home = new HomePage(driver);

         Productlist list = home.clickShopNow();
         list.waitForProductsToLoad();

         list.clickProductByIndex(0);

         ProductDesc detail = new ProductDesc(driver);
         detail.addToCart();

         CartPage cart = home.clickCart();

        CheckoutPage checkout = cart.clickCheckout();

        // default state
        Assert.assertFalse(
            checkout.isBillingCheckboxSelected()
        );

        // select checkbox
        checkout.selectDifferentBillingAddress();

        Assert.assertTrue(
            checkout.isBillingCheckboxSelected()
        );

        // unselect
        checkout.unselectDifferentBillingAddress();

        Assert.assertFalse(
            checkout.isBillingCheckboxSelected()
        );
    }
    
    @Test
    public void verifyAddToCartPopupFlow() {

        HomePage home = new HomePage(driver);

        Productlist list = home.clickShopNow();
        list.waitForProductsToLoad();

        list.clickProductByIndex(0);

        ProductDesc detail = new ProductDesc(driver);

        // =========================
        // Popup appears
        // =========================
        detail.addToCart();

        Assert.assertTrue(
            detail.isCartPopupDisplayed()
        );

        Assert.assertEquals(
            detail.getCartPopupMessage(),
            "Added to cart"
        );

        // =========================
        // Close popup
        // =========================
        detail.closeCartPopup();

        Assert.assertFalse(
            detail.isCartPopupDisplayed()
        );

        // =========================
        // Reopen popup -> View Cart
        // =========================
        detail.addToCart();

        detail.clickViewCartFromPopup();

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/cart")
        );

        // =========================
        // Go back and test Checkout
        // =========================
        driver.navigate().back();

        detail.addToCart();

        detail.clickCheckoutFromPopup();

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/checkout")
        );
    }
    
    @Test
    public void verifyDefaultExpiryDateIsValid() {
    	
    	HomePage home = new HomePage(driver);

        Productlist list = home.clickShopNow();
        list.waitForProductsToLoad();

        list.clickProductByIndex(0);

        ProductDesc detail = new ProductDesc(driver);
        detail.addToCart();

        CartPage cart = home.clickCart();

       CheckoutPage checkout = cart.clickCheckout();

        int selectedYear =
            Integer.parseInt(checkout.getSelectedExpiryYear());

        int currentYear =
            java.time.Year.now().getValue();

        if (selectedYear < currentYear) {
            Assert.fail(
              "BUG: Default expiry year is " +
              selectedYear +
              ", should be current/future year"
            );
        }
    }
    @Test
    public void verifyExpiredDefaultDateRejected() {

    	HomePage home = new HomePage(driver);

        Productlist list = home.clickShopNow();
        list.waitForProductsToLoad();

        list.clickProductByIndex(0);

        ProductDesc detail = new ProductDesc(driver);
        detail.addToCart();

        CartPage cart = home.clickCart();
        CheckoutPage checkout = cart.clickCheckout();
        checkout.fillMandatoryFields();

        checkout.clickPlaceOrder();

        Assert.assertFalse(
            checkout.isOrderSuccessDisplayed(),
            "BUG: Order accepted with expired default date"
        );
    }
    @Test
    public void verifySuccessPageMessage() {

        HomePage home = new HomePage(driver);

        Productlist list = home.clickShopNow();
        list.waitForProductsToLoad();

        list.clickProductByIndex(0);

        ProductDesc detail = new ProductDesc(driver);
        detail.addToCart();

        CartPage cart = home.clickCart();

        CheckoutPage checkout =
            cart.clickCheckout();

        // Fill all required fields
        checkout.fillMandatoryFields();

        // IMPORTANT
        checkout.clickPlaceOrder();

        // Validate success page
        Assert.assertTrue(
            checkout.isOrderSuccessDisplayed()
        );

    }
    
    @Test
    public void verifyCheckoutHeaderAndShopLink() {

        HomePage home = new HomePage(driver);

        Productlist list = home.clickShopNow();
        list.waitForProductsToLoad();

        list.clickProductByIndex(0);

        ProductDesc detail =
            new ProductDesc(driver);

        detail.addToCart();

        CartPage cart =
            home.clickCart();

        CheckoutPage checkout =
            cart.clickCheckout();

        // Verify Checkout label
        Assert.assertEquals(
            checkout.getCheckoutPageTitle(),
            "Checkout"
        );

        // Click SHOP logo
        checkout.clickShopLogo();

        // Verify navigated home
        Assert.assertTrue(
            driver.getCurrentUrl().endsWith("/")
        );
    }
        
    }