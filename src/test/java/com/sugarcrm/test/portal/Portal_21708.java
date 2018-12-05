package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21708 extends PortalTest {
	ContactRecord myCon;
	FieldSet loginFieldSet = new FieldSet();
	DataSource ds = new DataSource();
	AccountRecord accountRecord;

	public void setup() throws Exception {
		ds = testData.get(testName);
		loginFieldSet = testData.get("env_portal_contact_setup").get(0);

		accountRecord = (AccountRecord) sugar().accounts.api.create();
		myCon = (ContactRecord) sugar().contacts.api.create();
		sugar().login();

		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myCon);

		sugar().admin.portalSetup.enablePortal();

		myCon.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(accountRecord.getRecordIdentifier());
		sugar().alerts.confirmAllAlerts();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("portalName").set(loginFieldSet.get("portalName"));
		sugar().contacts.recordView.getEditField("password").set(loginFieldSet.get("password"));
		sugar().contacts.recordView.getEditField("confirmPassword").set(loginFieldSet.get("confirmPassword"));
		sugar().contacts.recordView.getEditField("checkPortalActive").set(loginFieldSet.get("checkPortalActive"));
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().logout();
	}

	/**
	 * Verify portal user creates a new case and verify Sugar user edits in Sugar side.
	 *
	 * @throws Exception
	 */
	@Test
	public void Portal_21708_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", loginFieldSet.get("portalName"));
		portalUser.put("password", loginFieldSet.get("password"));
		portal().loginScreen.navigateToPortal();

		// login to portal
		portal().login(portalUser);

		// Navigate to case module 
		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();

		// Create a case record 
		portal().cases.listView.create();
		sugar().cases.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar().cases.createDrawer.getEditField("description").set(ds.get(0).get("description"));
		sugar().cases.createDrawer.getEditField("status").set(ds.get(0).get("status"));
		sugar().cases.createDrawer.getEditField("priority").set(ds.get(0).get("priority"));
		portal().cases.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		portal().logout();

		// Login to Sugar server side
		sugar().loginScreen.navigateToSugar();
		sugar().login();

		// Go to Cases module in server side. 
		sugar().cases.navToListView();

		// Verify that Case created in portal is available here 
		sugar().cases.listView.verifyField(1, "name", ds.get(0).get("name"));
		sugar().cases.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Assert the record on record view
		sugar().cases.recordView.getDetailField("status").assertEquals(ds.get(0).get("status"), true);
		sugar().cases.recordView.getDetailField("priority").assertEquals(ds.get(0).get("priority"), true);

		// Edit the case record i.e. "Name", "Priority", "Status"
		sugar().cases.recordView.edit();
		sugar().cases.recordView.getEditField("name").set(ds.get(1).get("name"));
		sugar().cases.recordView.getEditField("status").set(ds.get(1).get("status"));
		sugar().cases.recordView.getEditField("priority").set(ds.get(1).get("priority"));

		// Click on "Save" button.
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().logout();

		//  log in to Portal again with same user.
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		//  Navigate to Cases module
		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();

		// Assert that "Change made in Case recored at server side  are showing correctly in the portal side"
		portal().cases.listView.verifyField(1, "name", ds.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}