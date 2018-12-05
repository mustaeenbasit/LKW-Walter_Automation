package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TeamRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23412 extends SugarTest {
	TeamRecord myTeamRecord;

	public void setup() throws Exception {
		// create two cases record with different name
		sugar().cases.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().cases.api.create(fs);

		// Logon as Admin
		sugar().login();
	}

	/**
	 * Sort List_Verify that teams can be sorted by column titles in "Teams" pop up window in "Mass Update" panel of case list view.
	 * @throws Exception
	 */
	@Test
	public void Cases_23412_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Cases" tab in navigation bar and go to a case record view.
		sugar().cases.navToListView();
		sugar().cases.listView.toggleSelectAll();
		sugar().cases.listView.openActionDropdown();
		sugar().cases.listView.massUpdate();

		FieldSet customDate = testData.get(testName).get(0);

		// TODO: VOOD-1162
		new VoodooSelect("div", "css", "[data-voodoo-name='massupdate'] div.filter-body.clearfix div div.filter-field.controls.span4 div").set(sugar().teams.moduleNamePlural);

		new VoodooControl("a", "css", "[data-voodoo-name='team_name'] > div > div.span9 > div > div > div > a").click();
		new VoodooControl("div", "css", "#select2-drop > ul:nth-child(3) > li > div").click();
		VoodooUtils.waitForReady();

		// Verify that Team Drawer is opened
		new VoodooControl("span", "css", "#drawers [data-voodoo-name='title']").assertContains(customDate.get("teamDrawer"), true);

		// Click on header Title "Team Name"
		VoodooControl shortByName = new VoodooControl("th", "css", ".dataTable.search-and-select > thead > tr:nth-child(1) > th.orderByname");
		shortByName.click();
		VoodooUtils.waitForReady();

		// Verify that teams are sorted according to the column titles in the drawer.
		new VoodooControl("div", "css", ".layout_Teams table tbody tr:nth-child(1) td:nth-child(2) span div").assertContains(customDate.get("teamName2"), true);

		shortByName.click();
		VoodooUtils.waitForReady();

		// Verify that teams are sorted according to the column titles in the drawer.
		new VoodooControl("div", "css", ".layout_Teams table tbody tr:nth-child(1) td:nth-child(2) span div").assertContains(customDate.get("teamName1"), true);

		// Cancel Team Drawer
		new VoodooControl("a", "css", "a[name='close']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
