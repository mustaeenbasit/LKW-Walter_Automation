package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24361 extends SugarTest {
	LeadRecord myLead;
	OpportunityRecord myOpp;
	StandardSubpanel leadsSubpanel;
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
	}

	/**
	 * Test Case 24361: Link Existing Lead_Verify that an existing lead can be linked with the opportunity by clicking "Lead Name"
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24361_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		leadsSubpanel = sugar().opportunities.recordView.subpanels.get("Leads");
		leadsSubpanel.clickLinkExisting();
		new VoodooControl("span", "css", ".fld_full_name.list").waitForVisible();
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		VoodooUtils.waitForAlertExpiration();

		// Verify that linked lead is available in the leads subpanel
		fs.put("fullName", myLead.getRecordIdentifier()); 
		leadsSubpanel.verify(1, fs, true);
 
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}