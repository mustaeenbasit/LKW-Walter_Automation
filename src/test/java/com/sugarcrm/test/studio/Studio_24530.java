package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24530 extends SugarTest {
	DataSource customData;
	FieldSet data;
	VoodooControl contactsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl, descriptionCtrl, calculatedCtrl, saveFieldCtrl;
	ContactRecord myContact;

	public void setup() throws Exception {
		customData = testData.get(testName+"_fields");
		data = testData.get(testName).get(0);
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio 
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		saveFieldCtrl  = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl dataTypeCtrl = new VoodooControl("select", "css", "#type");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");

		// TODO: VOOD-999
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
			saveFieldCtrl.click();
			VoodooUtils.waitForReady();
			// multiple fields not working on JENKINS, that's why below code add
			studioFooterCtrl.click();
			VoodooUtils.waitForReady();
			contactsSubPanelCtrl.click();
			VoodooUtils.waitForReady();
		}
		// Edit description with formula: valueAt(1,enum(add($my_decimal_c,$my_currency_c),multiply($my_float_c,$my_int_c)))
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		descriptionCtrl = new VoodooControl("a", "id", "description");
		descriptionCtrl.click();
		VoodooUtils.waitForReady();
		calculatedCtrl = new VoodooControl("input", "id", "calculated");
		calculatedCtrl.click(); 
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(data.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
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
			moveToLayoutPanelCtrl.waitForVisible();
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get(i).get("module_field_name")); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Check the formula consisted of valueAt function and other functions
	 * @throws Exception
	 */
	@Test
	public void Studio_24530_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Contact Record
		myContact.navToRecord();
		sugar().contacts.recordView.edit();

		// TODO: VOOD-1036
		VoodooControl intCtrl = new VoodooControl("input", "css", ".fld_"+customData.get(2).get("module_field_name")+"_c.edit input");
		intCtrl.set(data.get("int_val"));
		new VoodooControl("input", "css", ".fld_"+customData.get(1).get("module_field_name")+"_c.edit input").set(data.get("float_val"));
		new VoodooControl("input", "css", ".fld_"+customData.get(0).get("module_field_name")+"_c.edit input").set(data.get("decimal_val"));
		new VoodooControl("input", "css", "input[name='"+customData.get(3).get("module_field_name")+"_c']").set(data.get("currency_val"));
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.recordView.showMore();

		// Verifying the calculation result on description field
		sugar().contacts.recordView.getDetailField("description").assertEquals(data.get("result"), true);
		sugar().contacts.recordView.edit();

		// Invalid case
		// wrong input integer value, don't display  data in the description field
		intCtrl.set(data.get("special_char"));
		sugar().contacts.recordView.getDetailField("description").hover();
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