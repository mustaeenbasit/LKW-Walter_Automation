package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20011 extends SugarTest {
	VoodooControl passwordManagementCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Create Email Templates
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20011_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		passwordManagementCtrl = sugar().admin.adminTools.getControl("passwordManagement");
		passwordManagementCtrl.click();

		// TODO: VOOD-993
		// Create a Template
		new VoodooControl("input", "css", "#emailTemplatesId tr:nth-child(2) td:nth-child(2) input:nth-child(2)").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "[name='name']").set(testName);
		new VoodooControl("input", "id", "SAVE").click();
		VoodooUtils.focusWindow(0);
		
		// Cancel updation of PasswordManagement settings 
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "input[title='Cancel']").click();
		VoodooUtils.focusDefault();
		
		// Go to EmailTamplete listView
		sugar().emails.navToListView();
		sugar().navbar.selectMenuItem(sugar().emails, "viewTemplates");
		sugar().emails.listView.basicSearch(testName);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify the new template be created successfully
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) b a").assertContains(testName, true);
		VoodooUtils.focusDefault();
		
		// Go to password Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		passwordManagementCtrl.click();
		
		// Verify the Email Template be the selected one.
		new VoodooControl("select", "id", "generatepasswordtmpl").assertContains(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}