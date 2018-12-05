package com.sugarcrm.test.admin;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20226 extends SugarTest {
	VoodooControl stackTraceError, systemSettingSaveBtnCtrl;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Warning: "unlink(): No such file or directory" when Display stack trace of errors is enabled 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20226_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> System Settings
		FieldSet sysSetting = new FieldSet();
		sysSetting.put("stackTraceErrors", "true");
		sugar().admin.setSystemSettings(sysSetting);

		// Go to Admin -> System Settings again
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that checkbox remains checked
		stackTraceError = sugar().admin.systemSettings.getControl("stackTraceErrors");
		Boolean checkStatus = stackTraceError.isChecked();
		Assert.assertTrue("stackTraceError checkbox still not checked", checkStatus);
		
		// TODO: VOOD-872 -Lib support in view log window
		// Check out the log in Admin->System Settings
		new VoodooControl("a", "xpath", "//a[contains(.,'View Log')]").click(); // Click on 'View Log' link

		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Now we are on the new Tab
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusWindow(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that there are no errors and warnings in the sugarcrm.log file
		new VoodooControl("div", "id", "content").assertContains("FATAL", false);
		VoodooUtils.focusDefault();
		// Close this Tab
		VoodooUtils.closeWindow();
		
		// Try to create/edit/delete some different records (Accounts, Contacts, Leads etc.)
		// Create Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.save();
		
		// Verify that there are no errors and warnings on the screen after each action
		sugar().alerts.getWarning().assertExists(false);
		
		// Delete created record
		sugar().accounts.listView.deleteRecord(1);
		sugar().accounts.listView.confirmDelete();
		
		// Verify that there are no errors and warnings on the screen after each action
		sugar().alerts.getWarning().assertExists(false);
		
		// Create Contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.save();
		
		// Verify that there are no errors and warnings on the screen after each action
		sugar().alerts.getWarning().assertExists(false);
		
		// Delete created record
		sugar().contacts.listView.deleteRecord(1);
		sugar().contacts.listView.confirmDelete();
		
		// Verify that there are no errors and warnings on the screen after each action
		sugar().alerts.getWarning().assertExists(false);
		
		// Create Lead record
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		sugar().leads.createDrawer.save();
		
		// Verify that there are no errors and warnings on the screen after each action
		sugar().alerts.getWarning().assertExists(false);
		
		// Delete created record
		sugar().leads.listView.deleteRecord(1);
		sugar().leads.listView.confirmDelete();
		
		// Verify that there are no errors and warnings on the screen after each action
		sugar().alerts.getWarning().assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}