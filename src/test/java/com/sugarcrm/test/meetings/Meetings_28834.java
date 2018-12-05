package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_28834 extends SugarTest {
	DataSource meetingData = new DataSource();

	public void setup() throws Exception {
		// Creating Meetings records
		meetingData = testData.get(testName);
		sugar().meetings.api.create(meetingData);
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

		// Moving Description Field from Hidden to Default
		VoodooControl descriptionInHidden = new VoodooControl("li", "css", "#Hidden li[data-name='description']");
		VoodooControl addToDefaultList = new VoodooControl("td", "css", "#editor-content #Default");
		descriptionInHidden.scrollIntoViewIfNeeded(false);
		descriptionInHidden.dragNDrop(addToDefaultList);
		new VoodooControl("input", "css", ".list-editor #savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Search filter is working using condition "exactly matches" in the Description field 
	 * @throws Exception
	 */
	@Test
	public void Meetings_28834_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a filter 
		sugar().meetings.navToListView();
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();

		// Creating filter that exactly matches Description field
		// TODO: VOOD-1489 - Need Library Support for All fields moved from Hidden to Default & vice versa for All Modules
		FieldSet filterData = testData.get(testName+"_filterData").get(0);
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("filterOperator"));
		new VoodooControl("textarea", "css", ".detail.fld_description textarea").set(meetingData.get(0).get("description"));
		VoodooUtils.waitForReady();

		// Verifying that Meetings matching exact Description is searched/listed
		sugar().meetings.listView.sortBy("headerName", true);
		sugar().meetings.listView.verifyField(1, "name", meetingData.get(0).get("name"));
		sugar().meetings.listView.verifyField(2, "name", meetingData.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}