package utils;

import java.util.List;

import org.testng.Assert;

import pages.CartPage;
import pages.HomePage;
import pages.ProductDesc;
import pages.Productlist;

public class CartHelper {

	public static double calculateExpectedTotal(double... prices) {

	    double total = 0.0;

	    for (double price : prices) {
	        total += price;
	    }

	    return total;
	}
	
    
}
