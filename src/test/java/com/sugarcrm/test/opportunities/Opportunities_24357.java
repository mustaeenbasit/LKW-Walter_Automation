package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24357 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel leadsSubpanel;
	DataSource leadDS;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		leadDS = testData.get(testName);
	}

	/**
	 * Test Case 24357: In-Line Create Lead_Verify that lead can be in-line created from Leads sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24357_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		leadsSubpanel = sugar().opportunities.recordView.subpanels.get("Leads");
		// Open opportunity record view and inline-create a related lead
		myOpp.navToRecord();
		leadsSubpanel.addRecord();
		sugar().leads.createDrawer.getEditField("lastName").set(leadDS.get(0).get("lastName"));
		sugar().leads.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		// Verify the lead is successfully created and visible in leads subpanel of the opportunity
		myOpp.navToRecord();
		leadsSubpanel.expandSubpanel();
		new VoodooControl("a", "css", ".fld_full_name.list a").assertContains((leadDS.get(0).get("lastName")), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}