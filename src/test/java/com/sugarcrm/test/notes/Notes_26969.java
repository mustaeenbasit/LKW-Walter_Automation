package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class Notes_26969 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the related field link should work properly in record view
	 * @throws Exception
	 */
	@Test
	public void Notes_26969_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet accountsDefaultData = sugar().accounts.getDefaultData();

		// Create a Notes Record with related account field
		sugar().notes.navToListView();
		sugar().notes.listView.create();
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.getDefaultData().get("subject"));
		sugar().notes.createDrawer.getEditField("relRelatedToValue").set(accountsDefaultData.get("name"));
		sugar().notes.createDrawer.save();

		// Navigate to the record view of the Note Record
		sugar().notes.listView.clickRecord(1);

		// Click on the related Account Link
		sugar().notes.recordView.getDetailField("relRelatedToValue").click();

		// Verify link to account record view
		sugar().accounts.recordView.getDetailField("name").assertEquals(accountsDefaultData.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}