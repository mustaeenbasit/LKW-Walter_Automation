package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_30212 extends SugarTest {
	FieldSet customData = new FieldSet();
	StandardSubpanel emailsSubpanel, meetingsSubpanel, taskSubpanel, noteSubpanel, callsSubpanel;
	LeadRecord leadRec;
	String callsPlural = "", tasksPlural = "", meetingsPlural = "", emailsPlural = "", notesPlural = "";

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		leadRec = (LeadRecord) sugar().leads.api.create();
		sugar().login();

		// Configure Email settings
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().admin.setEmailServer(emailSetup);

		// Navigate to system setting
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl systemSettingCtrl = sugar().admin.adminTools.getControl("systemSettings");
		systemSettingCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1903 - Additional System Settings support
		// Changing lead conversion option from 'Do Nothing' to 'Move'
		VoodooControl leadConvertingCtrl = new VoodooControl("select", "css", "[name='lead_conv_activity_opt']");
		leadConvertingCtrl.set(customData.get("leadConversionOption"));

		// Saving the system settings
		VoodooControl systemSettingSaveCtrl = sugar().admin.systemSettings.getControl("save");
		systemSettingSaveCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Navigate to lead module
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Creating one Call record from calls subpanel
		callsPlural = sugar().calls.moduleNamePlural;
		callsSubpanel = (StandardSubpanel) sugar().leads.recordView.subpanels.get(callsPlural);
		callsSubpanel.addRecord();
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));
		sugar().calls.createDrawer.save();

		// Creating one Meeting record from meetings subpanel
		meetingsPlural = sugar().meetings.moduleNamePlural;
		meetingsSubpanel = (StandardSubpanel) sugar().leads.recordView.subpanels.get(meetingsPlural);
		meetingsSubpanel.addRecord();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.save();

		// Creating one Task record from Tasks subpanel
		tasksPlural = sugar().tasks.moduleNamePlural;
		taskSubpanel = (StandardSubpanel) sugar().leads.recordView.subpanels.get(tasksPlural);
		taskSubpanel.addRecord();
		sugar().tasks.createDrawer.getEditField("subject").set(sugar().tasks.getDefaultData().get("subject"));
		sugar().tasks.createDrawer.save();

		// Creating one Note record from Notes subpanel
		notesPlural = sugar().notes.moduleNamePlural;
		noteSubpanel = (StandardSubpanel) sugar().leads.recordView.subpanels.get(notesPlural);
		noteSubpanel.addRecord();
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.getDefaultData().get("subject"));
		sugar().notes.createDrawer.save();

		// Adding one sent email to the leads subpanel
		emailsPlural = sugar().emails.moduleNamePlural;
		emailsSubpanel = (StandardSubpanel) sugar().leads.recordView.subpanels.get(emailsPlural);
		emailsSubpanel.composeEmail();
		VoodooControl toAddCtrl = sugar().leads.recordView.composeEmail.getControl("toAddress");
		String sentEmail = customData.get("sentEmail");
		toAddCtrl.set(sentEmail);
		
		// TODO: VOOD-843 -Lib support to handle new email composer UI
		VoodooControl toAddSelectCtrl = new VoodooControl("div", "class", "select2-result-label");
		toAddSelectCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl subjectCtrl = sugar().leads.recordView.composeEmail.getControl("subject");
		subjectCtrl.set(customData.get("sentEmailSubject"));
		sugar().leads.recordView.composeEmail.getControl("sendButton").click();
		sugar().alerts.getAlert().confirmAlert();
		VoodooUtils.waitForReady();

		// Adding one draft email to the leads subpanel
		emailsSubpanel.getControl("composeEmail").click();
		VoodooUtils.waitForReady();
		toAddCtrl.set(sentEmail);
		toAddSelectCtrl.click();
		VoodooUtils.waitForReady();
		subjectCtrl.set(customData.get("draftEmailSubject"));
		sugar().leads.recordView.composeEmail.getControl("actionDropdown").click();
		sugar().leads.recordView.composeEmail.getControl("saveDraft").click();
	}

	/**
	 * Verify that "Move" action will move Lead's activities into the Converted Contact without email address
	 * @throws Exception
	 */
	@Test
	public void Leads_30212_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Clicking on edit action dropdown of lead in record view
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Click "Convert Lead" button in "Lead" record view.
		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Fill in Account name and click Associate Account
		String accountName = sugar().accounts.getDefaultData().get("name");
		new VoodooControl("input", "css", "#collapseAccounts .fld_name input").set(accountName);
		new VoodooControl("a", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();

		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Verify that there is no records present in Calls,Meetings,Tasks,Notes and Emails subpanel on Leads Records view.
		sugar().leads.recordView.setRelatedSubpanelFilter(callsPlural);
		Assert.assertTrue("Records Present in Calls subpanel", callsSubpanel.countRows() == 0);
		sugar().leads.recordView.setRelatedSubpanelFilter(meetingsPlural);
		Assert.assertTrue("Records Present in Meetings subpanel", meetingsSubpanel.countRows() == 0);
		sugar().leads.recordView.setRelatedSubpanelFilter(tasksPlural);
		Assert.assertTrue("Records Present in Tasks subpanel", taskSubpanel.countRows() == 0);
		sugar().leads.recordView.setRelatedSubpanelFilter(notesPlural);
		Assert.assertTrue("Records Present in Notes subpanel", noteSubpanel.countRows() == 0);
		sugar().leads.recordView.setRelatedSubpanelFilter(emailsPlural);
		Assert.assertTrue("Records Present in Emails subpanel", emailsSubpanel.countRows() == 0);
		sugar().leads.recordView.setRelatedSubpanelFilter(customData.get("filterValue"));

		// Navigate contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Asserting the Account Name on contact record view
		sugar().contacts.recordView.getDetailField("relAccountName").assertEquals(accountName, true);

		// Contacts subpanel controls
		StandardSubpanel contactCallsSubpanel = (StandardSubpanel) sugar().contacts.recordView.subpanels.get(callsPlural);
		StandardSubpanel contactMeetSubpanel = (StandardSubpanel) sugar().contacts.recordView.subpanels.get(meetingsPlural);
		StandardSubpanel contactTaskSubpanel = (StandardSubpanel) sugar().contacts.recordView.subpanels.get(tasksPlural);
		StandardSubpanel contactNoteSubpanel = (StandardSubpanel) sugar().contacts.recordView.subpanels.get(notesPlural);
		StandardSubpanel contactEmailSubpanel = (StandardSubpanel) sugar().contacts.recordView.subpanels.get(emailsPlural);
		String leadsPlural = sugar().leads.moduleNamePlural;
		StandardSubpanel contactLeadSubpanel = (StandardSubpanel) sugar().contacts.recordView.subpanels.get(leadsPlural);

		// Verify all the records from lead's subpanels are moved to contact Records view.
		sugar().contacts.recordView.setRelatedSubpanelFilter(callsPlural);
		Assert.assertTrue("Records not moved to Calls subpanel of Contact", contactCallsSubpanel.countRows() == 1);
		sugar().contacts.recordView.setRelatedSubpanelFilter(meetingsPlural);
		Assert.assertTrue("Records not moved to Meetings subpanel of Contact", contactMeetSubpanel.countRows() == 1);
		sugar().contacts.recordView.setRelatedSubpanelFilter(tasksPlural);
		Assert.assertTrue("Records not moved to Tasks subpanel of Contact", contactTaskSubpanel.countRows() == 1);
		sugar().contacts.recordView.setRelatedSubpanelFilter(notesPlural);
		Assert.assertTrue("Records not moved to Notes subpanel of Contact", contactNoteSubpanel.countRows() == 1);
		sugar().contacts.recordView.setRelatedSubpanelFilter(leadsPlural);
		contactLeadSubpanel.expandSubpanel();
		Assert.assertTrue("Converted Lead record is not present in lead subpanel", contactLeadSubpanel.countRows() == 1);
		sugar().contacts.recordView.setRelatedSubpanelFilter(emailsPlural);
		Assert.assertTrue("Records not moved to Emails subpanel of Contact", contactEmailSubpanel.countRows() == 2);
		sugar().contacts.recordView.setRelatedSubpanelFilter(customData.get("filterValue"));

		// Navigate account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Accounts subpanel controls
		StandardSubpanel accountCallsSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(callsPlural);
		StandardSubpanel accountMeetSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(meetingsPlural);
		StandardSubpanel accountTaskSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(tasksPlural);
		StandardSubpanel accountNoteSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(notesPlural);
		StandardSubpanel accountEmailSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(emailsPlural);
		StandardSubpanel accountLeadSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(leadsPlural);
		String contactsPlural = sugar().contacts.moduleNamePlural;
		StandardSubpanel accountContactSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(contactsPlural);

		// Verify no records present in Calls,Meetings,Tasks and Notes subpanel on accounts record view
		sugar().accounts.recordView.setRelatedSubpanelFilter(callsPlural);
		Assert.assertTrue("Records present in Calls subpanel of account", accountCallsSubpanel.countRows() == 0);
		sugar().accounts.recordView.setRelatedSubpanelFilter(meetingsPlural);
		Assert.assertTrue("Records present in Meetings subpanel of account", accountMeetSubpanel.countRows() == 0);
		sugar().accounts.recordView.setRelatedSubpanelFilter(tasksPlural);
		Assert.assertTrue("Records present in Tasks subpanel of account", accountTaskSubpanel.countRows() == 0);
		sugar().accounts.recordView.setRelatedSubpanelFilter(notesPlural);
		Assert.assertTrue("Records present in Notes subpanel of account", accountNoteSubpanel.countRows() == 0);

		// Verify records present in Leads,Contact and Email subpanel on accounts record view
		sugar().accounts.recordView.setRelatedSubpanelFilter(leadsPlural);
		Assert.assertTrue("No record in Leads subpanel of account", accountLeadSubpanel.countRows() == 1);
		sugar().accounts.recordView.setRelatedSubpanelFilter(contactsPlural);
		accountContactSubpanel.expandSubpanel();
		Assert.assertTrue("No record in Contact subpanel of account", accountContactSubpanel.countRows() == 1);
		sugar().accounts.recordView.setRelatedSubpanelFilter(emailsPlural);
		Assert.assertTrue("No record in Email subpanel of account", accountEmailSubpanel.countRows() == 2);

		// Verify on Email detail view Contact record appears in contact subpanel
		// TODO: VOOD-972 - Not working methods in BWC subpanels: verify, assertContains, create
		new VoodooControl("a", "css", ".single .fld_name a").click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-792 - Need lib support to handle or access email records
		new VoodooControl("a", "css", "#list_subpanel_contacts tr.oddListRowS1 a").assertContains(leadRec.getRecordIdentifier(), true);

		// Verify on Email detail view no record appears in Leads and Email Contact subpanel
		String nodata = customData.get("noData");
		new VoodooControl("div", "id", "list_subpanel_leads").assertContains(nodata, true);
		new VoodooControl("div", "id", "list_subpanel_contacts_snip").assertContains(nodata, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}