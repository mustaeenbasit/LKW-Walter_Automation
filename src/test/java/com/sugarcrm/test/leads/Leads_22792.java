package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22792 extends SugarTest {
	DataSource leadsDS = new DataSource();

	public void setup() throws Exception {
		leadsDS = testData.get(testName);
		
		// Create 2 Leads
		sugar().leads.api.create(leadsDS);
		sugar().login();
	}

	/**
	 * Test Case 22792: Verify that merge edit view can be displayed
	 * @throws Exception
	 */
	@Test
	public void Leads_22792_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Leads listview
		sugar().leads.navToListView();

		// Select at least 2 Lead records. Open action dropdown 
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();

		// Click 'Merge'
		// TODO: VOOD-689
		new VoodooControl("a", "name", "merge_button").click();
		VoodooUtils.waitForReady();
		
		// Verify that merge edit view is displayed
		// TODO: VOOD-721
		new VoodooControl("span", "css", ".fld_title.merge-duplicates-headerpane").assertContains
			("Merging 2 Records", true);
		new VoodooControl("span", "css", ".record-name").assertContains(leadsDS.get(1).get
			("firstName")+" "+leadsDS.get(1).get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}