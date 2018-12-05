package com.sugarcrm.test.studio;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Studio_25632 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
		
		// TODO: VOOD-542
		// Go to Studio-> Calls-> Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooControl callsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		callsSubPanelCtrl.click();
		VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();

		// TODO: VOOD-542
		// Add custom field of type "HTML" and save
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		VoodooUtils.focusFrame("htmlarea_ifr");
		new VoodooControl("body", "id", "tinymce").set(customData.get("html_data"));
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-542
		// Layout subpanel
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		callsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();

		// TODO: VOOD-542
		// Record view
		new VoodooControl("td", "id", "viewBtnrecordview").click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		VoodooControl newRowCtrl = new VoodooControl("div", "css", "#toolbox .le_row.special");
		newRowCtrl.dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);   
		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		// List view
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(5)").click();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click();     
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default li:nth-of-type(5)");
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Calls customization_Verify that html content is displayed in call list view after adding the html type 
	 * custom field to list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25632_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to 'Calls' module
		DataSource ds = testData.get(testName+"_call");
		sugar().navbar.selectMenuItem(sugar().calls, "create" + sugar().calls.moduleNameSingular);
		
		// Create a call with the html format custom field not empty and save
		sugar().calls.createDrawer.setFields(ds.get(0));
		sugar().calls.createDrawer.save();
		
		// TODO: VOOD-1036
		// Go to list view and Verify HTML field & data
		sugar().calls.navToListView();
		new VoodooControl("div", "css", "th[data-fieldname='test_html_c'] .ui-draggable").assertEquals(customData.get("module_field_name_lbl"), true);
		new VoodooControl("p", "css", "[name='test_html_c'] p").assertEquals(customData.get("html_data"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}