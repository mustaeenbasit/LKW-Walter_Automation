package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20080 extends SugarTest {
	DataSource rangeSearchValues = new DataSource();
	FieldSet filterData = new FieldSet();
	VoodooControl opportunitiesCtrl;

	public void setup() throws Exception {
		filterData = testData.get(testName+"_filter").get(0);
		rangeSearchValues = testData.get(testName);
		sugar().login();

		// TODO: VOOD-938
		opportunitiesCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		VoodooControl fieldLayoutCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "[name='addfieldbtn']");
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl enableRangeCtrl = new VoodooControl("input", "css", "input[name='enable_range_search']");
		VoodooControl saveButtonCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		VoodooControl layoutCtrl =  new VoodooControl("td", "id", "layoutsBtn");

		// Navigate to Admin > studio > Opportunities > Fields > Add a custom field type: datetime and enabling range search 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		opportunitiesCtrl.click();
		VoodooUtils.waitForReady();
		fieldLayoutCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeDropdownCtrl.set(filterData.get("dataType"));
		VoodooUtils.waitForReady();
		nameFieldCtrl.set(filterData.get("fieldName"));
		enableRangeCtrl.set(Boolean.toString(true));
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate back to Studio (Footer Pane) >  Opportunities > Layout
		sugar().admin.studio.clickStudio();
		opportunitiesCtrl.click();
		layoutCtrl.click();

		// Adding the above created custom datetime field to search layout
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultFieldsColumn = new VoodooControl("li", "css", "[data-name='amount']");
		String customDateTime = String.format("li[data-name=%s_c]",filterData.get("fieldName")); 
		new VoodooControl("li", "css", customDateTime).dragNDrop(defaultFieldsColumn);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}
	/**
	 * Verify the set of range search operator values for datetime type custom field 
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_20080_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities > List view, open create filter window 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();

		// TODO: VOOD-1462 - Enhance Filter Create to allow use of Custom Fields from Studio	
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("fieldLabel"));
		new VoodooControl("span", "css", ".detail.fld_filter_row_operator").click();
		VoodooSelect operatorValues = new VoodooSelect("div", "css", "[id='select2-drop']");

		// Asserting the range search operator values for datetime type custom field 
		int operatorCount = rangeSearchValues.size();
		for (int i = 0; i < operatorCount; i++) {
			operatorValues.assertContains(rangeSearchValues.get(i).get("fieldOperator"), true);
		}

		// to close operator filter dropdown
		operatorValues.click(); 

		// Close the filter in list view of Opportunities module 
		sugar().opportunities.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}