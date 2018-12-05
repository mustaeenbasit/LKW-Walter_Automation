package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Cases_27067 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().cases.api.create();

		sugar().login();
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Create a contact record under Contact subpanel
		StandardSubpanel contactsSubpanel = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.addRecord();
		sugar().contacts.createDrawer.showMore();
		customData = testData.get(testName).get(0);
		sugar().contacts.createDrawer.getEditField("emailAddress").set(customData.get("email"));
		sugar().contacts.createDrawer.getEditField("lastName").set(customData.get("last_name"));
		sugar().contacts.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.confirmAllAlerts();
		sugar().contacts.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
		sugar().logout();
	}

	/**
	 * Verify that subject of case record has auto-filled in the subject of Email
	 * @throws Exception
	 */
	@Test
	public void Cases_27067_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login((sugar().users.getQAUser()));

		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		StandardSubpanel emailPanel = (StandardSubpanel)sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailPanel.composeEmail();
		VoodooUtils.waitForReady();

		// Verifying SMTP configuration warning message
		sugar().alerts.getWarning().assertContains(customData.get("warning_message"), true);
		sugar().alerts.getWarning().closeAlert();

		// Verifying subject
		// To assert value of subject case without CASE Macro
		sugar().cases.recordView.composeEmail.getControl("subject").assertContains(sugar().cases.getDefaultData().get("name"), true);

		// Verifying to address
		// TODO: VOOD-1423
		VoodooControl toRecipientCtrl = new VoodooControl("span", "css", ".fld_to_addresses.edit ul li span");
		toRecipientCtrl.assertEquals(customData.get("last_name"), true);
		toRecipientCtrl.assertAttribute("data-title", customData.get("email"), true);

		// Click Cancel button
		sugar().cases.recordView.composeEmail.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}