package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24551 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, fieldCtrl, saveBtnCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, studioFooterCtrl;
	DataSource customFieldData;
	FieldSet formulaData;
	AccountRecord myAccount;

	public void setup() throws Exception {
		customFieldData = testData.get(testName+"_fields");
		formulaData = testData.get(testName).get(0);
		myAccount = (AccountRecord)sugar().accounts.api.create();

		sugar().login();
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");

		// For add field control
		VoodooControl addFieldBtn = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl moduleFieldName = new VoodooControl("input", "id", "field_name_id");
		VoodooControl dataType = new VoodooControl("select", "css", "#type");
		saveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		// TODO: VOOD-999
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");

		// Add fields
		for (int i = 0; i < customFieldData.size(); i++) {   
			fieldCtrl.click();
			VoodooUtils.waitForReady();
			addFieldBtn.click();
			VoodooUtils.waitForReady();
			dataType.set(customFieldData.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			moduleFieldName.set(customFieldData.get(i).get("field_name"));
			saveBtnCtrl.click();
			VoodooUtils.waitForReady(30000);
			// multiple fields not working on JENKINS, that's why below code add
			studioFooterCtrl.click();
			VoodooUtils.waitForReady();
			accountsSubPanelCtrl.click();
			VoodooUtils.waitForReady();
		}

		// TODO: VOOD-938
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		VoodooControl newRowCtrl = new VoodooControl("div", "css", "#toolbox .le_row.special");

		for (int i = 0; i < customFieldData.size(); i++) {   
			newRowCtrl.dragNDrop(moveToLayoutPanelCtrl);
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customFieldData.get(i).get("field_name")+"_c"); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);   
		}
		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Check the formula consisted of greaterThan function and other functions
	 * @throws Exception
	 */
	@Test
	public void Studio_24551_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Accounts field
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldCtrl.click(); 		
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		// Edit formula field and save
		new VoodooControl("a", "id", formulaData.get("formula_field")+"_c").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		String formulaStr = String.format("%s($%s_c,%s($%s_c,%s($%s_c,$%s_c)))",formulaData.get("greater_than_formula_name"),customFieldData.get(3).get("field_name"),formulaData.get("subtract_formula_name"),customFieldData.get(0).get("field_name"),formulaData.get("add_formula_name"),customFieldData.get(2).get("field_name"),customFieldData.get(1).get("field_name"));
		new VoodooControl("textarea", "css", "#formulaInput").set(formulaStr);
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady(30000);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Accounts
		myAccount.navToRecord();

		// TODO: VOOD-1036
		// Verifying formula : greaterThan($my_int_c,subtract($my_currency_c,add($my_float_c,$my_decimal_c)))
		VoodooControl myCurrencyFieldCtrl = new VoodooControl("input", "css", "input[name='"+customFieldData.get(0).get("field_name")+"_c']");
		VoodooControl myDecimalFieldCtrl = new VoodooControl("input", "css", "input[name='"+customFieldData.get(1).get("field_name")+"_c']");
		VoodooControl myFloatFieldCtrl = new VoodooControl("input", "css", "input[name='"+customFieldData.get(2).get("field_name")+"_c']");
		VoodooControl myIntFieldCtrl = new VoodooControl("input", "css", "input[name='"+customFieldData.get(3).get("field_name")+"_c']");
		VoodooControl validOutputCtrl = new VoodooControl("div", "css",".fld_"+customFieldData.get(4).get("field_name")+"_c.detail.disabled div"); 
		VoodooControl invalidOutputCtrl = new VoodooControl("span", "css",".fld_"+customFieldData.get(4).get("field_name")+"_c.detail.disabled"); 

		// Case 1:: formula field should be true with disabled property
		sugar().accounts.recordView.edit();
		myCurrencyFieldCtrl.set(formulaData.get("currency_value"));
		myDecimalFieldCtrl.set(formulaData.get("decimal_value"));
		myFloatFieldCtrl.set(formulaData.get("float_value"));
		myIntFieldCtrl.set(formulaData.get("int_value"));
		sugar().accounts.recordView.save();
		validOutputCtrl.assertEquals(formulaData.get("success"), true);

		// Case 2:: On changing int value, formula field should be false with disabled property
		sugar().accounts.recordView.edit();
		myIntFieldCtrl.set(formulaData.get("modified_int_value"));
		sugar().accounts.recordView.save();
		validOutputCtrl.assertEquals(formulaData.get("failure"), true);

		// Case3: int with alphanumeric character, formula field should be an empty value
		sugar().accounts.recordView.edit();
		myCurrencyFieldCtrl.set(formulaData.get("currency_value"));
		myDecimalFieldCtrl.set(formulaData.get("decimal_value"));
		myFloatFieldCtrl.set(formulaData.get("float_value"));
		myIntFieldCtrl.set(formulaData.get("int_value")+formulaData.get("hash_char"));
		sugar().accounts.recordView.save();
		
		sugar().alerts.getError().assertVisible(true);
		new VoodooControl("span", "css", ".fld_"+customFieldData.get(3).get("field_name")+"_c.edit")
			.assertAttribute("class", "error", true);
		new VoodooControl("span", "css", ".fld_"+customFieldData.get(3).get("field_name")+"_c.edit")
			.getChildElement("span", "css", ".error-tooltip.add-on")
			.assertAttribute("data-original-title", "Error. This field requires a valid number.");

		sugar().accounts.recordView.cancel();

		// Case4:: currency with alphanumeric character, formula field should be an empty value
		sugar().accounts.recordView.edit();
		myCurrencyFieldCtrl.set(formulaData.get("currency_value")+formulaData.get("ampersand_char"));
		myDecimalFieldCtrl.set(formulaData.get("decimal_value"));
		myFloatFieldCtrl.set(formulaData.get("float_value"));
		myIntFieldCtrl.set(formulaData.get("int_value"));
		sugar().accounts.recordView.save();

		sugar().alerts.getError().assertVisible(true);
		new VoodooControl("span", "css", ".fld_"+customFieldData.get(0).get("field_name")+"_c.edit")
			.assertAttribute("class", "error", true);
		new VoodooControl("span", "css", 
				".fld_"+customFieldData.get(0).get("field_name")+"_c.edit .input-append.input .error-tooltip.add-on[data-original-title]")
			.assertAttribute("data-original-title", "Error. This field requires a valid number.");

		sugar().accounts.recordView.cancel();

		// Case5:: decimal with alphanumeric character, formula field should be an empty value
		sugar().accounts.recordView.edit();
		myCurrencyFieldCtrl.set(formulaData.get("currency_value"));
		myDecimalFieldCtrl.set(formulaData.get("decimal_value")+formulaData.get("exclamation_char"));
		myFloatFieldCtrl.set(formulaData.get("float_value"));
		myIntFieldCtrl.set(formulaData.get("int_value"));
		sugar().accounts.recordView.save();

		sugar().alerts.getError().assertVisible(true);
		new VoodooControl("span", "css", ".fld_"+customFieldData.get(1).get("field_name")+"_c.edit")
			.assertAttribute("class", "error", true);
		new VoodooControl("span", "css", ".fld_"+customFieldData.get(1).get("field_name")+"_c.edit")
			.getChildElement("span", "css", ".error-tooltip.add-on")
			.assertAttribute("data-original-title", "Error. This field requires a valid number.");

		sugar().accounts.recordView.cancel();

		// Case6:: float with alphanumeric character, formula field should be an empty value
		sugar().accounts.recordView.edit();
		myCurrencyFieldCtrl.set(formulaData.get("currency_value"));
		myDecimalFieldCtrl.set(formulaData.get("decimal_value"));
		myFloatFieldCtrl.set(formulaData.get("float_value")+formulaData.get("percentage_char"));
		myIntFieldCtrl.set(formulaData.get("int_value"));
		sugar().accounts.recordView.save();

		sugar().alerts.getError().assertVisible(true);
		new VoodooControl("span", "css", ".fld_"+customFieldData.get(2).get("field_name")+"_c.edit")
			.assertAttribute("class", "error", true);
		new VoodooControl("span", "css", ".fld_"+customFieldData.get(2).get("field_name")+"_c.edit")
			.getChildElement("span", "css", ".error-tooltip.add-on")
			.assertAttribute("data-original-title", "Error. This field requires a valid number.");

		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
