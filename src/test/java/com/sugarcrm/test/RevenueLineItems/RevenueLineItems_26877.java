package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26877 extends SugarTest {
	VoodooControl moduleCtrl, subpanelsBtnCtrl, rliSubpanelBtnCtrl, statusFieldInDefaultColumnCtrl, hiddenColumnCtrl,  historyDefault, saveBtnCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();

		// Create an Opportunity record(Create Opportunity from UI so that linked to the account record and Revenue Line Item exists linked to the Opportunity record as well)
		sugar().opportunities.create();
	}

	/**
	 * ENT/ULT: Verify that Opportunity can be saved successfully when Status field is added to RLI subpanel in opportunity record view  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26877_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet salesStageData = testData.get(testName).get(0);

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		subpanelsBtnCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		rliSubpanelBtnCtrl = new VoodooControl("a","xpath", "//table[@class='wizardButton']/tbody/tr[2]/td/a[contains(.,'Revenue Line Items')]");
		VoodooControl defaultColumnCtrl = new VoodooControl("td", "id", "Default");
		VoodooControl statusFieldInHiddenColumnCtrl = new VoodooControl("li", "css", "#Hidden li[data-name='status']");
		statusFieldInDefaultColumnCtrl = new VoodooControl("li", "css", "#Default li[data-name='status']");
		hiddenColumnCtrl = new VoodooControl("td", "id", "Hidden");
		historyDefault = new VoodooControl("input", "id", "historyDefault");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");

		// Go to Opportunities > Subpanels > Revenue Line Items
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		subpanelsBtnCtrl.click();
		VoodooUtils.waitForReady();
		rliSubpanelBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Drag and drop the 'Status' field into the bottom of the Default column
		statusFieldInHiddenColumnCtrl.dragNDrop(defaultColumnCtrl);
		VoodooUtils.waitForReady();

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		//  Navigate to an existing Opportunity record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Click the arrow next to the eyeball icon, and click 'Edit.'
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();
		rliSubpanel.editRecord(1);

		// Change the Sales Stage field to any other value.
		rliSubpanel.getEditField(1, "salesStage").scrollIntoViewIfNeeded(false);
		rliSubpanel.getEditField(1, "salesStage").set(salesStageData.get("salesStage"));

		// Click the Save button to apply your changes
		rliSubpanel.getControl("saveActionRow01").click(); // Used control instead of method because in method there is 'waitForLoadingExpiration()' and in this time success alert is disappears

		// Verify that 'Saving' dialog indicates the record was saved
		sugar().alerts.getSuccess().assertContains(salesStageData.get("alertMessage"), true);

		// Verify that the change to RLI is saved successfully 
		rliSubpanel.getDetailField(1, "salesStage").assertContains(salesStageData.get("salesStage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}