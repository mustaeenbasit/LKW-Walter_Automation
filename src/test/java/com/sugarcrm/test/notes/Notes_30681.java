package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Notes_30681 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Related To filed is working as expected during creation of Note in subpanel of Contact
	 * @throws Exception
	 */
	@Test
	public void Notes_30681_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel notesSP = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSP.addRecord();

		// Select Accounts module in Related Module
		sugar().notes.createDrawer.getEditField("relRelatedToModule").set(sugar().accounts.moduleNameSingular);

		// Click on the "Related to" dropdown to expand it
		VoodooSelect relatedTo = (VoodooSelect) sugar().notes.createDrawer.getEditField("relRelatedToValue");
		relatedTo.click();

		// Click "Search and Select..." link in the dropdown
		relatedTo.selectWidget.getControl("searchForMoreLink").click();

		// Verify Accounts Search & Select Drawer opens up
		sugar().accounts.searchSelect.assertVisible(true);

		// Cancel the Search Select Drawer
		sugar().accounts.searchSelect.cancel();

		// Cancel the Notes Create Drawer
		sugar().notes.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}