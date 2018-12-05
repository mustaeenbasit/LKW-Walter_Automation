package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24341 extends SugarTest {
	LeadRecord myLead;
	StandardSubpanel leadsSubpanel;
	FieldSet leadsDS;

	public void setup() throws Exception {
		sugar().login();
		sugar().opportunities.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		leadsDS = testData.get(testName).get(0);

		// Link lead and opportunity
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		leadsSubpanel = sugar().opportunities.recordView.subpanels.get("Leads");
		leadsSubpanel.linkExistingRecord(myLead);
	}

	/**
	 * Test Case 24341: Edit Lead_Verify that lead related to an opportunity can be inline-edited in "Leads" sub-panel
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24341_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Inline-edit the related lead record
		leadsSubpanel.expandSubpanel();
		leadsSubpanel.scrollIntoView();
		leadsSubpanel.editRecord(1);
		
		// Update sub-panel inline record
		sugar().leads.getField("firstName").listViewEditControl.set(leadsDS.get("firstName"));
		sugar().leads.getField("lastName").listViewEditControl.set(leadsDS.get("lastName"));
		sugar().leads.getField("phoneWork").listViewEditControl.set(leadsDS.get("phoneWork"));
		leadsSubpanel.saveAction(1);

		// Verify that lead is correctly modified
		leadsSubpanel.verify(1, leadsDS, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}