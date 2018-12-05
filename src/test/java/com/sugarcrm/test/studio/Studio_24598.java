package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24598 extends SugarTest {
	VoodooControl accountsSubPanelCtrl;
	VoodooControl layoutSubPanelCtrl;
	VoodooControl recordViewSubPanelCtrl;
	VoodooControl fieldCtrl;
	FieldSet customData;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Function isBefore in calculated field 
	 * @throws Exception
	 */
	@Test
	public void Studio_24598_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();

		// TODO: VOOD-938
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
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		String formulaStr = formulaInputCtrl.getAttribute("value");
		String formulaWithComma = String.format("%s,", formulaStr);
		formulaInputCtrl.set(formulaWithComma);
		new VoodooControl("div", "css", "#fieldsGrid .yui-dt-odd .yui-dt-col-name .yui-dt-liner").click();
		String formulaWithCloseParenthesis = String.format("%s)", formulaInputCtrl.getAttribute("value"));
		formulaInputCtrl.set(formulaWithCloseParenthesis);
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(4)").click();
		
		// TODO: VOOD-938
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		
		// TODO: VOOD-938
		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.focusDefault();
		
		// Accounts module
		AccountRecord myAccount = (AccountRecord)sugar().accounts.create();
		myAccount.navToRecord();
		
		// TODO: VOOD-935
		String customInputField = String.format(".fld_%s_c.detail .ellipsis_inline",customData.get("module_field_name")); 
		VoodooControl formulaResultCtrl = new VoodooControl("div", "css", customInputField);

		// Verify with data: first date is exactly the second date
		formulaResultCtrl.assertEquals(customData.get("failure_msg"), true);
		
		// Verify with new data: first date is before the second date
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("name").set(customData.get("name"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		formulaResultCtrl.assertEquals(customData.get("success_msg"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}