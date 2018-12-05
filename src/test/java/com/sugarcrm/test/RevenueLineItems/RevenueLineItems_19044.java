package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_19044 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().productCategories.api.create();
		sugar().login();

		// TODO: VOOD-444 - Once resolved Opportunity should create via API with account and RLI link
		sugar().opportunities.create();

		// The RLI record has a product category but does not have a product linked to it.  
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("category").set(sugar().productCategories.getDefaultData().get("name"));
		sugar().revLineItems.recordView.save();
	}

	/**
	 * Verify that quote could not be created from RLI record view when RLI has product category but does not have a product associated to the record  
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_19044_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Generate quote
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("generateQuote").click();
		VoodooUtils.waitForReady();

		// Verify error alert with message "The Revenue Line Item needs a Product from the Product Catalog before a Quote can be generated."
		Alert error = sugar().alerts.getError();
		error.assertVisible(true);
		error.assertEquals(testData.get(testName).get(0).get("error_message"), true);
		error.closeAlert();

		// Verify Quotes could not be generated from RLI, if without Product catalog
		sugar().quotes.navToListView();
		Assert.assertTrue("Quote is generated from RLI", sugar().quotes.listView.countRows() == 0);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}