package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24660 extends SugarTest {
	VoodooControl accountsSubPanelCtrl;
	VoodooControl layoutSubPanelCtrl;
	VoodooControl recordViewSubPanelCtrl;
	DataSource ds;
	FieldSet customData;
	
	public void setup() throws Exception {
		sugar().login();
		
		// TODO: VOOD-938		
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn"); // layout subpanel
	}

	/**
	 * Dependent field works with multiple dependency 
	 * @throws Exception
	 */
	@Test
	public void Studio_24660_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		customData = testData.get(testName+"_1").get(0);
		ds = testData.get(testName);
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938
		// studio
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Add field and save
		for (int i = 0; i < ds.size(); i++) {
			VoodooUtils.pause(3000); // Pause required here for add field
			new VoodooControl("input", "css", "#studiofields input:nth-child(1)").click();
			VoodooUtils.waitForReady();
			new VoodooControl("select", "css", "#type").set(ds.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			new VoodooControl("input", "id", "field_name_id").set(ds.get(i).get("module_field_name"));
			if(i < 2) {
				new VoodooControl("select", "css", "#options").set(ds.get(i).get("drop_down_list")); // Select drop down
			}
			
			if(i == 0) {
				new VoodooControl("select", "css", "[id*='default']").set(ds.get(i).get("default"));				
			}
			
			if(i == 1) {
				new VoodooControl("option", "css", "#depTypeSelect option[value='formula']").click();
			}
			
			if(i == 2) {
				new VoodooControl("input", "css", "input[name='dependent']").click();
				VoodooUtils.waitForReady();
			}
			
			if(i > 0) {
				VoodooUtils.waitForReady();
				new VoodooControl("input", "css", "#visFormulaRow input[name='editFormula']").click();
				// Set formula
				new VoodooControl("textarea", "css", "#formulaInput").set(ds.get(i).get("field_formula_part1")+'"'+ds.get(i).get("field_formula_part2")+'"'+")");
				VoodooUtils.waitForReady();
				new VoodooControl("input", "id", "fomulaSaveButton").click();
				VoodooUtils.waitForReady(30000);
			}
			// Save button
			new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
			VoodooUtils.waitForReady(30000);
		}
		
		VoodooUtils.pause(3000);
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(4)").click();
		VoodooUtils.waitForReady();
		
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

		for (int i = 0; i < ds.size(); i++) { 
			newRowCtrl.dragNDrop(moveToLayoutPanelCtrl);
			VoodooUtils.waitForReady();
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",ds.get(i).get("module_field_name")+"_c"); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);   
		}

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);

		// List view
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(5)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooUtils.waitForReady();

		for (int i = 0; i < ds.size(); i++) {
			VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
			new VoodooControl("li", "css", ".draggable[data-name='"+ds.get(i).get("module_field_name")+"_c").dragNDrop(moveHere);
		}
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", "input[name='name']").set(customData.get("name"));

		// Verify that second dropdown and custom text area are hidden
		new VoodooControl("div", "css", "div.vis_action_hidden[data-name='second_dropdown_c'] .record-label").assertExists(true);// if dropDown is hidden the css"vis_action_hidden" is exist
		new VoodooControl("div", "css", "div.vis_action_hidden[data-name='custom_textarea_c'] .record-label").assertExists(true); // if textfield is hidden the css"vis_action_hidden" is exist
		
		// Pick a value in first dropdown
		new VoodooControl("a", "css", ".fld_first_dropdown_c.edit a").click();
		new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(1)").click();
		
		// Verify that second dropdown becomes visible
		new VoodooControl("li", "css", "div.vis_action_hidden[data-name='second_dropdown_c'] .record-label").assertExists(false); // When dropDown appeared the hidden css should be removed

		// Pick a value in second drop down value
		new VoodooControl("a", "css", ".fld_second_dropdown_c.edit a").click();
		new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(2)").click();

		// Verify that custom text area becomes visible
		new VoodooControl("li", "css", "div.vis_action_hidden[data-name='custom_textarea_c'] .record-label").assertExists(false); // When textfield appeared the hidden css should be removed 
		new VoodooControl("input","css","input[name='custom_textarea_c']").set(customData.get("custom_textfield"));
		sugar().accounts.createDrawer.save();
		
		// Assert added value
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		new VoodooControl("div", "css", ".fld_first_dropdown_c.detail div").assertContains(customData.get("module_field_name1"),true);
		new VoodooControl("div", "css", ".fld_second_dropdown_c.detail div").assertContains(customData.get("module_field_name2"),true);
		new VoodooControl("div", "css", ".fld_custom_textarea_c.detail div").assertContains(customData.get("custom_textfield"),true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}