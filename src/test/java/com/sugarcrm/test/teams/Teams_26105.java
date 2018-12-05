package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26105 extends SugarTest {
	VoodooControl bugsCtrl;

	public void setup() throws Exception {
		DataSource bugsData = testData.get(testName+"_bugsData");

		// Create Bugs records
		sugar().bugs.api.create(bugsData);

		sugar().login();

		// Enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Moving Teams field to default so that it is visible in list view.
		// As verification is required in list view.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1507
		// Studio > Bugs > Layouts > ListView
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		bugsCtrl = new VoodooControl("a", "id", "studiolink_Bugs");
		bugsCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click();
		VoodooUtils.waitForReady();
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='team_name']").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Duplicate record with one team
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26105_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);
		sugar().bugs.recordView.openPrimaryButtonDropdown();

		// Click on "Find Duplicates"
		// TODO: VOOD-691
		new VoodooControl("a", "css", "[name='find_duplicates_button']").click();
		VoodooUtils.waitForReady();

		// Merge Duplicates
		// TODO: VOOD-681 -Create a Lib for Merge Duplicates
		new VoodooControl("input", "css", ".btn.checkall .toggle-all").click();
		new VoodooControl("a", "css", ".fld_merge_duplicates_button").click();
		sugar().alerts.confirmAllWarning();

		// Add 1 more team
		sugar().bugs.recordView.edit();
		sugar().bugs.recordView.showMore();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1161 
		new VoodooControl("button", "css", ".fld_team_name.edit button.btn.first").click();
		FieldSet customData = testData.get(testName).get(0);
		new VoodooSelect("a", "css", ".fld_team_name.edit div:nth-child(2) a").set(customData.get("team"));

		// Save
		sugar().bugs.recordView.save();

		// Verify that selected teams are display properly on detail page
		VoodooControl teamNameDetailCtrl = new VoodooControl("span", "css", ".fld_team_name[data-voodoo-name='team_name']");
		teamNameDetailCtrl.assertContains(customData.get("team"), true);
		teamNameDetailCtrl.assertContains(customData.get("defaultTeam"), true);

		sugar().bugs.recordView.edit();
		// Verify that multiple team displays on Edit view
		VoodooControl teamNameEditCtrl = new VoodooControl("span", "css", ".fld_team_name.edit");
		teamNameEditCtrl.assertContains(customData.get("team"), true);
		teamNameEditCtrl.assertContains(customData.get("defaultTeam"), true);
		sugar().bugs.recordView.cancel();

		sugar().bugs.navToListView();
		// Verify that primary team displays on list view.
		new VoodooControl("span", "css", ".list.fld_team_name").assertEquals(customData.get("defaultTeam"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}