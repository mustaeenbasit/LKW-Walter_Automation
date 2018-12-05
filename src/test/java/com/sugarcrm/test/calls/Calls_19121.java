package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_19121 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the calculation in the quick create form for a custom field using related in the formula
	 * @throws Exception
	 */
	@Test
	public void Calls_19121_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on studio link  
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Calls").click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Adding New custom field having calculated Value.
		// TODO: VOOD-542
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("field_name"));
		new VoodooControl("input","id", "calculated").click();
		new VoodooControl("input","name", "editFormula").click();
		new VoodooControl("textarea","id", "formulaInput").set(customData.get("formula"));
		new VoodooControl("input","id", "fomulaSaveButton").click();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Navigating back to Studio Module through Footer Pane
		sugar().admin.studio.clickStudio();
		new VoodooControl("a", "id", "studiolink_Calls").click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		new VoodooControl("td", "id", "viewBtnrecordview").click();

		// Adding New Row & New Filter in Record view layout of calls
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		VoodooUtils.waitForReady();
		String dataNameDraggableFieldToRecord = String.format("div[data-name=%s_c]",customData.get("field_name")); 

		// Dragging the Custom Created Field in the Record View layout
		new VoodooControl("div", "css", dataNameDraggableFieldToRecord).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to users profile
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563 
		new VoodooControl("input", "id", "department").set(customData.get("department_val"));
		sugar().users.userPref.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel Calls = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		Calls.addRecord();

		// Verify that Custom Field "contactc_c" contains value 2.
		// TODO: VOOD-1036
		new VoodooControl("input", "css", "[name='contactc_c']").assertEquals(customData.get("assert_value"),true);
		sugar().calls.createDrawer.cancel();
	}

	public void cleanup() throws Exception {}
}