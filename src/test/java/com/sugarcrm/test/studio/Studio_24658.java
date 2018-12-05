package com.sugarcrm.test.studio;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24658 extends SugarTest {
	VoodooControl accountSubPanelCtrl, fieldsBtn, layoutSubPanelCtrl, recordViewSubPanelCtrl,
	studioFooterCtrl,dependentCtrl,	saveFieldCtrl;
	DataSource myData, optionsDataSource;

	public void setup() throws Exception {
		sugar().login();

		myData = testData.get(testName);
		optionsDataSource = testData.get(testName + "_drop_down_data");

		// Create a dependent drop down  custom field in accounts module.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-938
		accountSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");

		studioFooterCtrl = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");		

		saveFieldCtrl  = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		dependentCtrl = new VoodooControl("select", "id", "depTypeSelect");

		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "input[value='Add Field']");
		VoodooControl typeCtrl = new VoodooControl("select", "id", "type");

		VoodooControl dependentTypeCtrl = new VoodooControl("select", "id", "parent_dd");
		VoodooControl editVisibilityCtrl = new VoodooControl("button", "css", "#visGridRow > td:nth-child(2) > button");

		VoodooControl blankOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(1)");
		VoodooControl blankFieldCtrl = new VoodooControl("ul", "id", "ddd_--blank--_list");

		VoodooControl analystOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(2)");
		VoodooControl analystFieldCtrl = new VoodooControl("ul", "id", "ddd_Analyst_list");

		VoodooControl competitorOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(3)");
		VoodooControl competitorFieldCtrl = new VoodooControl("ul", "id", "ddd_Competitor_list");

		VoodooControl customerOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(4)");
		VoodooControl customerFieldCtrl = new VoodooControl("ul", "id", "ddd_Customer_list");

		VoodooControl integratorOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(5)");
		VoodooControl integratorFieldCtrl = new VoodooControl("ul", "id", "ddd_Integrator_list");

		// Create two DropDown fields. Second is dependent to first one
		// TODO: VOOD-938
		for (int i = 0; i < myData.size(); i++){
			fieldsBtn.click();
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			typeCtrl.set("DropDown");
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(myData.get(i).get("module_field_name"));
			if(i == 1){
				dependentCtrl.set(myData.get(i).get("dependent"));
				dependentTypeCtrl.set(myData.get(0).get("module_field_name"));
				editVisibilityCtrl.click();
				blankOptionCtrl.dragNDrop(blankFieldCtrl);
				analystOptionCtrl.dragNDrop(analystFieldCtrl);
				competitorOptionCtrl.dragNDrop(competitorFieldCtrl);
				customerOptionCtrl.dragNDrop(customerFieldCtrl);
				integratorOptionCtrl.dragNDrop(integratorFieldCtrl);
				new VoodooControl("button", "css", "#visGridWindow  div:nth-child(6) > button:nth-child(2)").click();
			}
			saveFieldCtrl.click();
			VoodooUtils.waitForReady();
			studioFooterCtrl.click();
			accountSubPanelCtrl.click();
			VoodooUtils.waitForReady();
		}

		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Add custom field to Record view
		// TODO: VOOD-938
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		for(int i=0; i< myData.size(); i++){
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			moveToLayoutPanelCtrl.waitForVisible();
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get(i).get("display_name"));
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Set dependent dropdown field to a normal dropdown field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24658_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Get accounts default data
		FieldSet fs = sugar().accounts.getDefaultData();
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(fs.get("name"));
		// check second DropDown value depends on first DropDown
		// TODO: VOOD-1036
		new VoodooControl("span", "css", "[data-voodoo-name='myfield1_c']").click();
		new VoodooControl("div", "css", "#select2-drop  ul  li:nth-child(2)  div").click();
		// Assert second DropDown dependent value 
		new VoodooControl("span", "css", "[data-voodoo-name='myfield2_c']").assertContains(optionsDataSource.get(0).get("option_value"), true);

		// save record and navigate to the record
		sugar().accounts.createDrawer.save();
		sugar().accounts.listView.clickRecord(1);
		VoodooControl dataFieldCtrl = new VoodooControl("span", "css", "[data-name='myfield2_c']");

		// Assert dependent DropDown value on recordview  i.e. myfield2  value
		dataFieldCtrl.assertContains(optionsDataSource.get(0).get("option_value"), true);

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		accountSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn.click();

		// Open the dependent dropdown field
		new VoodooControl("a", "id", "myfield2_c").click();

		// Set None from the Dependent dropdown list 
		dependentCtrl.set(myData.get(0).get("dependent"));
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Check DropDown field values after setting None for dependent DropDown
		for (int i = 1; i < 6; i++) {
			sugar().accounts.recordView.edit();
			sugar().alerts.waitForLoadingExpiration();
			sugar().accounts.createDrawer.getEditField("name").set(fs.get("name"));
			new VoodooControl("span", "css", "[data-voodoo-name='myfield2_c']").click();
			new VoodooControl("div", "css", "#select2-drop  ul  li:nth-child("+(i+1)+")  div").click();
			sugar().accounts.recordView.save();
			sugar().alerts.waitForLoadingExpiration();
			VoodooUtils.waitForAlertExpiration();
			// Assert DropDown value on record view 
			new VoodooControl("span", "css", "[data-name='myfield2_c']").assertContains(optionsDataSource.get(i-1).get("option_value"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}