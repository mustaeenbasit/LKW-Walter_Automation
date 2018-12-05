package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24510 extends SugarTest {
	VoodooControl accountsButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl formulaInputCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter;
	VoodooControl formulaResultCtrl;
	
	FieldSet customData;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-938
		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		
		customData = testData.get(testName).get(0);
		
		// Navigate to Admin > Studio > Accounts > Fields > Name		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();

		new VoodooControl("input", "id", "formulaFuncSearch").set(customData.get("formula_name"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#functionsGrid .yui-dt-even .yui-dt-col-name .yui-dt-liner").click();

		new VoodooControl("input", "id", "formulaFieldsSearch").set(customData.get("formula_field"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#fieldsGrid .yui-dt-even .yui-dt-col-name .yui-dt-liner").click();

		// Add Closing parenthesis
		String formulaWithCloseParenthesis = String.format("%s)", formulaInputCtrl.getAttribute("value"));		
		formulaInputCtrl.set(formulaWithCloseParenthesis);

		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		
		// Save button
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// Accounts in breadcrumb
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(4)").click();

		layoutsButtonCtrl.click();

		// Add new field to Accounts Record view
		recordViewButtonCtrl.click();	
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 

		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		sugar().alerts.waitForLoadingExpiration();
		
		VoodooUtils.focusDefault();

		// Create an example rec in Accounts module
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("name1_small"));
		myAccount = (AccountRecord)sugar().accounts.create(fs);
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Show calculated fields in audit log
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24510_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit new Field to set audited to true
		
		// Navigate to Admin > Studio > Accounts > Fields > Name		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Goto Fields
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Edit field and save
		String customInputEditField = String.format("%s_c",customData.get("module_field_name")); 
		new VoodooControl("a", "id", customInputEditField).click();
		new VoodooControl("input", "name", "audited").set("true");
		
		// Save button
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		sugar().alerts.waitForLoadingExpiration();
		
		VoodooUtils.focusDefault();
		
		myAccount.navToRecord();
		
		// TODO: VOOD-935
		String customInputField = String.format(".fld_%s_c.detail .ellipsis_inline",customData.get("module_field_name")); 
		formulaResultCtrl = new VoodooControl("div", "css", customInputField);

		// Verify if the new formula field works with data1
		formulaResultCtrl.assertEquals(customData.get("name1_cap"), true);

		// Verify if the new formula field works with data2
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("name").set(customData.get("name2_small"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		formulaResultCtrl.assertEquals(customData.get("name2_cap"), true);
		
		// Check Audit Log
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_audit_button a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Assert if old and new values exist for 'Name'
		new VoodooControl("td", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[3]/table/tbody/tr[contains(.,'Name')]/td[2]").assertContains(customData.get("name1_small"), true);
		new VoodooControl("td", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[3]/table/tbody/tr[contains(.,'Name')]/td[3]").assertContains(customData.get("name2_small"), true);

		// Assert if old and new values exist for the new field
		new VoodooControl("td", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[3]/table/tbody/tr[contains(.,'"+customData.get("module_field_name")+"')]/td[2]").assertContains(customData.get("name1_cap"), true);
		new VoodooControl("td", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[3]/table/tbody/tr[contains(.,'"+customData.get("module_field_name")+"')]/td[3]").assertContains(customData.get("name2_cap"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}