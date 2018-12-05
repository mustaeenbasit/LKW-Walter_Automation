package com.sugarcrm.test.leads;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17065 extends SugarTest {
	FieldSet dupeData;

	public void setup() throws Exception {
		dupeData = testData.get("env_dupe_assertion").get(0);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that no new record is created if cancel after duplicate is detected 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_17065_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().leads, "createLead");
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.getEditField("firstName").set(sugar().leads.getDefaultData().get("firstName").toLowerCase());
		sugar().leads.createDrawer.getEditField("lastName").set(sugar().leads.getDefaultData().get("lastName"));
		sugar().leads.createDrawer.save();

		// Verify "Ignore Duplicate and Save" button is enabled
		sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);
		Assert.assertFalse("Button is disabled", sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").isDisabled());
		
		// Verify the message saying: 1 duplicates found appears on the page
		sugar().leads.createDrawer.getControl("duplicateCount").assertContains(dupeData.get("dupe_check"), true);
		sugar().leads.createDrawer.cancel();

		// Verify that no new record is created
		sugar().leads.listView.deleteRecord(1);
		sugar().alerts.getWarning().confirmAlert();
		sugar().leads.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}