package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_24183 extends SugarTest {
	StandardSubpanel directReports;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();

		// Add a related Direct Report for contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		directReports = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		directReports.addRecord();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.save();
	}

	/**
	 * Inline-edit direct reports_Verify that Direct Report record for contact can be
	 * correctly inline-edited in "Direct Reports" sub-panel
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_24183_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet contactData = testData.get(testName).get(0);

		// Inline-edit the contact in Direct report subpanel
		directReports.editRecord(1);
		directReports.getEditField(1, "firstName").set(contactData.get("firstName"));
		directReports.getEditField(1, "lastName").set(contactData.get("lastName"));
		directReports.saveAction(1);

		// Reload contact record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(2);

		// Verify that modified field is correctly displaying in Direct report sub panel
		// TODO: VOOD-1424 - Make StandardSubpanel.verify() verify specified value is in correct column.
		// directReports.verify(1, contactData, true);
		directReports.click();
		VoodooUtils.waitForReady();
		directReports.getDetailField(1, "fullName").assertContains(contactData.get("firstName")+ " " + contactData.get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}