package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22730 extends SugarTest {
	DataSource customLeadDS = new DataSource();

	public void setup() throws Exception {
		// custom leads
		customLeadDS = testData.get(testName);
		sugar().leads.api.create(customLeadDS);
		sugar().login();
		sugar().leads.navToListView();
	}

	/**
	 * Verify that records selected from leads list view can be merged
	 * @throws Exception
	 */
	@Test
	public void Leads_22730_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();

		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list").click();
		new VoodooControl("input", "css", ".fld_last_name.edit input").set(testName);

		// Click Save
		new VoodooControl("a", "css", ".merge-duplicates-headerpane a[name='save_button']").click();
		sugar().alerts.getWarning().confirmAlert();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
		
		// Verify there is only 1 record in the list view
		Assert.assertTrue("Record is not equal one", sugar().leads.listView.countRows() == 1);
		sugar().leads.listView.getDetailField(1, "fullName").assertContains(customLeadDS.get(2).get("firstName")+" "+testName, true);

		// Verify original merging lead records in leads list view does not exist
		sugar().leads.listView.setSearchString(customLeadDS.get(0).get("fullName"));
		sugar().leads.listView.assertIsEmpty();
		sugar().leads.listView.setSearchString(customLeadDS.get(1).get("fullName"));
		sugar().leads.listView.assertIsEmpty();
		sugar().leads.listView.clearSearch();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}