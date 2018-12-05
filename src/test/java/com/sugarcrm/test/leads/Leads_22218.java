package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22218 extends SugarTest {
	DataSource leadsData = new DataSource();

	public void setup() throws Exception {
		leadsData = testData.get(testName);
		sugar().leads.api.create(leadsData);
		sugar().login();
	}

	/**
	 * Verify that records selected from the leads list view can be merged.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22218_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		FieldSet verifyMerge = testData.get(testName+"_Merged").get(0);
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();

		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list").click();
		new VoodooControl("input", "css", ".fld_first_name.edit input").set(verifyMerge.get("newFirstName"));
		new VoodooControl("input", "css", ".fld_last_name.edit input").set(verifyMerge.get("newLastName"));

		// Click the first name of the record in the second column
		new VoodooControl("input", "css", ".col:nth-of-type(2) input[name=copy_first_name]").click();
		new VoodooControl("a", "css", ".fld_save_button.merge-duplicates-headerpane a").click();
		sugar().alerts.getAlert().confirmAlert();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify there is only 1 record in the list view
		Assert.assertTrue("Record is not equal one", sugar().leads.listView.countRows() == 1);
		sugar().leads.listView.getDetailField(1, "fullName").assertContains(verifyMerge.get("newFullName"), true);

		// Verify original merging lead records in leads list view does not exist
		sugar().leads.listView.setSearchString(leadsData.get(0).get("fullName"));
		sugar().leads.listView.assertIsEmpty();
		sugar().leads.listView.setSearchString(leadsData.get(1).get("fullName"));
		sugar().leads.listView.assertIsEmpty();
		sugar().leads.listView.clearSearch();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}