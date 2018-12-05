package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21765 extends PortalTest {
	CaseRecord portalCase;
	AccountRecord myAcc;
	FieldSet  portalContactData = new FieldSet();
	
	public void setup() throws Exception {
		portalContactData = testData.get("env_portal_contact_setup").get(0);
		
		// Setup Portal Access
		sugar().login();
		sugar().admin.portalSetup.enablePortal();

		myAcc = (AccountRecord)sugar().accounts.api.create();

		// Create contact for portal access
		ContactRecord myCon = (ContactRecord)sugar().contacts.api.create(portalContactData);

		// Add Account to Contact
		myCon.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().logout();
		
		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);
		new VoodooControl("div", "css", ".dashboard .thumbnail").assertVisible(true);
	}
	
	/**
	 * Verify of viewing all fields in case detail view as a portal user
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21765_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-1116. Need Lib support for Case module controls and functions in portal().
		// Create Case record in Portal
		FieldSet caseData = new FieldSet();
		caseData = portal().cases.getDefaultData();
		
		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		portal().cases.listView.create();
		sugar().cases.createDrawer.getEditField("name").set(caseData.get("name"));
		sugar().cases.createDrawer.getEditField("description").set(caseData.get("description"));
		sugar().cases.createDrawer.getEditField("priority").set(caseData.get("priority"));
		sugar().cases.createDrawer.getEditField("status").set(caseData.get("status"));
		// Type requires a special treatment
		new VoodooControl("a", "css", "span.fld_type.edit a").click();
		new VoodooControl("li", "xpath", "//li[contains(.,'Administration')][@role='presentation']").click();
		
		portal().cases.portalCreateDrawer.save();
		VoodooUtils.waitForReady();
		portal().alerts.waitForLoadingExpiration();
		portal().alerts.getAlert().closeAlert();
		
		portalCase = (CaseRecord) Class.forName(portal().cases.recordClassName).getConstructor(FieldSet.class).newInstance(caseData);

		// Case record created in portal(). Now time to verify it.
		// navToPortalRecord
		portal().cases.navToPortalListView();
		new VoodooControl("input", "css", "div.dataTables_filter input").set(portalCase.getRecordIdentifier());
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div.list-view tbody tr:nth-of-type(1) td:nth-of-type(2) a").click();
		VoodooUtils.waitForAlertExpiration();

		// Verify
		for(String controlName : caseData.keySet()) {
			if(controlName != "name" && 
			   controlName != "description" && 
			   controlName != "priority" &&
			   controlName != "status") {
				continue;
			}
				if(caseData.get(controlName) != null) {
					if(portal().cases.recordView.getDetailField(controlName) == null) {
						continue;
				}
				String toVerify = caseData.get(controlName);
				portal().cases.recordView.getDetailField(controlName).assertEquals(toVerify, true);
			}
		}
		
		// Verify Number 
		new VoodooControl("span", "css", ".fld_case_number.detail").assertExists(true);
		
		// Verify Type
		new VoodooControl("span", "css", ".fld_type.detail").assertExists(true);
		
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