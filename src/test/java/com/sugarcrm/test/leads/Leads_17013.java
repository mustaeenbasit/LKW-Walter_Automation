package com.sugarcrm.test.leads;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17013 extends SugarTest {
	DataSource leadsNames;
	FieldSet dupeData;
	LeadRecord myLead;

	public void setup() throws Exception {
		leadsNames = testData.get(testName);
		dupeData = testData.get("env_dupe_assertion").get(0);
		sugar().login();
	}

	/**
	 * Verify only Last Name, First Name and Acccount matching result into dup check in Lead
	 * @throws Exception
	 */
	@Test
	public void Leads_17013_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Go to the Leads module
		sugar().leads.navToListView();

		// Create a new lead record with last name as Tim, enter "josh" in first name, "acc" in Account field, rest fields leave blank.
		sugar().leads.listView.create();
		sugar().leads.createDrawer.setFields(leadsNames.get(0));
		sugar().leads.createDrawer.save();

		// Create 2nd new lead record with last name as Tim, enter "Josh" in first name, "Acc" in Account field, rest fields leave blank.
		sugar().leads.listView.create();
		sugar().leads.createDrawer.setFields(leadsNames.get(1));
		sugar().leads.createDrawer.save();

		// Verify: "Ignore Duplicate and Save" button is enabled
		sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);
		Assert.assertFalse("Button is disabled", sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").isDisabled());

		// Verify: A message displaying: 1 duplicates found.
		sugar().leads.createDrawer.getControl("duplicateCount").assertContains(dupeData.get("dupe_check"), true);

		// The existing dup record displays too
		// TODO: VOOD-513
		new VoodooControl("span", "css", "div[data-voodoo-name='dupecheck-list-edit'] table tbody tr span.fld_full_name").assertContains(leadsNames.get(0).get("firstName")+" "+leadsNames.get(0).get("lastName"), true);
		new VoodooControl("span", "css", "div[data-voodoo-name='dupecheck-list-edit'] table tbody tr span.fld_account_name").assertContains(leadsNames.get(0).get("accountName"), true);
		sugar().leads.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}