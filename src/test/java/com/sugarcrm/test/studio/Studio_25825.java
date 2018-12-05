package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25825 extends SugarTest {
	VoodooControl layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldsBtn, caseSubPanelCtrl;
	FieldSet myData = new FieldSet();
	
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
	}

	/**
	 * Verify add user related field in bug Cases module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25825_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		myData = testData.get(testName).get(0);
		
		// Create a custom field
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		caseSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Cases");
		caseSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#type option[value='relate']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select[name='ext2']").set("Users");
		new VoodooControl("input", "id", "field_name_id").set(myData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: Investigate Jenkins failure at the below line. 
		// new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		
		// TODO: Remove when Jenkins will pass the above line.
		VoodooUtils.focusDefault();
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		caseSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// List view
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]",myData.get("display_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		
		// TODO: VOOD-938
		// Layouts bread crumb link
		VoodooControl layoutSubBreadCrumbCtrl = new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(5)");
		layoutSubBreadCrumbCtrl.waitForVisible();
		layoutSubBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Add custom field to Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get("display_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		// Create a case record with related user field entered.
		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().alerts.waitForLoadingExpiration();
		sugar().cases.createDrawer.getEditField("name").set(myData.get("case_name"));
				
		// Verify search button is available.
		VoodooControl searchBtn = new VoodooControl("span", "css", ".select2-choice.select2-default .select2-chosen");
		searchBtn.waitForVisible();
		searchBtn.assertContains("Select User...", true);
		
		new VoodooControl("span", "css", "span[data-voodoo-name='"+myData.get("display_name")+"']").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		// Verify user record can be selected in pop-up window.
		new VoodooControl("li", "css", ".select2-result").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".flex-list-view tr:nth-child(1) td:nth-child(1) span input").waitForVisible();
		new VoodooControl("input", "css", ".flex-list-view tr:nth-child(1) td:nth-child(1) span input").click();
		sugar().cases.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().cases.createDrawer.save();
		VoodooUtils.waitForReady();
		
		// Verify user related field is shown correctly in List view.
		//sugar().cases.navToListView();
		new VoodooControl("th", "css", "th[data-fieldname='"+myData.get("display_name")+"']").assertVisible(true);

		// Verify user related field is shown correctly in Detail view.
		sugar().cases.listView.clickRecord(1);
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("span", "css", "span[data-name='"+myData.get("display_name")+"']").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
