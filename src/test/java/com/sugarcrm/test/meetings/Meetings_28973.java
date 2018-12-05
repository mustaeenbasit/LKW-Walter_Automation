package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_28973 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Navigating to Studio Panel
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// Navigating To Meetings module in Studio
		// TODO: VOOD-542 - Need lib support for studio
		new VoodooControl("a", "id", "studiolink_Meetings").click();
		VoodooUtils.waitForReady();

		// Navigating to Search Layout panel of Meetings module
		// TODO: VOOD-1509 - Support Studio Module Search Layout View
		// TODO: VOOD-1510 - Support Studio Module Search View
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();

		// Adding the 'Related to' field to default column in search layout
		VoodooControl defaultFieldsColumn = new VoodooControl("td", "css", "#editor-content #Default");
		VoodooControl hiddenFieldsColumn = new VoodooControl("li", "css", "#Hidden li[data-name='parent_name']");
		hiddenFieldsColumn.dragNDrop(defaultFieldsColumn);
		new VoodooControl("input", "css", ".list-editor #savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}
	/**
	 * Verify select field appears when user chooses "Related to" field in the create filter section.
	 * @throws Exception
	 */
	@Test
	public void Meetings_28973_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Meetings list view
		sugar().meetings.navToListView();

		// Create a filter.
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();
		FieldSet filterData = testData.get(testName).get(0);

		// Setting-Up filter in list view of Meetings module
		// TODO: VOOD-1489 - Need Library Support for All fields moved from Hidden to Default & vice versa for All Modules
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterValue"));
		VoodooControl relatedModulesCtrl = new VoodooControl("span", "css", ".flex-relate-module span");
		relatedModulesCtrl.assertExists(true);
		relatedModulesCtrl.assertEquals(filterData.get("relatedModuleValue"), true);
		new VoodooControl("div", "css", ".flex-relate-record div").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}