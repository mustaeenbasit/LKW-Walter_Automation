package com.sugarcrm.test.calls;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_19110 extends SugarTest {
	ArrayList<Record> multipleRecordData = new ArrayList<Record>();
	ArrayList<String> multipleModulesName = new ArrayList<String>();
	FieldSet customFS = new FieldSet();
	StandardSubpanel callSubpanelCtrl;

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		// Define Calls sub-panel Control
		callSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);

		// Create Account, Contact, Opportunity and Lead records and add them into an ArrayList
		multipleRecordData.add(sugar().accounts.api.create());
		multipleRecordData.add(sugar().contacts.api.create());
		multipleRecordData.add(sugar().opportunities.api.create());
		multipleRecordData.add(sugar().leads.api.create());

		// Add Account, Contact, Opportunity and Lead module's name in an ArrayList
		multipleModulesName.add(sugar().accounts.moduleNameSingular);
		multipleModulesName.add(sugar().contacts.moduleNameSingular);
		multipleModulesName.add(sugar().opportunities.moduleNameSingular);
		multipleModulesName.add(sugar().leads.moduleNameSingular);

		// login
		sugar().login();
	}

	// Create a Call record -> relate any module's record -> 'Close' the Call record -> Navigates to any module's record -> Verify the Status of Call record
	private void relateRecordAndCloseCall(String moduleName, Record relatedModuleRecordData, int selectOption) throws Exception {
		// In Calls module, create a Call and relate the Account/Contact/Opportunity/Lead record -> Save
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		String callRecordName = moduleName + "_" + selectOption;
		sugar().calls.createDrawer.getEditField("name").set(callRecordName);
		sugar().calls.createDrawer.showMore();
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(moduleName);
		sugar().calls.createDrawer.getEditField("relatedToParentName").set(relatedModuleRecordData.getRecordIdentifier());
		sugar().calls.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Select the created Call record from list view to open it in record view
		sugar().calls.listView.clickRecord(1);

		if (selectOption == 0) {
			// Select 'Close' for this Call record
			sugar().calls.recordView.close();
		} else {
			// Select 'Close and Create New' for this Call record
			sugar().calls.recordView.closeAndCreateNew();
			sugar().calls.createDrawer.cancel();
		}

		// Navigate to the Account/Contact/Opportunity/Lead Record View and check this Call in Calls sub panel
		sugar().calls.recordView.getDetailField("relatedToParentName").click();

		// Verify that this Call with Held status in Calls sub panel
		callSubpanelCtrl.scrollIntoViewIfNeeded(false);
		callSubpanelCtrl.expandSubpanel();
		VoodooUtils.waitForReady(); // Wait needed
		callSubpanelCtrl.getDetailField(1, "name").assertEquals(callRecordName, true);
		callSubpanelCtrl.getDetailField(1, "status").assertEquals(customFS.get("status"), true);
	}

	/**
	 * Calls_Details View_select Close or Close and Create New, does not remove this call from sub panel in its related module
	 * @throws Exception
	 */
	@Test
	public void Calls_19110_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// For Account/Contact/Opportunity/Lead verify 'Close'(if j = 0) and 'Close and Create New' (if j == 1) functionality
		for(int j = 0; j < 2; j++) {
			for(int i = 0; i < multipleRecordData.size(); i++) {
				relateRecordAndCloseCall(multipleModulesName.get(i), multipleRecordData.get(i), j);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}