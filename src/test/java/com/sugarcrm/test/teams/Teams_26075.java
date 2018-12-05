package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26075  extends SugarTest {
	DataSource ds = new DataSource();
	DataSource teamData = new DataSource();
	VoodooControl moduleCtrl, layoutCtrl, searchBtnCtrl, advanceSearchCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		ds = testData.get(testName);
		teamData = testData.get(testName + "_1");
		sugar.contracts.api.create(ds);
		sugar.login();

		// enable contracts module
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		// Define studio controls
		// TODO VOOD-517, VOOD-542, VOOD-1510
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Contracts");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		searchBtnCtrl = new VoodooControl("td", "id", "searchBtn");
		advanceSearchCtrl = new VoodooControl("td", "id", "AdvancedSearchBtn");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");

		// Admin -> Studio -> Contracts
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();

		// Select Contracts -> layouts -> search -> advanced search 
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();
		advanceSearchCtrl.click();
		VoodooUtils.waitForReady();

		//  Drag teams field from Hidden panel to Default panel 
		new VoodooControl("li", "css", "#Hidden [data-name='team_name']").dragNDrop(new VoodooControl("td", "id", "Default"));
		VoodooUtils.waitForReady();

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Search record by team id(any) 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26075_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.contracts.navToListView();
		sugar.contracts.listView.toggleSelectAll();
		sugar.contracts.listView.openActionDropdown();

		// mass update Contracts records teams 
		sugar.contracts.listView.massUpdate();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-768, VOOD-1160
		new VoodooControl("button", "css", "#MassUpdate_team_name_table button.button.firstChild").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall").click();
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();

		//  Go to advance search 
		new VoodooControl("a", "id", "advanced_search_link").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1162, VOOD-975
		VoodooControl teamCtrl= new VoodooControl("input", "css", "#search_form_team_name_advanced_table [name='teamset_div'] input[type='text']");
		VoodooControl selectCtrl= new VoodooControl("li", "css", "#search_form_search_form_team_name_advanced_collection_0_results li:nth-child(1)");
		VoodooControl searchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		VoodooControl tableRecordCtrl = new VoodooControl("td", "css", "#MassUpdate > table");

		// input teams id (eg. qauser)
		teamCtrl.set(teamData.get(0).get("teamName"));

		// search and select team id
		selectCtrl.click();
		VoodooUtils.waitForReady();

		// Select a radio button below the team id (eg. Any)
		new VoodooControl("input", "css", "#team_name_advanced_type[value='any']").click();
		searchCtrl.click();
		VoodooUtils.waitForReady();

		// Assert list record for searched team id
		tableRecordCtrl.assertContains(ds.get(0).get("name"), true);
		tableRecordCtrl.assertContains(ds.get(0).get("name"), true);

		// input teams id(eg. Administrator)
		teamCtrl.set(teamData.get(1).get("teamName"));
		// select teams id
		selectCtrl.click();
		VoodooUtils.waitForReady();

		// Select a radio button below the team id (eg. all)
		new VoodooControl("input", "css", "#team_name_advanced_type[value='all']").click();
		// click the search button
		searchCtrl.click();
		VoodooUtils.waitForReady();

		// Assert list record for searched team id
		tableRecordCtrl.assertContains(ds.get(0).get("name"), true);
		tableRecordCtrl.assertContains(ds.get(1).get("name"), true);
		teamCtrl.set(teamData.get(2).get("teamName"));

		//  select teams id
		selectCtrl.click();
		VoodooUtils.pause(200); // small pause required to select team id

		// Select a radio button below the team id (eg. exact)
		new VoodooControl("input", "css", "#team_name_advanced_type[value='exact']").click();
		// click the search button
		searchCtrl.click();
		VoodooUtils.waitForReady();

		// Assert list record not having the exact matching record
		new VoodooControl("div", "css", "#MassUpdate > div.list.view.listViewEmpty").assertContains("No results found", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}