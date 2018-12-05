package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_26948 extends SugarTest {
	VoodooControl contactsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	FieldSet customData, fs;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		sugar().login();
		customData = testData.get(testName).get(0);
		
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		contactsSubPanelCtrl.click();		
		fieldCtrl.click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula_1"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-999//		
		studioFooterCtrl.click();
		contactsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		// Record view		
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that contact record can be successfully created when adding calculated date field to contact layout
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_26948_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create contact record
		fs = new FieldSet();
		fs.put("firstName", customData.get("firstName"));
		fs.put("lastName", customData.get("lastName"));
		sugar().contacts.create(fs);
		
		// Verify that contact record is created properly
		sugar().contacts.navToListView();
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.listView.verifyField(1, "fullName", customData.get("firstName")+" "+customData.get("lastName"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
