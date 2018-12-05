package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_28146 extends SugarTest{
	DataSource productCatalogData;

	public void setup() throws Exception {
		productCatalogData = testData.get(testName);

		// Create a Quote Record
		sugar.quotes.api.create();

		// Create 6 Product Catalog Records
		sugar.productCatalog.api.create(productCatalogData);


		// Log-in as Administrator
		sugar.login();
	}

	/** Verify that Quote line items retains their proper order when moving line items to new groups
	 *@throws Exception
	 */
	@Test
	public void Quotes_28146_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to edit view of a Quote record
		sugar.quotes.navToListView();
		sugar.quotes.listView.clickRecord(1);
		sugar.quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Edit any quote record and add extra line items to the quote until there are at least 6 line items.
		// TODO: VOOD-865
		VoodooControl addGroupCtrl = new VoodooControl("input", "id", "add_group");
		VoodooControl addRowCtrl = new VoodooControl("input", "css", "input[name='Add Row']");
		addGroupCtrl.click();
		for (int i = 0; i < productCatalogData.size(); i++) {
			addRowCtrl.click();
			new VoodooControl("input", "css", "input[name='product_name["+(i+1)+"]']").set(productCatalogData.get(i).get("name"));
			new VoodooControl("li", "css", "#EditView_name_"+(i+1)+"_results li:nth-child(1)").click();
		}

		// Scroll down to add a new group.
		addGroupCtrl.click();

		// Click the Add Row button three times so that there are three empty rows.
		VoodooControl addNewRowCtrl = new VoodooControl("input", "css", "table:nth-child(10) input[name='Add Row']");
		for (int i = 0; i < productCatalogData.size()/2; i++) {
			addNewRowCtrl.click();
		}

		// Using xPath because 'css' is dynamic while cut and paste the group items 
		// Scroll back up to the first group and click the || button on the left side of any line item to cut the line item.
		// TODO: VOOD-865
		// Cut PC1
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[1]/tbody/tr[2]/td[1]/input").click(); 

		// Scroll down to the second group and click the || button on the first empty line item row.
		//The line item should now be moved to this new row from the first group.
		// Paste PC1
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[3]/tbody/tr[2]/td[1]/input").click(); 

		// Verify that PC1 appears in first row a of Group 2 and disappears from Group 1
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[3]/tbody/tr[2]/td[3]/input").
		assertEquals(productCatalogData.get(0).get("name"), true);
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[1]/tbody/tr[2]/td[3]/input").
		assertEquals("", true);

		// Now go to the first group again and click the || button for any of the remaining quotes (remember which one this is)
		// Cut PC2
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[1]/tbody/tr[3]/td[1]/input").click(); 

		// Scroll down to the second group and click the || button to paste in the second line item. (So far all looks good)
		// Paste PC2
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[3]/tbody/tr[3]/td[1]/input").click(); 

		VoodooUtils.waitForReady();
		
		// Verify that PC2 appears in second row a of Group 2 and disappears from Group 1
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[3]/tbody/tr[3]/td[3]/input").
		assertEquals(productCatalogData.get(1).get("name"), true);
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[1]/tbody/tr[3]/td[3]/input").
		assertEquals("", true);

		// Scroll up to the first group and click the || button on the for any of the remaining line item row.
		// Cut PC3
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[1]/tbody/tr[4]/td[1]/input").click(); 

		// Scroll back down to the second group and click the || button to paste in the third line item.
		// Paste PC3
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[3]/tbody/tr[4]/td[1]/input").click(); 

		// Verify that the third line item should be remains in the third row in the second group.
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[3]/tbody/tr[4]/td[3]/input").
		assertEquals(productCatalogData.get(2).get("name"), true);
		new VoodooControl("input", "xpath", "//*[@id='add_tables']/table[1]/tbody/tr[4]/td[3]/input").
		assertEquals("", true);

		VoodooUtils.focusDefault();

		// Click on the Cancel button on Quotes edit view page
		sugar.quotes.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}