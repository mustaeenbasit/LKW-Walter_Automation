package com.sugarcrm.test.workflow;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Workflow_26939 extends SugarTest {	
	VoodooControl contactsButtonCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, mathOperator, workFlow;
	VoodooControl resetButtonCtrl, resetClickCtrl, relationshipsCtrl, resetFieldsCtrl, layoutsCtrl, labelsCtrl, extensionsCtrl;
	FieldSet customData, fs = new FieldSet();
	ContactRecord myContact;
	DataSource ds;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		ds = testData.get(testName+"_math_operator");
		mathOperator = new VoodooControl("option", "css", "#adv_options td:nth-child(2) select option[value='+']");
		myContact = (ContactRecord) sugar.contacts.api.create();
		sugar.login();

		// TODO: VOOD-938 -Need library support for studio subpanel
		// studio
		contactsButtonCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");

		// Reset module
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		resetFieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		contactsButtonCtrl.click();		
		fieldCtrl.click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		sugar.admin.studio.waitForAJAX(30000); // Required more wait to reflecting fields according to data type
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-999
		// List view
		studioFooterCtrl.click();
		contactsButtonCtrl.click();
		layoutSubPanelCtrl.click();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-999
		studioFooterCtrl.click();
		contactsButtonCtrl.click();
		layoutSubPanelCtrl.click();
		// Record view		
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		VoodooUtils.waitForReady();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify advanced functions for integer field in workflow.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Workflow_26939_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1042 -Need Lib support for Workflow
		VoodooControl workFlow = new VoodooControl("a", "id", "workflow_management");
		workFlow.click();
		VoodooUtils.focusDefault();

		// Click on Workflow Arrow 
		new VoodooControl("i", "css", "li.active i.fa.fa-caret-down").click();

		// Click on Create Workflow Definition link		
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_WORKFLOW']").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// Select Contacts module and create
		new VoodooControl("input", "id", "name").set(customData.get("test_workflow"));
		new VoodooControl("option", "css", "select[name='base_module'] option[value='Contacts']").click();
		new VoodooControl("input", "css", "[type='submit']").click();
		sugar.alerts.waitForLoadingExpiration(30000);

		// Create Condition
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "#mod_type1 #type1").click();
		new VoodooControl("input", "id", "save").click();
		VoodooUtils.focusWindow(0);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		VoodooControl actionEdit = new VoodooControl("a", "css", "#contentTable tbody tr td slot:nth-child(8) table.list.view tbody tr td:nth-child(5) slot a");
		VoodooControl workFlowAction = new VoodooControl("a", "id", "NewWorkFlowActionShells");
		VoodooControl actionType = new VoodooControl("input", "css", "#mod_action_type0 #action_type0");
		VoodooControl nextStep = new VoodooControl("input", "id", "step1_next");
		VoodooControl customCreatedField = new VoodooControl("input", "id", "mod_field_44");
		VoodooControl clickOnSetText = new VoodooControl("s", "css", "body form table tbody tr:nth-child(3) td table tbody tr:nth-child(46) td a");
		VoodooControl advanceType = new VoodooControl("a", "id", "href_set_type_basic");
		VoodooControl setValue = new VoodooControl("a", "css", "#adv_options > td:nth-child(2) > input[type='text']");
		VoodooControl saveButton = new VoodooControl("input", "css", "[title='Save']");
		VoodooControl customFieldAssert = new VoodooControl("div", "css", ".fld_test_c.detail div");
		VoodooControl openWorkFlowRecord = new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(3) > b > a");

		for(int i = 0; i < ds.size(); i++) {
			if(i == 0) {
				// Create Action
				workFlowAction.click();
				VoodooUtils.focusWindow(1);
				actionType.click();
				nextStep.click();
				sugar.alerts.waitForLoadingExpiration();
				VoodooUtils.focusFrame("selectiframe");		
				// Select custom field
				customCreatedField.click();
				VoodooUtils.focusDefault();
			} else {
				sugar.navbar.navToAdminTools();
				VoodooUtils.focusFrame("bwc-frame");

				// TODO: VOOD-1042 -Need Lib support for Workflow 
				workFlow.click();
				sugar.alerts.waitForLoadingExpiration();
				VoodooUtils.focusFrame("bwc-frame");
				openWorkFlowRecord.click();
				sugar.alerts.waitForLoadingExpiration();
				VoodooUtils.focusFrame("bwc-frame");
				actionEdit.click();
				VoodooUtils.focusWindow(1);
			}
			// Click on custom field link for set value
			clickOnSetText.waitForVisible(8000);
			clickOnSetText.click();
			VoodooUtils.focusWindow(2);			
			if(i == 0)
				advanceType.waitForVisible(1000).click(); // Click on Advance type once

			mathOperator.waitForVisible();
			new VoodooControl("option", "css", "#adv_options td:nth-child(2) select option[value='"+ds.get(i).get("math_operator")+"']").click();

			setValue.set(ds.get(1).get("assert_value")); // statically set value 2
			saveButton.click();
			VoodooUtils.focusWindow(1);
			// Save created action
			saveButton.click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusDefault();

			// Create contact record using UI after created workflow
			if(i == 0) {				
				fs.put("firstName", customData.get("firstName"));
				myContact.edit(fs);
				sugar.alerts.waitForLoadingExpiration(30000); // Required more wait to complete edit action
				
				// Assert that "Test_c" field changes to "2"
				sugar.contacts.listView.assertContains("2", true);
			}
			sugar.alerts.waitForLoadingExpiration(30000); // Required to complete above action
			
			// TODO: VOOD-1203
			// Update existing contact record for math functionality 
			fs.clear();
			fs.put("firstName", customData.get("firstName"));
			myContact.edit(fs);
			sugar.alerts.waitForLoadingExpiration(30000); // Required more wait to complete edit action
			
			// Assert that "Test_c" field changes according to "math operator"
			customFieldAssert.assertContains(ds.get(i).get("assert_value"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
