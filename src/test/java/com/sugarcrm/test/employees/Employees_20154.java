package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20154 extends SugarTest {
	VoodooControl employeesCtrl,fieldCtrl,layoutSubPanelCtrl,editViewSubPanelCtrl,detailViewSubPanelCtrl,studioFooterCtrl;
	VoodooControl usersCtrl,saveField,addField,inputFieldName,saveAndDeploy,moveToLayoutPanelCtrl,moveToNewFilter;
	DataSource customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar.login();
	}

	/**
	 * Verify that Employees custom fields correctly works in Users and Employees modules.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20154_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938
		employeesCtrl = new VoodooControl("a", "id", "studiolink_Employees");
		usersCtrl = new VoodooControl("a", "id", "studiolink_Users");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		editViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtneditview");
		detailViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtndetailview");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		addField = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		inputFieldName = new VoodooControl("input", "id", "field_name_id");
		saveField = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		saveAndDeploy = new VoodooControl("input", "id", "publishBtn");		
		
		// Employees Module.
		sugar.admin.adminTools.getControl("studio").click();
		employeesCtrl.click();
		fieldCtrl.click();
		
		// Create a text field in Employees module.
		addField.click();
		inputFieldName.set(customData.get(0).get("module_field_name"));
		saveField.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1210 -On Jenkins, test is failing at below line.
		//studioFooterCtrl.click();
		VoodooUtils.focusDefault();
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		
		employeesCtrl.click();
		layoutSubPanelCtrl.click();

		// Add custom field in Edit View
		editViewSubPanelCtrl.click();	
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel1 = String.format("div[data-name=%s]",customData.get(0).get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel1).dragNDrop(moveToNewFilter);
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
				
		studioFooterCtrl.click();
		employeesCtrl.click();
		layoutSubPanelCtrl.click();

		// TODO: VOOD-938
		// Add custom field in Detail View
		detailViewSubPanelCtrl.click();	
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel1).dragNDrop(moveToNewFilter);
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
				
		// Users Module.
		// TODO: VOOD-999
		studioFooterCtrl.click();
		usersCtrl.click();
		fieldCtrl.click();
		
		// Create a text field in Users module.
		addField.click();
		inputFieldName.set(customData.get(1).get("module_field_name"));
		saveField.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1210 -On Jenkins, test is failing at below line.
		//studioFooterCtrl.click();		
		VoodooUtils.focusDefault();
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
				
		usersCtrl.click();
		layoutSubPanelCtrl.click();

		// Add custom field in Edit View
		editViewSubPanelCtrl.click();	
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel2 = String.format("div[data-name=%s]",customData.get(1).get("display_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel2).dragNDrop(moveToNewFilter);
		saveAndDeploy.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();
				
		studioFooterCtrl.click();
		usersCtrl.click();
		layoutSubPanelCtrl.click();

		// TODO: VOOD-938
		// Add custom field in Detail View
		detailViewSubPanelCtrl.click();	
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel2).dragNDrop(moveToNewFilter);
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Edit the Admin record in the Employees module, adding a string in custom fields and Save.
		sugar.navbar.toggleUserActionsMenu();
		// TODO VOOD-1041
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.users.listView.basicSearch("Administrator");
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "edit_button").click();
		String inputField1 = String.format("%s",customData.get(0).get("display_name"));
		new VoodooControl("input", "id", inputField1).set(customData.get(0).get("value"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify, the data is saved and appears in the fields.
		new VoodooControl("span", "id", inputField1).assertContains(customData.get(0).get("value"), true);
		VoodooUtils.focusDefault();
		
		// Edit the Admin record in the Users module, adding a string in custom fields and Save.
		sugar.users.navToListView();
		sugar.users.listView.basicSearch("Administrator");
		sugar.users.listView.clickRecord(1);
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		String inputField2 = String.format("%s",customData.get(1).get("display_name"));
		new VoodooControl("input", "id", inputField2).set(customData.get(1).get("value"));
		VoodooUtils.focusDefault();
		sugar.users.editView.save();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify, the data is saved and appears in the fields.
		new VoodooControl("span", "id", inputField2).assertContains(customData.get(1).get("value"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}