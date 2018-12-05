package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Dashlets_26705 extends SugarTest {
	StandardSubpanel emailSubpanel, contactSubpanel;
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName);
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		CallRecord myCall = (CallRecord) sugar().calls.api.create();
		MeetingRecord myMeeting = (MeetingRecord) sugar().meetings.api.create();
		TaskRecord myTask = (TaskRecord) sugar().tasks.api.create();
		NoteRecord myNote = (NoteRecord) sugar().notes.api.create();
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// Configure Email settings
		sugar().admin.setEmailServer(emailSetup);

		// To make relationship between calls,meetings,tasks and notes with Contacts
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural).linkExistingRecord(myCall);
		sugar().opportunities.recordView.subpanels.get(sugar().meetings.moduleNamePlural).linkExistingRecord(myMeeting);
		sugar().opportunities.recordView.subpanels.get(sugar().tasks.moduleNamePlural).linkExistingRecord(myTask);
		sugar().opportunities.recordView.subpanels.get(sugar().notes.moduleNamePlural).linkExistingRecord(myNote);
		contactSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(myContact);
		emailSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.scrollIntoView();
		emailSubpanel.composeEmail();
		sugar().opportunities.recordView.composeEmail.getControl("addressBook").click();

		// TODO: VOOD-1423 - Need lib support for Accounts > recordView > composeEmail > ToAddress > AddressBook	 
		VoodooControl checkbox = new VoodooControl("input", "css", ".flex-list-view tr td input[type='checkbox']");
		checkbox.waitForVisible();
		checkbox.click();
		new VoodooControl("a", "css", "a[name='done_button']").click();
		VoodooUtils.waitForReady();
		sugar().opportunities.recordView.composeEmail.getControl("subject").set(customDS.get(0).get("subject"));
		sugar().opportunities.recordView.composeEmail.addBodyMessage(customDS.get(0).get("emailBody"));

		// Send Email
		sugar().opportunities.recordView.composeEmail.getControl("sendButton").click();
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Verify that modules names under Type columns are updated if corresponding module is renamed in Historical Summary page 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_26705_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Rename Calls module.
		String pluralCallName = customDS.get(0).get("pluralLabel");
		sugar().admin.renameModule(sugar().calls, customDS.get(0).get("singularLabel"), pluralCallName);

		// Rename Meetings module.
		String pluralMeetingName = customDS.get(1).get("pluralLabel");
		sugar().admin.renameModule(sugar().meetings, customDS.get(1).get("singularLabel"), pluralMeetingName);

		// Rename Tasks module.
		String pluralTaskName = customDS.get(2).get("pluralLabel");
		sugar().admin.renameModule(sugar().tasks, customDS.get(2).get("singularLabel"), pluralTaskName);

		// Rename Notes module.
		String pluralNoteName = customDS.get(3).get("pluralLabel");
		String singularNoteName = customDS.get(3).get("singularLabel");
		sugar().admin.renameModule(sugar().notes, singularNoteName, pluralNoteName);

		// Rename Contacts module.
		String pluralContactName = customDS.get(4).get("pluralLabel");
		sugar().admin.renameModule(sugar().contacts, customDS.get(4).get("singularLabel"), pluralContactName);

		// Rename Emails module.
		String pluralEmailName = customDS.get(5).get("pluralLabel");
		sugar().admin.renameModule(sugar().emails, customDS.get(5).get("singularLabel"), pluralEmailName);

		// Open the opportunity record created in the setup in the record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Verify renamed modules are displayed correctly
		sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertElementContains(pluralCallName, true);
		sugar().opportunities.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertElementContains(pluralMeetingName, true);
		sugar().opportunities.recordView.subpanels.get(sugar().tasks.moduleNamePlural).assertElementContains(pluralTaskName, true);
		sugar().opportunities.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertElementContains(pluralNoteName, true);
		contactSubpanel.assertElementContains(pluralContactName, true);
		emailSubpanel.assertElementContains(pluralEmailName, true);

		// Open Historical Summary page by click Historical Summary menu item under Actions dropdown in the opportunity record view
		sugar().opportunities.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-965 - Support for Historical Summary page
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify that the updated name of the module displayed under Type column. 
		VoodooControl iconCtrl1 = new VoodooControl("span", "css", ".label.label-module.label-module-lg.label-Emails");
		iconCtrl1.hover();
		iconCtrl1.assertVisible(true);
		VoodooControl iconCtrl2 = new VoodooControl("span", "css", ".label.label-module.label-module-lg.label-Notes");
		iconCtrl2.hover();
		iconCtrl2.assertVisible(true);
		
		// Verify that the Header is updated based on new name of the Contacts module 
		new VoodooControl("span", "css", "[data-fieldname='related_contact'] .ui-draggable span").assertContains(customDS.get(4).get("singularLabel"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}