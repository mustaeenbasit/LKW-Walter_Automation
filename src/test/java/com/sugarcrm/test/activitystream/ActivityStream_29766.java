package com.sugarcrm.test.activitystream;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class ActivityStream_29766 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();

		// Go to Admin -> System Settings "Listview items per page:" to 2.
		FieldSet customSettings = testData.get(testName).get(0); 
		sugar().admin.setSystemSettings(customSettings);

		// Perform some random activities like create account, contact, lead.
		// Create Account
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.save();

		// Create Contact
		sugar().navbar.selectMenuItem(sugar().contacts, "createContact");
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.save();

		// Create Lead
		sugar().navbar.selectMenuItem(sugar().leads, "createLead");
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		sugar().leads.createDrawer.save();
	}

	/**
	 * Verify that irrelevant activity should not display in preview section of any sidecar module.
	 * 
	 */
	@Test
	public void ActivityStream_29766_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enable Bugs Module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Navigate to bug module(or any side car module) and create one record.
		sugar().navbar.selectMenuItem(sugar().bugs, "createBug");
		String bugSubject = sugar().bugs.getDefaultData().get("name");
		sugar().bugs.createDrawer.getEditField("name").set(bugSubject);
		sugar().bugs.createDrawer.save();

		// Navigate to it's record view.
		sugar().bugs.listView.clickRecord(1);

		// Create 1 record from calls subpanel
		StandardSubpanel callsSubpanel = sugar().bugs.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.addRecord();
		String callSubject = sugar().calls.getDefaultData().get("name");
		sugar().calls.createDrawer.getEditField("name").set(callSubject);
		sugar().calls.createDrawer.save();

		// Create 1 record from Meetings Subpanel
		StandardSubpanel meetingsSubpanel = sugar().bugs.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.addRecord();
		String meetingSubject = sugar().meetings.getDefaultData().get("name");
		sugar().meetings.createDrawer.getEditField("name").set(meetingSubject);
		sugar().meetings.createDrawer.save();

		// Create 1 record from cases Subpanel
		StandardSubpanel casesSubpanel = sugar().bugs.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubpanel.addRecord();
		String caseSubject = sugar().cases.getDefaultData().get("name");
		sugar().cases.createDrawer.getEditField("name").set(caseSubject);
		sugar().cases.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().cases.createDrawer.save();

		// Click on preview link of record i.e. from call
		callsSubpanel.clickPreview(1);

		// Click on below the Activity stream "more post" link on preview pane.
		// TODO: VOOD-1944
		VoodooControl morePostsCtrl = new VoodooControl("button", "css", ".more.padded");
		morePostsCtrl.click();

		// Verify It should display Activities related to its particular record only.
		// i.e it is displaying result related to Call, Bug & nothing related to Account/Contact/Lead created in set up
		VoodooControl activityStreamResultsCtrl = new VoodooControl("ul", "css", ".activitystream-list.results");

		// Verifying Call
		activityStreamResultsCtrl.assertContains(callSubject, true);

		// Verifying Bug
		activityStreamResultsCtrl.assertContains(bugSubject, true);

		// Verifying Account/Contact/Lead created in set up is not showing up here.
		activityStreamResultsCtrl.assertContains(testName, false);

		// Click on preview link of record i.e. from Meeting
		meetingsSubpanel.clickPreview(1);

		// Click on below the Activity stream "more post" link on preview pane.
		morePostsCtrl.click();

		// Verify It should display Activities related to its particular record only.
		// i.e. it is displaying result related to Meeting, Bug & nothing related to Account/Contact/Lead created in set up

		// Verifying Meeting
		activityStreamResultsCtrl.assertContains(meetingSubject, true);

		// Verifying Bug
		activityStreamResultsCtrl.assertContains(bugSubject, true);

		// Verifying Account/Contact/Lead created in set up is not showing up here.
		activityStreamResultsCtrl.assertContains(testName, false);

		// Click on preview link of record i.e. from Cases
		casesSubpanel.clickPreview(1);

		// Click on below the Activity stream "more post" link on preview pane.
		morePostsCtrl.click();

		// Verify It should display Activities related to its particular record only.
		// i.e. it is displaying result related to Case, Bug & nothing related to Account/Contact/Lead created in set up

		// Verifying Cases
		activityStreamResultsCtrl.assertContains(caseSubject, true);

		// Verifying Bug
		activityStreamResultsCtrl.assertContains(bugSubject, true);

		// Verifying Account/Contact/Lead created in set up is not showing up here.
		activityStreamResultsCtrl.assertContains(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}