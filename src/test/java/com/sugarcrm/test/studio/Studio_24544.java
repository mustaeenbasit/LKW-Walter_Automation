package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24544 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldSaveBtn;
	
	FieldSet customData;
	DataSource customFieldData;

	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-938
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		
		// For add field control
		VoodooControl moduleFieldName = new VoodooControl("input", "id", "field_name_id");
		fieldSaveBtn = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl addFieldBtn = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl fieldDataType = new VoodooControl("select", "css", "#type");
		
		customData = testData.get(testName+"_1").get(0);
		customFieldData = testData.get(testName);

		// Navigate to Admin > Studio > Accounts > Fields > Name		
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		accountsSubPanelCtrl.click(); // Click on Studio > Accounts
		VoodooUtils.waitForReady();
		fieldCtrl.click(); // Click on Field button (Stodio > Accounts > fields)
		VoodooUtils.waitForReady();
					
		// TODO: VOOD-938
		// Add fields
		for (int i = 0; i < customFieldData.size(); i++) {			
			VoodooUtils.waitForReady();
			// Add decimal field
			addFieldBtn.click();
			VoodooUtils.waitForReady();
			fieldDataType.set(customFieldData.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			moduleFieldName.set(customFieldData.get(i).get("m_field_name"));
			if(i == 4) {
				VoodooUtils.waitForReady();
				new VoodooControl("input", "id", "calculated").click();
				new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
				// Set formula
				VoodooUtils.waitForReady();
				new VoodooControl("textarea", "css", "#formulaInput").set("divide($myinteger_c,add($mycurrency_c,$myfloat_c,$mydecimal_c))");
				VoodooUtils.waitForReady();
				new VoodooControl("input", "id", "fomulaSaveButton").click();
				VoodooUtils.waitForReady(30000);
			}
			fieldSaveBtn.click();
			VoodooUtils.waitForReady(30000);
		}
		
		// Add fields to layout > record view
		// TODO: VOOD-999		
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		// layout subpanel		
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		for (int i = 0; i < customFieldData.size(); i++) {		
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customFieldData.get(i).get("m_field_name")+"_c"); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);			
		}
		
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);

		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click();
		VoodooUtils.waitForReady();
		
		for (int i = 0; i < customFieldData.size(); i++) {				
			VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
			new VoodooControl("li", "css", ".draggable[data-name='"+customFieldData.get(i).get("m_field_name")+"_c").dragNDrop(moveHere);
		}
		
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Check the formula consisted of divide function and other functions
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24544_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create Account record
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		
		new VoodooControl("input", "css", "input[name='"+customFieldData.get(1).get("m_field_name")+"_c']").set(customData.get("decimal_field"));
		new VoodooControl("input", "css", "input[name='"+customFieldData.get(2).get("m_field_name")+"_c']").set(customData.get("currency_field"));
		new VoodooControl("input", "css", "input[name='"+customFieldData.get(3).get("m_field_name")+"_c']").set(customData.get("float_field"));
		
		// Verify that myinteger have 0 value 
		new VoodooControl("input", "css", "input[name='"+customFieldData.get(0).get("m_field_name")+"_c']").set("0");
		sugar().accounts.recordView.getEditField("name").click();
		new VoodooControl("div", "css", ".fld_"+customFieldData.get(4).get("m_field_name")+"_c input").assertContains(customData.get("zero_val"), true);
		
		// Verify with special char
		new VoodooControl("input", "css", "input[name='"+customFieldData.get(1).get("m_field_name")+"_c']").set("*");
		sugar().accounts.recordView.getEditField("name").click();
		new VoodooControl("div", "css", ".fld_"+customFieldData.get(4).get("m_field_name")+"_c input").assertContains(customData.get("zero_val"), true);
		
		new VoodooControl("input", "css", "input[name='"+customFieldData.get(0).get("m_field_name")+"_c']").set(customData.get("int_field"));
		new VoodooControl("input", "css", "input[name='"+customFieldData.get(1).get("m_field_name")+"_c']").set(customData.get("decimal_field"));
		sugar().accounts.recordView.getEditField("name").click();
		
		// Verify divided value
		new VoodooControl("div", "css", ".fld_"+customFieldData.get(4).get("m_field_name")+"_c input").assertContains(customData.get("on_edit_calc_output"), true);
		
		sugar().accounts.recordView.save();
		
		sugar().accounts.navToListView();
		// Verify after save divided value
		sugar().accounts.listView.assertContains(customData.get("after_save_calc_output"), true);
		VoodooUtils.focusDefault();		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
