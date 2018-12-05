package com.sugarcrm.test.opportunities;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Opportunities_19045 extends SugarTest {
	public void setup() throws Exception {
		// create an account record through api
		sugar().accounts.api.create();
		sugar().productCategories.api.create();
		sugar().login();

		// TODO: VOOD-444 
		// Create opportunity record through UI to add relationship with account and rli record.
		sugar().opportunities.create();

		// link product category with rli record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("category").set(sugar().productCategories.getDefaultData().get("name"));
		sugar().revLineItems.recordView.save();
	}

	/**
	 * Verify that quote could not be created from RLI sub-panel of Opportunity record view when RLI has product category but does not have a product associated to the record  
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_19045_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open the opportunity record created during setup in the record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.scrollIntoViewIfNeeded(false);
		// TODO: SC-4692
		rliSubPanel.toggleSubpanel();
		// In the Revenue Line Items subpanel check RLI record
		rliSubPanel.checkRecord(1);
		// open action dropdown
		rliSubPanel.openActionDropdown();
		// Select "Generate Quote" from the actions menu of RLI subpanel 
		rliSubPanel.generateQuote();
		// Verify that the error message appear
		sugar().alerts.getError().assertVisible(true);
		// Assert error message string
		sugar().alerts.getError().assertElementContains(testData.get(testName).get(0).get("error_message"), true);
		sugar().alerts.getError().closeAlert();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}