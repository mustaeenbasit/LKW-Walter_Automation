package com.sugarcrm.test.notes;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_17472 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Test Case 17472: Verify note creation - "Save"
	 * 
	 * @throws Exception
	 */
	@Ignore("SP-1722 - Search fail when query Salutation + Full name")
	@Test
	public void Notes_17472_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet messageData = testData.get(testName).get(0);

		// Click on "Notes" module > click "Create Note or Attachment".
		sugar().notes.navToListView();
		sugar().notes.listView.create();

		// Create drawer controls for Notes module
		VoodooControl subjectCtrl = sugar().notes.createDrawer.getEditField("subject");
		VoodooControl contactCtrl = sugar().notes.createDrawer.getEditField("contact");
		VoodooControl relRelatedToModuleCtrl = sugar().notes.createDrawer.getEditField("relRelatedToModule");
		VoodooControl relRelatedToValueCtrl = sugar().notes.createDrawer.getEditField("relRelatedToValue");
		String contactName = sugar().contacts.getDefaultData().get("fullName");
		String AccountName = sugar().accounts.getDefaultData().get("name");

		// Enter Subject, Select Contact, select "Related To" = Account, and select an existing account
		subjectCtrl.set(testName);
		contactCtrl.set(contactName);
		relRelatedToModuleCtrl.set(sugar().accounts.moduleNameSingular);
		relRelatedToValueCtrl.set(AccountName);

		// Verify that all fields should be properly populated with your selections
		subjectCtrl.assertEquals(testName, true);
		contactCtrl.assertContains(contactName, true);
		relRelatedToModuleCtrl.assertEquals(sugar().accounts.moduleNameSingular, true);
		relRelatedToValueCtrl.assertEquals(AccountName, true);

		// Click "Save" button
		sugar().notes.createDrawer.save();

		// Verify a message indicating that the record was successfully saved.
		sugar().alerts.getSuccess().assertEquals(messageData.get("message") + " " + testName + ".", true);
		sugar().alerts.getSuccess().closeAlert();

		// Verify that it will taken to the "Notes" list view and the newly created note appears, properly displaying the users selected values
		sugar().notes.listView.assertVisible(true);
		sugar().notes.listView.getDetailField(1, "subject").assertEquals(testName, true);
		sugar().notes.listView.getControl("checkbox02").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}