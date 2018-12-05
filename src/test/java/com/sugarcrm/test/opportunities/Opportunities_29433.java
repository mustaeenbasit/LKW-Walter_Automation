package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_29433 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that currency field does not have default value of 0 after conversion to Opportunities only mode
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_29433_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set Opportunity settings
		FieldSet oppSettingsData = testData.get(testName).get(0);
		sugar().admin.switchOpportunityView(oppSettingsData);

		// Go to Opportunity listView
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		// Likely, best and worst should not have a default value
		sugar().opportunities.createDrawer.getEditField("likelyCase").assertContains("", true);
		sugar().opportunities.createDrawer.getEditField("bestCase").assertContains("", true);
		sugar().opportunities.createDrawer.getEditField("worstCase").assertContains("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}