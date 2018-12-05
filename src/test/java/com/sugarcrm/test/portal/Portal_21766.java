package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21766 extends PortalTest {
	public void setup() throws Exception {
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);

		// Create an Account record
		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		// Create contact for portal access
		ContactRecord myCon = (ContactRecord)sugar().contacts.api.create(portalContactData);

		// Setup Portal Access
		sugar().login();

		// Add Account to Contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();

		// Enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable portal
		sugar().admin.portalSetup.enablePortal();

		// Logout
		sugar().logout();

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);
	}

	/**
	 * Verify of viewing all fields in bug detail view as a portal user
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21766_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1116. Need Lib support for Cases and Bugs module controls and functions in Portal
		// Create bug record in Portal
		FieldSet bugData = new FieldSet();
		bugData = portal().bugs.getDefaultData();

		portal().navbar.navToModule(portal().bugs.moduleNamePlural);
		portal().bugs.listView.create();
		portal().bugs.portalCreateDrawer.setFields(bugData);
		portal().bugs.portalCreateDrawer.save();
		portal().alerts.waitForLoadingExpiration();
		portal().alerts.getAlert().closeAlert();
		BugRecord portalbug = (BugRecord) Class.forName(portal().bugs.recordClassName).getConstructor(FieldSet.class).newInstance(bugData);

		// Bug record created in portal(). Now time to verify it.
		// navToPortalRecord
		portal().bugs.navToPortalListView();
		new VoodooControl("input", "css", "div.dataTables_filter input").set(portalbug.getRecordIdentifier());
		portal().alerts.waitForLoadingExpiration();
		new VoodooControl("a", "css", "div.list-view tbody tr:nth-of-type(1) td:nth-of-type(2) a").click();
		portal().alerts.waitForLoadingExpiration();

		// Verify
		for(String controlName : bugData.keySet()) {
			if(controlName != "name" && 
					controlName != "description" && 
					controlName != "priority" &&
					controlName != "type" &&
					controlName != "status" &&
					controlName != "source" &&
					controlName != "category" &&
					controlName != "work_log" ) {
				continue;
			}
			if(bugData.get(controlName) != null) {
				if(portal().bugs.recordView.getDetailField(controlName) == null) {
					continue;
				}
				String toVerify = bugData.get(controlName);
				portal().bugs.recordView.getDetailField(controlName).assertEquals(toVerify, true);
			}
		}

		// TODO: VOOD-1116
		// Verify Number 
		new VoodooControl("span", "css", ".fld_bug_number.detail").assertExists(true);

		// Verify Resolution
		new VoodooControl("span", "css", ".fld_resolution.detail").assertExists(true);

		// Verify Date Created 
		new VoodooControl("span", "css", ".fld_date_entered.detail").assertExists(true);
		new VoodooControl("span", "css", ".fld_created_by_name.detail").assertExists(true);

		// Verify Date Modified
		new VoodooControl("span", "css", ".fld_date_modified.detail").assertExists(true);
		new VoodooControl("span", "css", ".fld_modified_by_name.detail").assertExists(true);

		// Verify Assigned to
		new VoodooControl("span", "css", ".fld_assigned_user_name.detail").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}