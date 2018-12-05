
package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_26870 extends SugarTest {
	FieldSet myData;
	VoodooControl accountSubPanelCtrl, fieldsBtn, recordViewSubPanelCtrl, layoutSubPanelCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify default value field is disabled for calculated field.
	 * @throws Exception
	 */
	@Test
	public void Studio_26870_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		myData = testData.get(testName).get(0);

		// Create a custom field in accounts module.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		
		// TODO: VOOD-938
		accountSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "[name='editFormula']").click();
		new VoodooControl("textarea", "id", "formulaInput").set(myData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		new VoodooControl("input", "id", "field_name_id").set(myData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// TODO: Investigate Jenkins failure at the below line.
		// new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();

		// TODO: Remove when Jenkins will pass the above line.
		new VoodooControl("input", "css", "#footerHTML input[value='Studio']").click();
		VoodooUtils.waitForReady();
		accountSubPanelCtrl.click();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add custom field to Record view
		// TODO: VOOD-938
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get("display_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Verify the default value field will be disabled (grayed out).
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("span", "css", ".fld_myfield_c.disabled.edit").assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}