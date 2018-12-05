package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_27686  extends SugarTest {

	VoodooControl contactsModuleCtrl, fieldsBtn, layoutSubPanelCtrl, recordViewSubPanelCtrl,
	studioFooterCtrl,dependentCtrl,	saveFieldCtrl;
	DataSource myData;
	ContactRecord myContact;

	public void setup() throws Exception {

		myData = testData.get(testName);
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();

		// Create a dependent drop down  custom field in contacts module.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-938
		contactsModuleCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsModuleCtrl.click();
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

		VoodooControl analystOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(2)");
		VoodooControl analystFieldCtrl = new VoodooControl("ul", "id", "ddd_Analyst_list");

		VoodooControl competitorOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(3)");
		VoodooControl competitorFieldCtrl = new VoodooControl("ul", "id", "ddd_Competitor_list");

		VoodooControl customerOptionCtrl = new VoodooControl("li", "css", "#childTable > li:nth-child(4)");
		VoodooControl customerFieldCtrl = new VoodooControl("ul", "id", "ddd_Customer_list");

		// Create two DropDown fields. Second is dependent to first one
		// TODO: VOOD-938
		for (int i = 0; i < myData.size(); i++){
			fieldsBtn.click();
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			typeCtrl.set("DropDown");
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(myData.get(i).get("module_field_name"));
			new VoodooControl("select", "id", "options").set(myData.get(0).get("drop_down_value"));
			if(i == 1){
				dependentCtrl.set(myData.get(i).get("dependent"));
				dependentTypeCtrl.set(myData.get(0).get("module_field_name"));
				editVisibilityCtrl.click();
				analystOptionCtrl.dragNDrop(analystFieldCtrl);
				competitorOptionCtrl.dragNDrop(competitorFieldCtrl);
				customerOptionCtrl.dragNDrop(customerFieldCtrl);
				new VoodooControl("button", "css", "#visGridWindow  div:nth-child(6) > button:nth-child(2)").click();
			}
			// save custom field
			saveFieldCtrl.click();
			sugar().admin.studio.waitForAJAX(45000);
			studioFooterCtrl.click();
			contactsModuleCtrl.click();
			sugar().admin.studio.waitForAJAX(45000);
		}

		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		// click layout in contacts module 
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Add custom field to Record view
		// TODO: VOOD-938
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilterOne =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		VoodooControl moveToNewFilterTwo =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(2)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		for(int i=0; i< myData.size(); i++){
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get(i).get("display_name"));
			if(i == 0){
				new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilterTwo);
			}
			else{
				new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilterOne);
			}
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify dependent drop down should be shown properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_27686_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();
		sugar().contacts.recordView.edit();

		// click first dropdown
		new VoodooControl("span", "css", "[data-voodoo-name='drop1_c']").click();
		new VoodooControl("div", "css", "#select2-drop  ul  li:nth-child(2)  div").click();

		// Verify that drop2 should be displayed in the 1st column and drop1 should be displayed in the 2nd column properly.  
		new VoodooControl("div", "css", "#content div.record > div:nth-child(2) > div:nth-child(1)").assertAttribute("data-name", myData.get(1).get("display_name"), true);
		new VoodooControl("div", "css", "#content div.record > div:nth-child(2) > div:nth-child(2)").assertAttribute("data-name", myData.get(0).get("display_name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}