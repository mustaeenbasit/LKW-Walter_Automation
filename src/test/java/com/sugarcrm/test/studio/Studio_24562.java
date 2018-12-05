package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24562 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl;
	FieldSet customData;
	AccountRecord myAccount;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Function isValidDate in calculated field  
	 * @throws Exception
	 */
	@Test
	public void Studio_24562_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl saveFieldCtrl  = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated");
		VoodooControl editFormulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");
		// TODO: VOOD-999
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");

		// Adding multiple custom fields, formula add on result field (i.e isValidDate($mydate_c))
		for(int i=1; i< 3; i++){ // 2 fields
			fieldCtrl.click();
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(customData.get("field_"+i));
			if(i==2){
				calculatedCtrl.click();
				editFormulaCtrl.click();
				formulaInputCtrl.set(customData.get("formula"));
				formulaSaveCtrl.click();
				VoodooUtils.waitForReady();
			}
			saveFieldCtrl.click();
			VoodooUtils.waitForReady();
			// multiple fields not working on JENKINS, that's why below code add
			studioFooterCtrl.click();
			accountsSubPanelCtrl.click();
		}

		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// TODO: VOOD-938
		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter1 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		VoodooControl moveToNewFilter2 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();

		// TODO: VOOD-999
		new VoodooControl("div", "css", "div[data-name="+customData.get("field_1")+"_c]").dragNDrop(moveToNewFilter1);
		new VoodooControl("div", "css", "div[data-name="+customData.get("field_2")+"_c]").dragNDrop(moveToNewFilter2);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Account record
		myAccount.navToRecord();

		// Case1:: valid date with true message on result field
		sugar().accounts.recordView.edit();
		// TODO: VOOD-1036
		VoodooControl dateCtrl = new VoodooControl("input", "css", ".fld_"+customData.get("field_1")+"_c.edit input");
		dateCtrl.set(customData.get("date_valid"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl resultCtrl = new VoodooControl("div", "css", ".fld_"+customData.get("field_2")+"_c.detail div");
		resultCtrl.assertEquals(customData.get("true_msg"), true);	

		// Case2:: invalid date with false message on result field
		sugar().accounts.recordView.edit();
		dateCtrl.set(customData.get("date_invalid"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		resultCtrl.assertEquals(customData.get("false_msg"), true);	

		// Case3:: invalid date format with false message on result field	
		sugar().accounts.recordView.edit();
		dateCtrl.set(customData.get("date_invalid_format"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		resultCtrl.assertEquals(customData.get("false_msg"), true);	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}