package com.sugarcrm.test.studio;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_27206 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	FieldSet customData, fs;
	AccountRecord myAccount;
	CallRecord myCall;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify using Related Field in Visibility Formula can still display in list view properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_27206_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName).get(0);
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		sugar().alerts.waitForLoadingExpiration();
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-999//
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		studioFooterCtrl.click();
		accountsSubPanelCtrl.click();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
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

		// Create account record
		fs = new FieldSet();
		fs.put("name", customData.get("name"));
		myAccount = (AccountRecord) sugar().accounts.create(fs);
		sugar().alerts.waitForLoadingExpiration();
		
		// Assert: list view appear properly without an error
		sugar().accounts.listView.verifyField(1, "name",customData.get("name"));
				
		// Verify value of the custom field = false on record view 
	 	myAccount.navToRecord();
	 	new VoodooControl("div","css",".fld_myfield_c.detail div").assertContains("false", true);
	 	
		// Create Call record 
	    fs.clear();
	    fs.put("status", "Held");
	    myCall = (CallRecord)sugar().calls.api.create(fs);

	    fs.clear();
	    fs.put("relatedToParentType", "Account");
	    fs.put("relatedToParentName",myAccount.getRecordIdentifier());
	    myCall.edit(fs);
	    sugar().alerts.waitForLoadingExpiration();
	 		
	    // Verify value of the custom field = true on record view when added call with status "Held" related to the account
	    myAccount.navToRecord();
	    new VoodooControl("div","css",".fld_myfield_c.detail div").assertContains("true", true);
	    
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}