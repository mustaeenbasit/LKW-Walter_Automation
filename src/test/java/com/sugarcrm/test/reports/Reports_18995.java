package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Reports_18995 extends SugarTest {
	VoodooControl reportModuleCtrl;
	FieldSet roleRecord, userData,myData;
	UserRecord myUser;
	
	public void setup() throws Exception {
		roleRecord = testData.get(testName+"_role").get(0);
		userData = testData.get(testName+"_user").get(0);
		myData = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();
		myUser = (UserRecord) sugar().users.create(userData);
	}

	/**
	 * Verify that user is Removed from all Roles when the user with Regular Permissions is changing to a System Administrator.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_18995_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Role with View permissions of Owner for a module (Leads)
		AdminModule.createRole(roleRecord);
		sugar().alerts.waitForLoadingExpiration(30000); // Sugar alerts... Required due to createRole method have no wait after save
		
		// Now on the Access matrix set a "View" permission to owner for Leads module
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",	"td#ACLEditView_Access_Leads_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Leads_view div select").set("Owner");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign newly created user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		
		// TODO VOOD-858
		sugar().alerts.waitForLoadingExpiration(30000); // Sugar alerts... Required due to assignUserToRole method have no wait after save

		// Edit user, User Type change from Regular User to System Administrator User.
		myUser.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("select", "id", "UserType").set(myData.get("user_type"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		
		// Log out of Sugar as Admin and log in as newly created user.
		sugar().logout();
		
		sugar().login(myUser);
		
		// Create Custom Report in Leads module
		// TODO: VOOD-822
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa.fa-caret-down");
		VoodooControl createRowsColumnReportCtrl = new VoodooControl("td", "css", "#report_type_div tr:nth-child(2) td:nth-child(1) tr:nth-child(1) td:nth-child(1)");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");
		
		sugar().navbar.navToModule("Reports");
		VoodooUtils.focusDefault();
		reportModuleCtrl.waitForVisible();
		reportModuleCtrl.click();
		new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");
		createRowsColumnReportCtrl.waitForVisible();
		createRowsColumnReportCtrl.click();
		new VoodooControl("table", "id", "Leads").click();
		
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Leads_full_name").click();
		nextBtnCtrl.click();
		
		reportNameCtrl.set(myData.get("report_name"));
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify, Report is displayed much like the Listview does when selecting the module.
		new VoodooControl("a", "css", ".listViewBody tr:nth-child(3) a").assertContains(sugar().leads.getDefaultData().get("fullName"),true);
		VoodooUtils.focusDefault();
		
		// Log out of newly created user and log in Admin.
		sugar().logout();
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}