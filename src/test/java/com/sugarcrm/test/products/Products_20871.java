package com.sugarcrm.test.products;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20871 extends SugarTest {
	FieldSet fs;
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);	
		sugar.productCatalog.api.create(fs);
		sugar.login();			
	}

	/**
	 * Verify that price data larger than 1000 can be displayed in list view as entered ones.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20871_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// currency format 
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		Double costPrice = Double.parseDouble(fs.get("costPrice"));
		Double unitPrice = Double.parseDouble(fs.get("unitPrice"));
		Double listPrice = Double.parseDouble(fs.get("listPrice"));
		String strCostPrice =  currencyFormat.format(costPrice);
		String strUnitPrice =  currencyFormat.format(unitPrice);
		String strlistPrice =  currencyFormat.format(listPrice);
		
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.getDetailField(1, "name").assertEquals(fs.get("name"), true);
		sugar.productCatalog.listView.getDetailField(1, "costPrice").assertEquals(strCostPrice, true);
		sugar.productCatalog.listView.getDetailField(1, "unitPrice").scrollIntoViewIfNeeded(false);
		sugar.productCatalog.listView.getDetailField(1, "listPrice").assertEquals(strlistPrice, true);
		sugar.productCatalog.listView.getDetailField(1, "unitPrice").assertEquals(strUnitPrice, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}