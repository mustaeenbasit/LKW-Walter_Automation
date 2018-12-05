package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_27178 extends SugarTest {
	FieldSet activityStreamData = new FieldSet();
	OpportunityRecord myOpportunity;
	AccountRecord account1, account2;

	public void setup() throws Exception {
		activityStreamData = testData.get(testName).get(0);

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Create two Accounts(Created from UI so that message should post in activity dtream
		account1 = (AccountRecord) sugar().accounts.create(); // Create an Account record with default csv name
		FieldSet accountName = new FieldSet();
		accountName.put("name", activityStreamData.get("name"));
		account2 = (AccountRecord) sugar().accounts.create(accountName); // Create an Account record with custom name

		// Create an Opportunity record from UI so that 
		myOpportunity = (OpportunityRecord) sugar().opportunities.create(); // It links the default account record created from default csv
	}

	/**
	 * Verify that "My Activity Stream" Dashlet is saved in Home Dashboard
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_27178_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Lead Record
		sugar().leads.create(); // Create lead record from UI so that a message should post in activity stream

		// Edit an Opportunity record. (e.g Change the Account Name for the Opportunity.)
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(activityStreamData.get("name"));
		sugar().opportunities.recordView.save();

		// Verify that it will Post a message in Home -> Activity Stream.
		sugar().navbar.selectMenuItem(sugar().home, "activityStream");
		VoodooUtils.waitForReady();
		ActivityStream stream = new ActivityStream();
		stream.assertContains(activityStreamData.get("assertMessageUnlinked") + " " + myOpportunity.getRecordIdentifier() + " " + activityStreamData.get("assertMessageTo") + " " + account1.getRecordIdentifier(), true);
		stream.assertContains(activityStreamData.get("assertMessageLinked") + " " + myOpportunity.getRecordIdentifier() + " " + activityStreamData.get("assertMessageTo") + " " + account2.getRecordIdentifier(), true);

		// Navigate to Home cube
		sugar().navbar.clickModuleDropdown(sugar().home);
		
		// TODO VOOD-953 - need defined control of dashboard menu items under home tab
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	
		VoodooUtils.waitForReady();

		// In RHS, click on Create button
		sugar().dashboard.clickCreate();
		sugar().dashboard.getControl("title").set(testName);
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("input", "css", ".layout_Home .inline-drawer-header .search").set(activityStreamData.get("myActivityStream"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();
		VoodooControl morePostsCtrl = new VoodooControl("button", "css", ".block-footer .btn");
		morePostsCtrl.click(); // Click on More Posts... button

		// Verify that the description of the Activity Stream dashlet that appears on the Dashlet is "View a list of activities performed on records and create and post comments."
		stream.assertContains(activityStreamData.get("assertMessageUnlinked") + " " + myOpportunity.getRecordIdentifier() + " " + activityStreamData.get("assertMessageTo") + " " + account1.getRecordIdentifier(), true);
		stream.assertContains(activityStreamData.get("assertMessageLinked") + " " + myOpportunity.getRecordIdentifier() + " " + activityStreamData.get("assertMessageTo") + " " + account2.getRecordIdentifier(), true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + myOpportunity.getRecordIdentifier() + " " + sugar().opportunities.moduleNameSingular, true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + account1.getRecordIdentifier() + " " + sugar().accounts.moduleNameSingular, true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + account2.getRecordIdentifier() + " " + sugar().accounts.moduleNameSingular, true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + sugar().leads.getDefaultData().get("fullName") + " " + sugar().leads.moduleNameSingular, true);

		// Save it
		sugar().home.dashboard.save();

		// Verify that "My Activity Stream" dashlet is added in created DB.
		sugar().dashboard.getControl("dashboardTitle").assertEquals(testName, true);
		sugar().dashboard.getControl("firstDashlet").assertVisible(true);

		// Assert dashlet title 
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("h4", "css", ".dashlet-title").assertEquals(activityStreamData.get("myActivityStream"), true);
		morePostsCtrl.click(); // Click on More Post... button

		// Verify that the saved dashlet shows all activity in Lead, Opportunity and post messages
		stream.assertContains(activityStreamData.get("assertMessageUnlinked") + " " + myOpportunity.getRecordIdentifier() + " " + activityStreamData.get("assertMessageTo") + " " + account1.getRecordIdentifier(), true);
		stream.assertContains(activityStreamData.get("assertMessageLinked") + " " + myOpportunity.getRecordIdentifier() + " " + activityStreamData.get("assertMessageTo") + " " + account2.getRecordIdentifier(), true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + myOpportunity.getRecordIdentifier() + " " + sugar().opportunities.moduleNameSingular, true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + account1.getRecordIdentifier() + " " + sugar().accounts.moduleNameSingular, true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + account2.getRecordIdentifier() + " " + sugar().accounts.moduleNameSingular, true);
		stream.assertContains(activityStreamData.get("assertMessageCreated") + " " + sugar().leads.getDefaultData().get("fullName") + " " + sugar().leads.moduleNameSingular, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 