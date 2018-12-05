package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Cases_17163 extends SugarTest {
	DataSource fieldsDS = new DataSource();
	DataSource operatorDS = new DataSource();
	VoodooControl casesSubPanelCtrl, operatorDropdownCtrl;

	public void setup() throws Exception {
		// 2 CSV's one for custom fields and another for operators
		fieldsDS = testData.get(testName);
		operatorDS = testData.get(testName + "_operators");
		casesSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Cases");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl enableRangeCtrl = new VoodooControl("input", "css", "input[name='enable_range_search']");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		sugar().cases.api.create();
		sugar().login();

		// Add a custom fields to the cases module
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		casesSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		for (int i = 0; i < fieldsDS.size(); i++) {
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			dataTypeDropdownCtrl.set(fieldsDS.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			nameFieldCtrl.set(fieldsDS.get(i).get("module_field_name"));
			enableRangeCtrl.set(Boolean.toString(true));
			saveBtnCtrl.click();
			VoodooUtils.waitForReady();
		}
		sugar().admin.studio.clickStudio();
		casesSubPanelCtrl.click();

		// layout subpanel
		VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// List view
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		for (int i = 0; i < fieldsDS.size(); i++) {
			String dataNameDraggableLi = String.format("li[data-name=%s_c]", fieldsDS.get(i).get("module_field_name"));
			new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		}
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
		for (int i = 0; i < fieldsDS.size(); i++) {
			String fieldDraggableLi = String.format("li[data-name=%s_c]", fieldsDS.get(i).get("module_field_name"));
			new VoodooControl("li", "css", fieldDraggableLi).dragNDrop(defaultSearchCtrl);
		}
		saveBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Available operators for Integer, Float, Decimal type field when defining filter
	 *
	 * @throws Exception
	 */

	@Test
	public void Cases_17163_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterCreateNew();

		// TODO: VOOD-1879
		// Verify operators displayed in dropdown
		for (int i = 0; i < fieldsDS.size(); i++) {
			String searchText = fieldsDS.get(i).get("module_field_name").replace('_', ' ');
			new VoodooSelect("span", "css", "[data-filter='row']:nth-of-type(1) .fld_filter_row_name.detail").set(searchText);
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", "[data-filter='row']:nth-of-type(1) .fld_filter_row_operator.detail a").click();
			for (int j = 0; j < operatorDS.size(); j++) {
				operatorDropdownCtrl = new VoodooControl("div", "css", "div#select2-drop ul li:nth-of-type(" + (j + 1) + ") div");
				if (i == 0 || i == 1) // for decimal and float operators only
					operatorDropdownCtrl.assertEquals(operatorDS.get(j).get("decimal_float_operators"), true);
				else // special case for int field "is any of" operator
					operatorDropdownCtrl.assertEquals(operatorDS.get(j).get("int_operators"), true);
			}
			operatorDropdownCtrl.click(); // need to change focus to change data to first voodoo select control
		}

		// Cancel filter
		sugar().cases.listView.filterCreate.cancel();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1036
		// Verify custom field on listview as header columns
		for (int k = 0; k < fieldsDS.size(); k++) {
			new VoodooControl("span", "css", "th.orderBy" + fieldsDS.get(k).get("module_field_name") + "_c").assertVisible(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
