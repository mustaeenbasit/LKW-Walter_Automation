package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_16997 extends SugarTest {		
	FieldSet dupeData;

	public void setup() throws Exception {
		dupeData = testData.get("env_dupe_assertion").get(0);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify Office Phone number is matched exactly in lead record results into dup while "Save"
	 * @throws Exception
	 */
	@Test
	public void Leads_16997_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().leads, "createLead");
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.getEditField("phoneWork").set(sugar().leads.getDefaultData().get("phoneWork"));
		sugar().leads.createDrawer.save();

		// Verify duplicates found message, button is enabled, office phone number has exact with existing record
		sugar().leads.createDrawer.getControl("duplicateCount").assertContains(dupeData.get("dupe_check"), true);
		sugar().leads.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);
		sugar().leads.recordView.getEditField("phoneWork").assertEquals(sugar().leads.getDefaultData().get("phoneWork"), true);

		// Save duplicated record 
		sugar().leads.createDrawer.ignoreDuplicateAndSave();

		// Verify there are 2 records on leads listview
		sugar().leads.listView.verifyField(1, "phoneWork", sugar().leads.getDefaultData().get("phoneWork"));
		sugar().leads.listView.verifyField(2, "phoneWork", sugar().leads.getDefaultData().get("phoneWork"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}