package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_17383 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Product, Expected Close Date, and Product Line Item Amount fields are highlighted as required fields on save if not filled in.
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17383_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create RLI -> save record without filling any required fields
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.save();

		// Verify RLI name, Opportunity Name, Expected Closed Date and Likely fields are highlighted in red indicating that those are required fields. 
		sugar().revLineItems.createDrawer.getEditField("name").assertAttribute("class", "required", true);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").getChildElement("span", "css", "span.select2-chosen").assertEquals("Required", true);
		sugar().revLineItems.createDrawer.getEditField("date_closed").assertAttribute("class", "required", true);
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertAttribute("class", "required", true);
		sugar().alerts.getError().closeAlert();
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}