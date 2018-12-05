package com.sugarcrm.test.leads;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17006 extends SugarTest {
	DataSource leadsNames;
	LeadRecord myLead;
	FieldSet dupeData;
	
	public void setup() throws Exception {
		leadsNames = testData.get(testName);
		dupeData = testData.get("env_dupe_assertion").get(0);
		sugar().login();
	}

	/**
	 * Verify only Last Name matching results into dup check in Lead
	 * @throws Exception
	 */
	@Test
	public void Leads_17006_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Go to the Leads module
		sugar().leads.navToListView();

		// Create a new lead record with last name, rest fields leave blank
		sugar().leads.listView.create();
		sugar().leads.createDrawer.setFields(leadsNames.get(0));
		sugar().leads.createDrawer.save();

		// Create another new lead record with last name including the whole first leads second name
		sugar().leads.listView.create();
		sugar().leads.createDrawer.setFields(leadsNames.get(1));
		// Verify the 2nd new record was saved without warnings
		sugar().leads.createDrawer.save();

		// Create 3rd record with last name as part of second lead's last name but including the whole first lead's last name
		sugar().leads.listView.create();
		sugar().leads.createDrawer.setFields(leadsNames.get(2));
		sugar().leads.createDrawer.save();

		// Verify: "Ignore Duplicate and Save" button is enabled
		sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);
		Assert.assertFalse("Button is disabled", sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").isDisabled());

		// Verify: A message displaying: 1 duplicates found.
		sugar().leads.createDrawer.getControl("duplicateCount").assertContains(dupeData.get("dupe_check"), true);

		// The existing dup record (second one) displays too
		// TODO: VOOD-513
		new VoodooControl("span", "css", "div[data-voodoo-name='dupecheck-list-edit'] table tbody tr span.fld_full_name").assertContains(leadsNames.get(1).get("lastName"), true);

		sugar().leads.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}