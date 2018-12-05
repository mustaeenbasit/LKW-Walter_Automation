package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24523 extends SugarTest {
	DataSource customData;
	FieldSet data;
	VoodooControl contactsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl;
	ContactRecord myContact;

	public void setup() throws Exception {
		customData = testData.get(testName+"_fields");
		data = testData.get(testName).get(0);
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();

		// TODO: VOOD-938
		// studio 
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl saveFieldCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl dataTypeCtrl = new VoodooControl("select", "css", "#type");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated"); 
		VoodooControl editFormulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");

		// TODO: VOOD-999
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Adding multiple custom fields, formula add on currency field (i.e add(int, mul(float * decimal)))
		for(int i=0; i< customData.size(); i++){
			fieldCtrl.click();
			VoodooUtils.waitForReady();
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			dataTypeCtrl.set(customData.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(customData.get(i).get("module_field_name"));
			if(i==3){
				calculatedCtrl.click();
				editFormulaCtrl.click();
				formulaInputCtrl.set(data.get("formula"));
				formulaSaveCtrl.click();
				VoodooUtils.waitForReady(30000);
			}
			saveFieldCtrl.click();
			VoodooUtils.waitForReady(30000);
			// multiple fields not working on JENKINS, that's why below code add
			studioFooterCtrl.click();
			VoodooUtils.waitForReady();
			contactsSubPanelCtrl.click();
			VoodooUtils.waitForReady();
		}

		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		for(int i=0; i< customData.size(); i++){
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			VoodooUtils.waitForReady();
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get(i).get("module_field_name")); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
			VoodooUtils.waitForReady();
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Check the formula consisted of add function and other functions
	 * @throws Exception
	 */
	@Test
	public void Studio_24523_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Contact Record
		myContact.navToRecord();
		sugar().contacts.recordView.edit();

		// TODO: VOOD-1036
		VoodooControl intCtrl = new VoodooControl("input", "css", ".fld_"+customData.get(2).get("module_field_name")+"_c.edit input");
		intCtrl.set(data.get("int_val"));
		new VoodooControl("input", "css", ".fld_"+customData.get(1).get("module_field_name")+"_c.edit input").set(data.get("float_val"));
		new VoodooControl("input", "css", ".fld_"+customData.get(0).get("module_field_name")+"_c.edit input").set(data.get("decimal_val"));
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Verifying the calculation result on my_currency field
		VoodooControl currencyInputCtrl = new VoodooControl("div", "css", ".fld_"+customData.get(3).get("module_field_name")+"_c.detail div");
		currencyInputCtrl.assertEquals("$"+data.get("currency_val")+"", true);
		sugar().contacts.recordView.edit();

		// Invalid case
		// wrong input integer value, don't display  data in the currency field
		intCtrl.set(data.get("special_char"));
		sugar().contacts.recordView.save();
		
		sugar().alerts.getError().assertVisible(true);
		new VoodooControl("span", "css", ".fld_"+customData.get(2).get("module_field_name")+"_c.edit")
			.assertAttribute("class", "error", true);
		new VoodooControl("span", "css", ".fld_"+customData.get(2).get("module_field_name")+"_c.edit")
			.getChildElement("span", "css", ".error-tooltip.add-on")
			.assertAttribute("data-original-title", "Error. This field requires a valid number.");

		sugar().contacts.recordView.cancel();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}