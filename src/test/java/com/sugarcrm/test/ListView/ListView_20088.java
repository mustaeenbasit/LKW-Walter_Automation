package com.sugarcrm.test.ListView;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20088 extends SugarTest {
	DataSource accountRecords,intValues;
	FieldSet filterData;
	VoodooControl accountCtrl,customIntCtrl;

	public void setup() throws Exception {
		filterData = testData.get(testName+"_filter").get(0);
		accountRecords = testData.get(testName);
		sugar.accounts.api.create(accountRecords);
		sugar.login();

		// TODO: VOOD-938
		accountCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl fieldLayoutCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "[name='addfieldbtn']");
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveButtonCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		VoodooControl recordViewLayoutCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl listViewLayoutCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		VoodooControl layoutCtrl =  new VoodooControl("td", "id", "layoutsBtn");
		customIntCtrl = new VoodooControl("input", "css", "[name='"+filterData.get("fieldName")+"_c']");

		// Navigate to Admin > studio > Accounts > Fields > Add a custom field type: integer
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountCtrl.click();
		VoodooUtils.waitForReady();
		fieldLayoutCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeDropdownCtrl.set(filterData.get("dataType"));
		VoodooUtils.waitForReady();
		nameFieldCtrl.set(filterData.get("fieldName"));
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate back to Studio (Footer Pane) >  Accounts > Layout
		sugar.admin.studio.clickStudio();
		accountCtrl.click();
		layoutCtrl.click();

		// Adding the above created custom integer field to record view layout
		recordViewLayoutCtrl.click();
		VoodooControl defaultFieldsColumn = new VoodooControl("li", "css", "[data-name='phone_fax']");
		String customIntHook = String.format("div[data-name=%s_c]",filterData.get("fieldName")); 
		new VoodooControl("li", "css", customIntHook).dragNDrop(defaultFieldsColumn);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// Navigate back to Studio (Footer Pane) >  Accounts > Layout
		sugar.admin.studio.clickStudio();
		accountCtrl.click();
		layoutCtrl.click();

		// Adding the above created custom integer field to list view layout
		listViewLayoutCtrl.click();
		defaultFieldsColumn = new VoodooControl("li", "css", "[data-name='phone_office']");
		customIntHook = String.format("li[data-name=%s_c]",filterData.get("fieldName"));
		new VoodooControl("li", "css", customIntHook).dragNDrop(defaultFieldsColumn);
		new VoodooControl("input", "id", "savebtn").click();

		// Navigate back to Studio (Footer Pane) >  Accounts > Layout
		sugar.admin.studio.clickStudio();
		accountCtrl.click();
		layoutCtrl.click();

		// Adding the above created custom integer field to search layout
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", customIntHook).dragNDrop(defaultFieldsColumn);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Updating the records in Accounts module with test data for custom integer field
		sugar.accounts.navToListView();
		intValues = testData.get(testName+"_int_values");
		sugar.accounts.listView.clickRecord(1);
		int count = intValues.size();
		for (int i = 0; i < count; i++) {
			sugar.accounts.recordView.edit();
			sugar.accounts.recordView.showMore();

			// TODO: VOOD-1036
			customIntCtrl.set(intValues.get(i).get("intTestValues"));
			sugar.accounts.recordView.save();
			sugar.accounts.recordView.gotoNextRecord();
		}
	}
	/**
	 * Account Basic Search-Searching SQL uses "like" instead of "equal" when using int type custom field
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_20088_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to List View and opening filter window 
		sugar.accounts.navToListView();
		sugar.accounts.listView.openFilterDropdown();
		sugar.accounts.listView.selectFilterCreateNew();

		// Creating filter for Custom Integer Field
		// TODO: VOOD-1462		
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("fieldLabel"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("fieldOperator"));
		VoodooUtils.waitForReady();
		customIntCtrl.set(filterData.get("filterValue"));
		VoodooUtils.waitForReady();

		// Asserting the number of record count in list view and value of custom integer field for the record in list view 
		// TODO: VOOD-1036	
		Assert.assertTrue("Number of records not equals to ONE", sugar.accounts.listView.countRows() == 1);
		new VoodooControl("div", "css", ".fld_"+filterData.get("fieldName")+"_c div").assertEquals(intValues.get(0).get("intTestValues"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}