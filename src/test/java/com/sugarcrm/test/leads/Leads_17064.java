package com.sugarcrm.test.leads;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_17064 extends SugarTest {
	FieldSet dupeData, leadsData;
	LeadRecord lead;

	public void setup() throws Exception {
		leadsData = testData.get(testName).get(0);
		dupeData = testData.get("env_dupe_assertion").get(0);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that a new record is created if clicking on "Ignore Duplicate and Save" button
	 * @throws Exception
	 */
	@Test
	public void Leads_17064_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.setFields(leadsData);
		sugar().leads.createDrawer.save();

		// Verify: "Ignore Duplicate and Save" button is enabled
		sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);
		Assert.assertFalse("Button is disabled", sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").isDisabled());

		// Verify: A message displaying: 1 duplicates found.
		sugar().leads.createDrawer.getControl("duplicateCount").assertContains(dupeData.get("dupe_check"), true);

		// Verify that "Office Phone" field has the exact number entered in the new record
		sugar().leads.createDrawer.getEditField("phoneWork").assertEquals(leadsData.get("phoneWork"), true);

		// Click "Ignore Duplicate and Save" button to save the record  
		sugar().leads.createDrawer.ignoreDuplicateAndSave();

		// Verify "Success You successfully created ..." message appears
		sugar().alerts.getSuccess().assertContains("Success You successfully created the lead "+leadsData.get("firstName")+" "+leadsData.get("lastName"), true);
		sugar().alerts.getSuccess().closeAlert();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that "Office Phone" field has the exact number in both records in listview 
		sugar().leads.listView.verifyField(1, "phoneWork", leadsData.get("phoneWork"));
		sugar().leads.listView.verifyField(2, "phoneWork", leadsData.get("phoneWork"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}