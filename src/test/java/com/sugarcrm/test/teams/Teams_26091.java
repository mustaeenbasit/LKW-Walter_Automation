package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26091 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Login as a valid user
		sugar().login();

		// Enable Contracts module 
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);

		// Enable Team field in the Contact's list view
		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_Contracts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "li[data-name=team_name]").dragNDrop(new VoodooControl("td", "id", "Default"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that for BWC modules - During Create record - No "more" double-down arrow displays after multiple teams selected and only one team is left after removing
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26091_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamsData = testData.get(testName).get(0);

		// Go to <contracts>|<create contracts>, enter values
		// TODO: VOOD-444 - Support creating relationships via API
		sugar().navbar.selectMenuItem(sugar().contracts, "createContract" );
		sugar().contracts.editView.setFields(sugar().contracts.getDefaultData());
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("assignedTo").set(teamsData.get("assignedTo"));

		// Define controls for Contracts detail view page
		// TODO: VOOD-1072 - Library support needed for controls in any BWC( i.e. Campaign/Quotes) Detail view.
		VoodooControl showAndHideCtrl = new VoodooControl("span", "id", "more_div_EditView_team_name");
		VoodooControl firstTeamFieldCtrl = new VoodooControl("input", "id", "EditView_team_name_collection_0");
		VoodooControl secondTeamFieldCtrl = new VoodooControl("input", "id", "EditView_team_name_collection_1");
		VoodooControl thirdTeamFieldCtrl = new VoodooControl("input", "id", "EditView_team_name_collection_2");
		VoodooControl hideButtonCtrl = new VoodooControl("span", "css", "span#more_EditView_team_name span");
		VoodooControl firstRemoveBtnCtrl = new VoodooControl("button", "id", "remove_team_name_collection_1");
		VoodooControl secondRemoveBtnCtrl = new VoodooControl("button", "id", "remove_team_name_collection_2");

		// Assert - Hide button is not available
		showAndHideCtrl.assertVisible(false);

		// In "team" field, click "select" or click "add" multiple teams
		// Click on arrow button next to 'Teams' filed
		// TODO: VOOD-1072 - Library support needed for controls in any BWC( i.e. Campaign/Quotes) Detail view.
		new VoodooControl("button", "id", "teamSelect").click();
		VoodooUtils.focusWindow(1);
		// Used xPath so that test will not fail in case we decide to add another team in the future and in case of search and select case, we need to repeat the same process twice. 
		new VoodooControl("input", "xpath", "//tr[contains(.,'qauser')]/td[1]/input").set("true");
		new VoodooControl("input", "xpath", "//tr[contains(.,'Administrator')]/td[1]/input").set("true");
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Assert - Hide button becomes available
		showAndHideCtrl.assertContains(teamsData.get("hide"), true);

		// Assert - All teams are visible
		firstTeamFieldCtrl.assertContains(teamsData.get("global"), true);
		secondTeamFieldCtrl.assertContains(teamsData.get("administrator"), true);
		thirdTeamFieldCtrl.assertContains(teamsData.get("qauser"), true);

		// Now click "Hide"
		hideButtonCtrl.click();

		// Assert - Only Primary team is visible
		firstTeamFieldCtrl.assertContains(teamsData.get("global"), true);
		secondTeamFieldCtrl.assertVisible(false);
		thirdTeamFieldCtrl.assertVisible(false);

		// Save the record
		VoodooUtils.focusDefault();
		sugar().contracts.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that selected teams appears
		VoodooControl teamFieldCtrl = sugar().contracts.detailView.getDetailField("teams");
		teamFieldCtrl.assertContains(teamsData.get("global"), true);
		teamFieldCtrl.assertContains(teamsData.get("administrator"), true);
		teamFieldCtrl.assertContains(teamsData.get("qauser"), true);
		VoodooUtils.focusDefault();

		// Navigate to Contracts List view
		sugar().contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1489 - Need Library Support for All fields moved from Hidden to Default & vice versa for All Modules
		VoodooControl firstListRecordCtrl = new VoodooControl("tr", "css", ".oddListRowS1");

		// Verify that only one team displays in list view
		firstListRecordCtrl.assertContains(teamsData.get("global"), true);
		firstListRecordCtrl.assertContains(teamsData.get("administrator"), false);
		firstListRecordCtrl.assertContains(teamsData.get("qauser"), false);
		VoodooUtils.focusDefault();

		// Navigate to Contracts record and Edit Contracts record
		sugar().contracts.listView.clickRecord(1);
		sugar().contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Click the remove team button and only leave one team
		showAndHideCtrl.click();
		VoodooUtils.waitForReady();
		secondRemoveBtnCtrl.click();
		firstRemoveBtnCtrl.click();

		// Save the record
		VoodooUtils.focusDefault();
		sugar().contracts.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that only one team displays in detail view.
		teamFieldCtrl.assertContains(teamsData.get("global"), true);
		teamFieldCtrl.assertContains(teamsData.get("administrator"), false);
		teamFieldCtrl.assertContains(teamsData.get("qauser"), false);
		VoodooUtils.focusDefault();

		// Navigate to Contracts List view
		sugar().contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that only one team displays in list view
		firstListRecordCtrl.assertContains(teamsData.get("global"), true);
		firstListRecordCtrl.assertContains(teamsData.get("administrator"), false);
		firstListRecordCtrl.assertContains(teamsData.get("qauser"), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}