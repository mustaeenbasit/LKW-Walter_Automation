package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Cases_17164 extends SugarTest {
	FieldSet customData;
	VoodooControl casesSubPanelCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().cases.api.create();
		casesSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Cases");
		sugar().login();

		// Add a custom checkbox to the cases module
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		casesSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		casesSubPanelCtrl.click();

		// layout subpanel
		VoodooControl layoutSubPanelCtrl =  new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// List view
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		String dataNameDraggableLi = String.format("li[data-name=%s_c]",customData.get("module_field_name"));
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtn = new VoodooControl("td", "id", "savebtn");
		saveBtn.click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		casesSubPanelCtrl.click();
		layoutSubPanelCtrl.click();

		// Search View
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultSearchCtrl = new VoodooControl("ul", "id", "ul0");
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSearchCtrl);
		saveBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Available operators for Checkbox type field when defining filter
	 * @throws Exception
	 */

	@Test
	public void Cases_17164_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterCreateNew();

		// TODO: VOOD-1879 - Need Individual Controls for Filter Fields in List View of Sidecar modules.
		// Verify operator dropdown is having value "is"
		String searchText =  customData.get("module_field_name").replace('_', ' ');
		new VoodooSelect("span", "css", "[data-filter='row']:nth-of-type(1) .fld_filter_row_name.detail").set(searchText);
		new VoodooControl("a", "css", "[data-filter='row']:nth-of-type(1) .fld_filter_row_operator.detail a").click();
		VoodooControl operatorDropdownCtrl = new VoodooControl("div", "css", "div#select2-drop ul li div");
		operatorDropdownCtrl.assertEquals(customData.get("operator"), true);
		operatorDropdownCtrl.click();

		// Cancel filter
		sugar().cases.listView.filterCreate.cancel();

		// TODO: VOOD-1036
		// Verify checkbox custom field on listview
		new VoodooControl("span", "css", ".fld_" + customData.get("module_field_name") + "_c.list").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
