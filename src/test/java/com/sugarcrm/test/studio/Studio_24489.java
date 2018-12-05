package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24489 extends SugarTest {
	VoodooControl notesSubPanelCtrl, layoutSubPanelCtrl, fieldCtrl, recordViewSubPanelCtrl;
	DataSource customField, typeDS, brandDS;
	AccountRecord myAccountRecord;
	OpportunityRecord myOppurtunityRecord;
	
	public void setup() throws Exception {
		myAccountRecord = (AccountRecord)sugar().accounts.api.create();
		// Data with CSV
		customField = testData.get(testName);
		typeDS = testData.get(testName+"_type");
		brandDS = testData.get(testName+"_brand");
		sugar().login();
	}

	/**
	 * Check that dependant dropdown appears in notes quick create form for Opportunities
	 * @throws Exception
	 */
	@Test
	public void Studio_24489_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938 && VOOD-542
		// studio
		notesSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Notes");
		notesSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		// Add field and save
		VoodooControl dropNameCtrl = new VoodooControl("input", "id", "drop_name");
		VoodooControl dropValCtrl = new VoodooControl("input", "id", "drop_value");
		for(int i=0; i<customField.size(); i++){
			new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
			VoodooUtils.waitForReady();
			new VoodooControl("option", "css", "#type option:nth-of-type(8)").click();
			VoodooUtils.waitForReady();
			new VoodooControl("input", "id", "field_name_id").set(customField.get(i).get("module_field_name"));
			VoodooUtils.waitForReady();
			new VoodooControl("input", "css", "#popup_form_id table tr:nth-of-type(6) input[value='Add']").click();
			VoodooUtils.pause(2000); // TODO: PAT-1957 - Studio buttons remain disabled longer than intended
			for(int j=0; j<3; j++){
				if(i==0){
					dropNameCtrl.set(typeDS.get(j).get("name"));
					dropValCtrl.set(typeDS.get(j).get("value"));
				}else if(i==1){
					dropNameCtrl.set(brandDS.get(j).get("name"));
					dropValCtrl.set(brandDS.get(j).get("value"));
				}
				new VoodooControl("input", "id", "dropdownaddbtn").click();
				VoodooUtils.pause(2000); // TODO: PAT-1957 - Studio buttons remain disabled longer than intended
			}
			// save options
			new VoodooControl("input", "id", "saveBtn").click();
			VoodooUtils.waitForReady(30000);
			if(i==1){
				// brand dependency with type dropdown
				new VoodooControl("option", "css", "#depTypeSelect option:nth-of-type(2)").click();
				new VoodooControl("button", "css", "#visGridRow td:nth-of-type(2) button").click();
				VoodooUtils.waitForReady();
				
				// No brand
				VoodooControl dragToType1 = new VoodooControl("ul", "css", "#visGridWindow div.bd div:nth-child(5) table tbody tr td:nth-child(1) ul");
				new VoodooControl("li", "css", "#childTable li:nth-of-type(1)").dragNDrop(dragToType1);
				// Brand 1
				VoodooControl dragToType2 = new VoodooControl("ul", "css", "#visGridWindow div.bd div:nth-child(5) table tbody tr td:nth-child(2) ul");
				new VoodooControl("li", "css", "#childTable li:nth-of-type(2)").dragNDrop(dragToType2);
				// Brand 2
				VoodooControl dragToType3 = new VoodooControl("ul", "css", "#visGridWindow div.bd div:nth-child(5) table tbody tr td:nth-child(3) ul");
				new VoodooControl("li", "css", "#childTable li:nth-of-type(3)").dragNDrop(dragToType3);

				// Save
				new VoodooControl("button", "css", "#visGridWindow div.bd div:nth-child(6) button:nth-child(2)").click();
			}
			new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
			VoodooUtils.waitForReady(30000);
		}
		
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(4)").click();
		VoodooUtils.waitForReady();

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
		VoodooControl moveToNewFilter1 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		VoodooControl moveToNewFilter2 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();

		// TODO: VOOD-999
		String typeDragToBC = String.format("div[data-name=%s_c]",customField.get(0).get("module_field_name")); 
		String brandDragToBC = String.format("div[data-name=%s_c]",customField.get(1).get("module_field_name"));
		new VoodooControl("div", "css", typeDragToBC).dragNDrop(moveToNewFilter1);
		new VoodooControl("div", "css", brandDragToBC).dragNDrop(moveToNewFilter2);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Opportunities
		FieldSet newData = new FieldSet();
		newData.put("relAccountName", myAccountRecord.getRecordIdentifier());
		myOppurtunityRecord = (OpportunityRecord)sugar().opportunities.create(newData);
		myOppurtunityRecord.navToRecord();
		sugar().opportunities.recordView.subpanels.get(sugar().notes.moduleNamePlural).addRecord();
	
		String typeVal = String.format(".fld_%s_c.edit div a span:nth-of-type(1)", customField.get(0).get("module_field_name"));
		VoodooControl typeDropdownCtrl = new VoodooControl("span", "css", typeVal);
		String brandVal = String.format(".fld_%s_c.edit div a span:nth-of-type(1)", customField.get(1).get("module_field_name"));
		VoodooControl brandDropdownCtrl = new VoodooControl("span", "css", brandVal);
		
		// verify no brand with type 1
		typeDropdownCtrl.assertEquals(typeDS.get(0).get("value"), true);
		brandDropdownCtrl.assertEquals(brandDS.get(0).get("value"), true);
		
		// verify brand 1 with type 2
		String typeDropDownStr =  String.format(".fld_%s_c.edit div.select2", customField.get(0).get("module_field_name"));
		VoodooControl dropdownCtrl = new VoodooControl("div", "css", typeDropDownStr);
		VoodooControl type2OptionCtrl = new VoodooControl("li", "css", ".select2-results li:nth-of-type(2)");
		dropdownCtrl.click();
		type2OptionCtrl.click();
		typeDropdownCtrl.assertEquals(typeDS.get(1).get("value"), true);		
		brandDropdownCtrl.assertEquals(brandDS.get(1).get("value"), true);
		
		// verify brand 2 with type 3
		dropdownCtrl.click();
		VoodooControl type3OptionCtrl = new VoodooControl("li", "css", ".select2-results li:nth-of-type(3)");
		type3OptionCtrl.click();
		typeDropdownCtrl.assertEquals(typeDS.get(2).get("value"), true);		
		brandDropdownCtrl.assertEquals(brandDS.get(2).get("value"), true);
		sugar().notes.createDrawer.cancel();
		
		// Accounts
		myAccountRecord.navToRecord();
		sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural).addRecord();
		
		// verify no brand with type 1
		typeDropdownCtrl.assertEquals(typeDS.get(0).get("value"), true);
		brandDropdownCtrl.assertEquals(brandDS.get(0).get("value"), true);
		
		// verify brand 1 with type 2
		dropdownCtrl.click();
		type2OptionCtrl.click();
		typeDropdownCtrl.assertEquals(typeDS.get(1).get("value"), true);		
		brandDropdownCtrl.assertEquals(brandDS.get(1).get("value"), true);
		
		// verify brand 2 with type 3
		dropdownCtrl.click();
		type3OptionCtrl.click();
		typeDropdownCtrl.assertEquals(typeDS.get(2).get("value"), true);		
		brandDropdownCtrl.assertEquals(brandDS.get(2).get("value"), true);
		sugar().notes.createDrawer.cancel();
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
