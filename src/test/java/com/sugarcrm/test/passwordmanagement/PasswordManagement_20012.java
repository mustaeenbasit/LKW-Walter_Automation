package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20012 extends SugarTest {
	VoodooControl passwordManagementCtrl, templateName, templateSaveCtrl, cancelTemplateCreation;

	public void setup() throws Exception {
		sugar().login();
		
		// Go to Admin
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		passwordManagementCtrl = sugar().admin.adminTools.getControl("passwordManagement");
		passwordManagementCtrl.click();

		// TODO: VOOD-993
		// Create a Template
		new VoodooControl("input", "css", "#emailTemplatesId tr:nth-child(2) td:nth-child(2) input:nth-child(2)").click();
		VoodooUtils.focusWindow(1);
		templateName = new VoodooControl("input", "css", "[name='name']");
		templateName.set(testName);
		templateSaveCtrl = new VoodooControl("input", "id", "SAVE");
		templateSaveCtrl.click();
		VoodooUtils.focusWindow(0);
		
		// Cancel updation of PasswordManagement settings 
		VoodooUtils.focusFrame("bwc-frame");
		cancelTemplateCreation = new VoodooControl("input", "css", "input[title='Cancel']");
		cancelTemplateCreation.click();
	}

	/**
	 * Edit Email Templates
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20012_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Admin
		passwordManagementCtrl.click();
		
		// Select created Template
		VoodooControl generatePasswordTml = new VoodooControl("select", "id", "generatepasswordtmpl");
		generatePasswordTml.click();
		VoodooControl selectGenratedPassword = new VoodooControl("option","css","#generatepasswordtmpl option:nth-child(3)");
		selectGenratedPassword.click();
		
		// Click on edit button
		new VoodooControl("input", "css", "#emailTemplatesId tr:nth-child(2) td:nth-child(2) input#edit_generatepasswordtmpl").click();
		VoodooUtils.focusWindow(1);
		
		// Verify the selected template can be edited
		templateName.assertExists(true);
		templateName.set(testName+"_1");
		templateSaveCtrl.click();
		VoodooUtils.focusWindow(0);
		
		// Cancel updation of PasswordManagement settings 
		VoodooUtils.focusFrame("bwc-frame");
		cancelTemplateCreation.click();
		VoodooUtils.focusDefault();
		
		// Go to EmailTamplete listView
		sugar().emails.navToListView();
		sugar().navbar.selectMenuItem(sugar().emails, "viewTemplates");
		sugar().emails.listView.basicSearch(testName);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that the Email Templates List to make sure the edited template is updated.
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) b a").assertContains(testName+"_1", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}