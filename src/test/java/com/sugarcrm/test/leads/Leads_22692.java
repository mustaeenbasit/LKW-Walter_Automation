package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22692 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}
	
	/**
	 * Update Lead_Verify that all the lead source can be selected for modifying a lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_22692_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Navigate to leads list view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		DataSource leadSourceData = testData.get(testName);
		
		// Click edit button & select each lead source in the drop down list of "Lead Source" save.
		for (int i = 0; i < leadSourceData.size(); i++) {
			sugar().leads.recordView.edit();
			sugar().leads.recordView.showMore();

			// Update the "lead source" fields
			sugar().leads.recordView.getEditField("leadSource").set(leadSourceData.get(i).get("leadSource"));
			sugar().leads.recordView.save();

			// Verify the selected lead source is displayed in the detail view of the modified lead.
			sugar().leads.recordView.getDetailField("leadSource").assertEquals(leadSourceData.get(i).get("leadSource"), true);
		}

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
