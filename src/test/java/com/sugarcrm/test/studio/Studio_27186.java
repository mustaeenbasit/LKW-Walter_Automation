package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_27186 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	FieldSet customData, userData;
	AccountRecord myAccount;
	UserRecord userRecord;
	FieldSet fs;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		sugar().login();
		
		//create user
		userData = testData.get(testName+"_user").get(0);
		userRecord = (UserRecord)sugar().users.create(userData);
	}

	/**
	 * Verify required calculated field is working properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_27186_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938 -Need library support for studio subpanel
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		accountsSubPanelCtrl.click();		
		fieldCtrl.click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula_1")+","+'"'+customData.get("formula_2")+'"'+")");
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", "input[name='required']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-999
		studioFooterCtrl.click();
		accountsSubPanelCtrl.click();
		// layout subpanel		
		layoutSubPanelCtrl.click();

		// Record view		
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		VoodooUtils.waitForReady();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// List view
		studioFooterCtrl.click();
		accountsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Create Accounts record
		fs = new FieldSet();
		fs.put("relAssignedTo", userRecord.get("lastName"));
		myAccount = (AccountRecord)sugar().accounts.create(fs);
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that required calculated field is working properly
		myAccount.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("div", "css" ,"div.record-label[data-name='"+customData.get("module_field_name")+"_c']:nth-child(1)").assertContains(customData.get("module_field_name"), true);
		new VoodooControl("div", "css" ,".record .fld_"+customData.get("module_field_name")+"_c div").assertContains(userRecord.get("lastName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}