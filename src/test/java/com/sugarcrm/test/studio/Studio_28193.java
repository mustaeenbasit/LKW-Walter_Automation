package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Studio_28193 extends SugarTest {
	DataSource customData;
	ContactRecord myContact;
	VoodooControl fieldNameCtrl,saveButtonCtrl,dataTypeCtrl,addFieldCtrl,moveToNewFilter,moveToLayoutPanelCtrl,recordViewCtrl,resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,contactsCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName);
		myContact =(ContactRecord)sugar().contacts.api.create();

		sugar().login();
		// TODO: VOOD-938
		contactsCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
		addFieldCtrl=new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		dataTypeCtrl=new VoodooControl("select", "id", "type");
		saveButtonCtrl=new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		fieldNameCtrl=new VoodooControl("input", "id", "field_name_id");
	}

	/**
	 * Verify that Calculated Decimal Fields should be saved with correct precision
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_28193_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Contacts > fields
		// TODO: VOOD-938
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		contactsCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();

		// Create three custom fields, two Integer fields and one Decimal field
		for (int i = 0; i < customData.size(); i++){
			addFieldCtrl.click();
			dataTypeCtrl.set(customData.get(i).get("dataType"));
			sugar().alerts.waitForLoadingExpiration();
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(customData.get(i).get("fieldName"));
			if (i==2){
				new VoodooControl("input", "css", "[name='len']").set(customData.get(i).get("maxSize"));
				new VoodooControl("input", "id", "precision").set(customData.get(i).get("precision"));
				new VoodooControl("input", "id", "calculated").click();
				new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
				new VoodooControl("textarea", "id", "formulaInput").set(customData.get(i).get("formula"));
				new VoodooControl("input", "id", "fomulaSaveButton").click();
				VoodooUtils.waitForReady();
			}
			saveButtonCtrl.click();
			VoodooUtils.waitForReady();
		}

		// Add created custom fields to Studio > Contacts > Layouts > Record View
		sugar().admin.studio.clickStudio();
		contactsCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		recordViewCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewCtrl.click(); 
		VoodooUtils.waitForReady();
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		for (int i = 0 ; i < customData.size() ; i++){
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get(i).get("fieldName"));
			if (i==2){
				new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));	
			}
			else
				new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type("+ (i+1) +")"));
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Edit custom fields of Contact Record
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		// TODO: VOOD-1036 Need library support for Accounts/any sidecar module for newly created custom fields
		new VoodooControl("input", "css", "[name='integer_one_c']").set(customData.get(2).get("precision"));
		new VoodooControl("input", "css", "[name='integer_two_c']").set(customData.get(2).get("maxSize"));
		sugar().contacts.recordView.save();

		// Verify Calculated Decimal Field is saved with correct precision
		new VoodooControl("div", "css", ".fld_decimal_one_c.disabled.detail .ellipsis_inline").assertContains(customData.get(2).get("decimalData"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}