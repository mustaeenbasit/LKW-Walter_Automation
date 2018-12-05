package com.sugarcrm.test.products;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCatalogRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.text.NumberFormat;
import java.util.Locale;

public class Products_create extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	@Test
	public void Products_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ProductCatalogRecord myProduct = (ProductCatalogRecord)sugar.productCatalog.create();

		// update object values for currency verification
		// TODO: VOOD-1402 Need to support verification of formatted currency fields
		// when VOOD-1402 fixed, remove line 28-34 
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		Double costPrice = Double.parseDouble(sugar.productCatalog.getDefaultData().get("costPrice"));
		Double unitPrice = Double.parseDouble(sugar.productCatalog.getDefaultData().get("unitPrice"));
		Double listPrice = Double.parseDouble(sugar.productCatalog.getDefaultData().get("listPrice"));
		myProduct.put("costPrice", currencyFormat.format(costPrice));
		myProduct.put("unitPrice", currencyFormat.format(unitPrice));
		myProduct.put("listPrice", currencyFormat.format(listPrice));

		// TODO: When currency field verification is implemented in library
		// update verification to use default data.
		myProduct.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}