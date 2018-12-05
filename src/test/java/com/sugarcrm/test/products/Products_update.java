package com.sugarcrm.test.products;

import java.text.NumberFormat;
import java.util.Locale;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCatalogRecord;
import com.sugarcrm.test.SugarTest;

public class Products_update extends SugarTest {
	ProductCatalogRecord myProduct;

	public void setup() throws Exception {
		sugar.login();
		myProduct = (ProductCatalogRecord)sugar.productCatalog.api.create();
	}

	@Test
	public void Products_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Marion Gadget");
		newData.put("status", "Out Of Stock");
		newData.put("costPrice", "1000");
		newData.put("unitPrice", "1000");
		newData.put("listPrice", "1000");
		newData.put("defaultPricingFormula", "Discount from List");
		newData.put("pricingFactor", "10");

		// Edit the product catalog record using the UI.
		myProduct.edit(newData);

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		Double costPrice = Double.parseDouble(newData.get("costPrice"));
		Double unitPrice = Double.parseDouble("900");
		Double listPrice = Double.parseDouble(newData.get("listPrice"));
		newData.put("costPrice", currencyFormat.format(costPrice));
		newData.put("unitPrice", currencyFormat.format(unitPrice));
		newData.put("listPrice", currencyFormat.format(listPrice));
		newData.remove("defaultPricingFormula");
		newData.remove("pricingFactor");

		// Verify the product catalog record was edited successfully.
		myProduct.verify(newData);  

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}