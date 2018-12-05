package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Mohd. Shariq <mshariq@sugarcrm.com>
 */
public class Opportunities_19320 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// opportunity record -> assignedTo -> qauser
		FieldSet oppRecord = new FieldSet();
		oppRecord.put("name", testName);
		oppRecord.put("relAssignedTo", roleRecord.get("userName"));
		sugar().opportunities.create(oppRecord);

		// Create role => Opportunities -> Delete=None 
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "#ACLEditView_Access_Opportunities_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Opportunities_delete div select").set(roleRecord.get("roleName"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();

		// Admin -> PDF Manager
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("pdfManager").click();
		sugar().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1158
		// Set-up Pdf template for respective module
		new VoodooControl("a", "css", "#header div.module-list  ul  li.dropdown.active button  i").click(); 	
		new VoodooControl("a", "css", "li[data-module='PdfManager'] a[data-navbar-menu-item='LNK_NEW_RECORD']").click();		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(testName);
		new VoodooControl( "select", "id", "base_module").set(sugar().opportunities.moduleNamePlural);
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.focusDefault();		
		sugar().alerts.waitForLoadingExpiration();

		sugar().logout();
	}

	/**
	 * Verify action links in record view when Delete=None in Opportunities
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_19320_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Navigate to Opportunities module.
		sugar().opportunities.navToListView();

		// Select one record that is assigned to qauser
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.showMore();
		// open primary button drop down in record view.
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		VoodooControl editButton = sugar().opportunities.recordView.getControl("editButton");
		VoodooControl copyButton = sugar().opportunities.recordView.getControl("copyButton");

		// TODO: VOOD-738
		VoodooControl share = new VoodooControl("a", "css", ".detail.fld_share a");
		VoodooControl downloadPDF = new VoodooControl("a", "css", ".detail.fld_download-pdf a");
		VoodooControl emailPDF = new VoodooControl("a", "css", ".detail.fld_email-pdf a");
		VoodooControl findDuplicate = new VoodooControl("a", "css", ".detail.fld_find_duplicates_button a");
		VoodooControl historicalSummary = new VoodooControl("a", "css", ".detail.fld_historical_summary_button a");
		VoodooControl viewChangeLog = new VoodooControl("a", "css", ".detail.fld_audit_button a");
		// Verify that record is assign to qauser
		sugar().opportunities.recordView.getDetailField("relAssignedTo").assertContains(roleRecord.get("userName"), true);
		// Verify that all action link available i.e. Edit, Share,Download PDF, Email PDF, Find Duplicate,
		// Historical Summary, View Change Log, Copy 
		editButton.assertVisible(true);
		copyButton.assertVisible(true);
		share.assertVisible(true);
		downloadPDF.assertVisible(true);
		emailPDF.assertVisible(true);
		findDuplicate.assertVisible(true);
		historicalSummary.assertVisible(true);
		viewChangeLog.assertVisible(true);

		// navigate to  next record that is not assigned to qauser
		sugar().opportunities.recordView.gotoNextRecord();
		sugar().opportunities.recordView.showMore();
		// Verify that record is not assign to qauser
		sugar().opportunities.recordView.getDetailField("relAssignedTo").assertContains(roleRecord.get("userName"), false);
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		// Verify that all action link available i.e. Edit, Share,Download PDF, Email PDF, Find Duplicate,
		// Historical Summary, View Change Log, Copy 
		editButton.assertVisible(true);
		copyButton.assertVisible(true);
		share.assertVisible(true);
		downloadPDF.assertVisible(true);
		emailPDF.assertVisible(true);
		findDuplicate.assertVisible(true);
		historicalSummary.assertVisible(true);
		viewChangeLog.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}