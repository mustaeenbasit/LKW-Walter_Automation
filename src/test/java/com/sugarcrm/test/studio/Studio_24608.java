package com.sugarcrm.test.studio;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_24608 extends SugarTest {
	VoodooControl contactsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl;
	DataSource customData;
	ContactRecord myContactsRecord;
	DataSource ds;
	ArrayList<Record> linkRecords = new ArrayList<Record>();
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		myContactsRecord = (ContactRecord) sugar().contacts.api.create();
		FieldSet data = new FieldSet();
		// Create 5 opportunity records to link with contact record
		for (int i = 0; i < ds.size(); i++) {
			data.put("name", ds.get(i).get("name"));
			linkRecords.add(sugar().opportunities.api.create(data));
		}
	}

	/**
	 * Check calculation with count function in formula
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24608_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		// studio 
		sugar().admin.adminTools.getControl("studio").click();
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl saveFieldCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated"); 
		VoodooControl editFormulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");

		// TODO: VOOD-999
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();
		// Create a calculated field in Contacts module
		fieldCtrl.click();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(ds.get(0).get("field_name"));
		calculatedCtrl.click();
		editFormulaCtrl.click();
		// Set formula contain count function, such as count($opportunities)
		formulaInputCtrl.set(ds.get(0).get("formula_string"));
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();
		// save custom field
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();
	
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		
		// Add the calculated custom field on detailview layouts
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","testfield"); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// navigate to current record 
		myContactsRecord.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		// 	opportunities subpanel to link existing record  
		StandardSubpanel opportunitiesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanel.linkExistingRecords(linkRecords);
		
		// navigate back to contact record to assert count of the opportunity records related to the current record
		myContactsRecord.navToRecord();
		new VoodooControl("span", "css", "[data-voodoo-name='testfield_c']").assertContains(ds.get(0).get("count"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}