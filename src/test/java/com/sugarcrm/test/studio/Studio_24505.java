package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24505 extends SugarTest {
	VoodooControl contactsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, editFormulaCtrl, formulaInputCtrl, formulaSaveCtrl, saveFieldCtrl, studioFooter, newFieldCtrl;
	FieldSet customData;
	ContactRecord myContact;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();

		// TODO: VOOD-938
		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();
		editFormulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		editFormulaCtrl.click();
		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		formulaInputCtrl.set(customData.get("formula"));
		formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();
		saveFieldCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-999
		studioFooter = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		studioFooter.click();
		contactsSubPanelCtrl.click();

		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

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
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Contacts
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		String lastName = sugar().contacts.recordView.getEditField("lastName").getText();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verifying newly created field 
		newFieldCtrl = new VoodooControl("div", "css" ,".fld_"+customData.get("module_field_name")+"_c.detail div");
		newFieldCtrl.assertEquals(lastName, true);
	}

	/**
	 * Formulas can be edited  
	 * @throws Exception
	 */
	@Test
	public void Studio_24505_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		contactsSubPanelCtrl.click();
		fieldCtrl.click();

		// TODO: VOOD-938
		// New formula
		new VoodooControl("a", "id", customData.get("module_field_name")+"_c").click();
		editFormulaCtrl.click();
		formulaInputCtrl.set(customData.get("new_formula"));
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Contact Record
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		String firstName = sugar().contacts.recordView.getEditField("firstName").getText();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verifying field with new formula
		newFieldCtrl.assertEquals(firstName, true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}