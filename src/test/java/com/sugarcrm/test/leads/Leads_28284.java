package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_28284 extends SugarTest {
	FieldSet leadConvertData = new FieldSet();
	VoodooControl moduleCtrl, layoutCtrl, recordviewBtnCtrl, historyDefault, saveBtnCtrl;

	public void setup() throws Exception {
		leadConvertData = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Define Studio controls
		// TODO: VOOD-542, VOOD-1506
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordviewBtnCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl accountNameCtrl = new VoodooControl("div", "css", "#panels div[data-name='account_name']");
		VoodooControl fillerCtrl = new VoodooControl("div", "css", "#availablefields .le_field.special");
		historyDefault = new VoodooControl("input", "id", "historyDefault");
		saveBtnCtrl = new VoodooControl("td", "id", "publishBtn");

		// Go to Leads -> Layouts -> Record View
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordviewBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Remove "Account Name"
		accountNameCtrl.dragNDrop(fillerCtrl);
		VoodooUtils.waitForReady();

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Account Name appear after Lead convert
	 * @throws Exception
	 */
	@Test
	public void Leads_28284_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout from admin user and logged in as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to lead module
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Convert a Lead.
		// TODO: VOOD-585	
		sugar().leads.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();
		// Associate Account
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").set(leadConvertData.get("accountName"));
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();
		// Associate Opportunity. 
		new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input").set(leadConvertData.get("oppName"));
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();
		// Save and Convert the Lead. 
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-585
		VoodooControl associatedContactCtrl = new VoodooControl("tr", "css", ".converted-results tr:nth-child(1)");
		VoodooControl associatedAccountCtrl = new VoodooControl("tr", "css", ".converted-results tr:nth-child(2)");
		VoodooControl associatedOpportunityCtrl = new VoodooControl("tr", "css", ".converted-results tr:nth-child(3)");
		VoodooControl convertedResultsPreviewCtrl = new VoodooControl("a", "css", ".preview-list-item");
		VoodooControl accountNameCtrl = new VoodooControl("a", "css", ".preview-pane .fld_account_name.detail a");
		VoodooControl accountRecordNameCtrl = new VoodooControl("a", "css", ".preview-pane .fld_name a");

		// Verify the converted Lead, at Contact section, Contact Name appears.
		associatedContactCtrl.assertContains(sugar().leads.getDefaultData().get("firstName") + " " + sugar().leads.getDefaultData().get("lastName"), true);

		// Verify the converted Lead, at Accounts section, Account Name appears.
		associatedAccountCtrl.assertContains(leadConvertData.get("accountName"), true);

		// Verify the converted Lead, at Opportunity section, Opportunity Name appears.
		associatedOpportunityCtrl.assertContains(leadConvertData.get("oppName"), true);

		// Click on preview of the Contact, the Account Name also appears.
		new VoodooControl("a", "css", associatedContactCtrl.getHookString() + " " + convertedResultsPreviewCtrl.getHookString()).click();
		VoodooUtils.waitForReady();
		accountNameCtrl.assertEquals(leadConvertData.get("accountName"), true);

		// Preview of Account, the Account Name appears.
		new VoodooControl("a", "css", associatedAccountCtrl.getHookString() + " " + convertedResultsPreviewCtrl.getHookString()).click();
		VoodooUtils.waitForReady();
		accountRecordNameCtrl.assertEquals(leadConvertData.get("accountName"), true);

		// Preview Opportunity, Account Name also appears.
		new VoodooControl("a", "css", associatedOpportunityCtrl.getHookString() + " " + convertedResultsPreviewCtrl.getHookString()).click();
		VoodooUtils.waitForReady();
		accountNameCtrl.assertEquals(leadConvertData.get("accountName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}