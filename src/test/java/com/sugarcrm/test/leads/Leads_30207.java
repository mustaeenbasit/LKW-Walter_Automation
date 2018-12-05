package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Assert;
import org.junit.Test;

public class Leads_30207 extends SugarTest {
	FieldSet fs = new FieldSet();
	StandardSubpanel callsPanel, meetingsPanel, tasksPanel, notesPanel,
			emailPanel, leadsPanel, contactsPanel;

	public void setup() throws Exception {
		sugar().leads.api.create();
		fs = testData.get(testName).get(0);
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().login();

		// Admin->system settings->Lead Conversion Options->Move.
		sugar().admin.navToAdminPanelLink("systemSettings");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1903 - Additional System Settings support
		new VoodooControl("select", "css", "[name='lead_conv_activity_opt']").set(fs.get("option"));
		sugar().admin.systemSettings.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

		sugar().leads.navToListView();
		// edit the lead record to update email field value
		sugar().leads.listView.editRecord(1);
		sugar().leads.listView.getEditField(1, "emailAddress").set(fs.get("email"));
		sugar().leads.listView.saveRecord(1);
		sugar().leads.listView.getDetailField(1, "fullName").scrollIntoViewIfNeeded(sugar().leads.listView.getControl("horizontalScrollBar"), false);
		sugar().leads.listView.clickRecord(1);

		// subpanel list on leads module recordview
		callsPanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		meetingsPanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		tasksPanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		notesPanel = sugar().leads.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		emailPanel = sugar().leads.recordView.subpanels.get(sugar().emails.moduleNamePlural);

		// In Lead's subpanels, add a new record in each the following subpanels - Calls, Meetings, Tasks, Notes.
		callsPanel.create(sugar().calls.getDefaultData());
		meetingsPanel.create(sugar().meetings.getDefaultData());
		tasksPanel.create(sugar().tasks.getDefaultData());
		notesPanel.create(sugar().notes.getDefaultData());

		// In Lead's Emails subpanel, add a sent email and a draft email.
		emailPanel.scrollIntoViewIfNeeded(false);
		emailPanel.composeEmail();
		// Enter Subject
		sugar().leads.recordView.composeEmail.getControl("subject").set(testName);
		// Enter the Body content
		sugar().leads.recordView.composeEmail.addBodyMessage(testName);
		// Click on the send button to sent a mail
		sugar().leads.recordView.composeEmail.getControl("sendButton").click();
		VoodooUtils.waitForReady();

		// refresh to set focus back to email frame
		VoodooUtils.refresh();
		emailPanel.scrollIntoViewIfNeeded(false);
		emailPanel.composeEmail();
		// Enter Subject
		sugar().leads.recordView.composeEmail.getControl("subject").set(fs.get("leadName"));
		// Enter the Body content
		sugar().leads.recordView.composeEmail.addBodyMessage(testName);
		// Click on the save draft button
		sugar().leads.recordView.composeEmail.getControl("actionDropdown").click();
		sugar().leads.recordView.composeEmail.getControl("saveDraft").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that "Move" action will move Lead's activities into the Converted Contact
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_30207_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();

		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		VoodooControl convertButton = new VoodooControl("a", "css", ".fld_lead_convert_button.detail a");
		VoodooControl accountName = new VoodooControl("input", "css", "div[data-module='Accounts'] "
				+ ".fld_name.edit input");
		VoodooControl associateAccountBtn = new VoodooControl("a", "css", "div[data-module='Accounts'] "
				+ ".fld_associate_button.convert-panel-header a");
		VoodooControl saveAndConvertButton = new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a");

		// Open the Lead record.
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		// Select action Convert.
		convertButton.click();
		VoodooUtils.waitForReady();
		// Associate Account, Contacts.
		accountName.set(fs.get("accountName"));
		associateAccountBtn.click();
		VoodooUtils.waitForReady();
		// Click on "Save and Convert".
		saveAndConvertButton.click();
		VoodooUtils.waitForReady();

		// Verify that The record in the following subpanel are not there any more - Calls, Meetings, Tasks, Notes.
		Assert.assertEquals("Calls records not exactly equals to 0", 0, callsPanel.countRows());
		Assert.assertEquals("Meetings records not exactly equals to 0", 0, meetingsPanel.countRows());
		Assert.assertEquals("Tasks records not exactly equals to 0", 0, tasksPanel.countRows());
		Assert.assertEquals("Notes records not exactly equals to 0", 0, notesPanel.countRows());

		// Verify that The 2 email records remain at the Emails subpanel.
		Assert.assertEquals("Email records not exactly equals to 2", 2, emailPanel.countRows());

		// Look at the associated Contact record view.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// subpanel list on contacts module recordview
		callsPanel = sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		meetingsPanel = sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		tasksPanel = sugar().contacts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		notesPanel = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		emailPanel = sugar().contacts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		leadsPanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);

		// Verify that The associated Account appears in the "Account Name" field.
		sugar().contacts.recordView.getDetailField("relAccountName").assertContains(fs.get("accountName"), true);

		// Verify that the record in the following subpanel are not appearing - Calls, Meetings, Tasks, Notes.
		Assert.assertEquals("Calls records not exactly equals to 1", 1, callsPanel.countRows());
		Assert.assertEquals("Meetings records not exactly equals to 1", 1, meetingsPanel.countRows());
		Assert.assertEquals("Tasks records not exactly equals to 1", 1, tasksPanel.countRows());
		Assert.assertEquals("Notes records not exactly equals to 1", 1, notesPanel.countRows());

		leadsPanel.expandSubpanel();
		// The converted Lead appears in the Leads subpanel
		Assert.assertEquals("Leads records not exactly equals to 1", 1, leadsPanel.countRows());

		// Verify that The 2 email records remain at the Emails subpanel.
		Assert.assertEquals("Email records not exactly equals to 2", 2, emailPanel.countRows());

		// Look at the associated Account record view.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// subpanel list on accounts module recordview
		callsPanel = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		meetingsPanel = sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		tasksPanel = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		notesPanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		emailPanel = sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		leadsPanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		contactsPanel = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);

		// Verify that The record in the following subpanel are not there any more - Calls, Meetings, Tasks, Notes.
		Assert.assertEquals("Calls records not exactly equals to 0", 0, callsPanel.countRows());
		Assert.assertEquals("Meetings records not exactly equals to 0", 0, meetingsPanel.countRows());
		Assert.assertEquals("Tasks records not exactly equals to 0", 0, tasksPanel.countRows());
		Assert.assertEquals("Notes records not exactly equals to 0", 0, notesPanel.countRows());

		contactsPanel.expandSubpanel();
		// Verify that The new Contact record appears in the Contacts subpanel.
		Assert.assertEquals("Contacts records not exactly equals to 1", 1, contactsPanel.countRows());
		// Verify that The converted Lead record appears in the Leads subpanel.
		Assert.assertEquals("Leads records not exactly equals to 1", 1, leadsPanel.countRows());

		// Verify that The 2 email records remain at the Emails subpanel.
		Assert.assertEquals("Email records not exactly equals to 2", 2, emailPanel.countRows());

		// Navigate to email record
		emailPanel.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		String fullName = sugar().leads.getDefaultData().get("fullName");
		// TODO: VOOD-1713 - Improve BWCSubpanel functionality
		// Verify that The new Contact record appears at "Contacts" and "Email Contacts" subpanel.
		new VoodooControl("a", "css", "#list_subpanel_contacts tr.oddListRowS1 span a").assertContains(fullName, true);
		new VoodooControl("a", "css", "#list_subpanel_contacts_snip tr.oddListRowS1 span a").assertContains(fullName.substring(5), true);
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}