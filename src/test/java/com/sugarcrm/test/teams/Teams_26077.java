package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26077 extends SugarTest {

	DataSource ds;
	FieldSet fs;
	VoodooControl studioCtrl,moduleCtrl, layoutCtrl, searchBtnCtrl,
	advanceSearchCtrl, saveBtnCtrl, basicSearch ;

	public void setup() throws Exception {

		ds = testData.get(testName + "_documents");
		fs = testData.get(testName).get(0);
		sugar.login();

		// create document records 
		sugar.documents.api.create(ds);

		//  Admin -> Studio
		sugar.navbar.navToAdminTools();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-517 Create Studio Module (BWC) - will provide the
		// references to replace these explicit VoodooControls
		studioCtrl = new VoodooControl("a", "id", "studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Admin -> Studio -> Documents
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Documents");
		moduleCtrl.click();
		VoodooUtils.waitForReady();

		// Select Documents -> layouts -> search -> advanced search 
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();

		searchBtnCtrl = new VoodooControl("td", "id", "searchBtn");
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();

		advanceSearchCtrl = new VoodooControl("td", "id", "AdvancedSearchBtn");
		advanceSearchCtrl.click();
		VoodooUtils.waitForReady();

		//  Drag teams field from Hidden panel to Default panel 
		VoodooControl itemToDrag = new VoodooControl("li", "css", "#Hidden [data-name='team_name']");
		VoodooControl dropCtrl = new VoodooControl("td", "id", "Default");
		itemToDrag.dragNDrop(dropCtrl);

		// Save & Deploy
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// create a new team 
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1011
		sugar.admin.adminTools.getControl("teamsManagement").click();

		// TODO VOOD-1011
		new VoodooControl("button", "css", "#header div.module-list li.dropdown.active button").click();
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_TEAM']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "#contentTable input[type='text']").set(fs.get("teamName"));
		new VoodooControl("input", "id", "btn_save").click();
		VoodooUtils.focusDefault();
	}

	/**
	 *  Search records by team id(exact)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26077_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		sugar.documents.navToListView();
		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.focusFrame("bwc-frame");
		// search the record to update team 
		basicSearch = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl nameBasic = new VoodooControl("input", "id", "document_name_basic");
		nameBasic.set(ds.get(0).get("documentName"));
		basicSearch.click();
		VoodooUtils.focusDefault();

		// update the team for two records
		sugar.documents.listView.checkRecord(1);
		sugar.documents.listView.openActionDropdown();
		sugar.documents.listView.massUpdate();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-768, VOOD-1160		
		new VoodooControl("input", "id", "MassUpdate_team_name_collection_0").set(fs.get("teamName"));
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "div#MassUpdate_MassUpdate_team_name_collection_0_results ul li:nth-of-type(1)").click();
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();

		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// clear the search
		nameBasic.set("");
		basicSearch.click();
		// TODO: VOOD-975
		//  Go to advance search 
		new VoodooControl("a", "id", "advanced_search_link").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1162
		VoodooControl teamCtrl= new VoodooControl("input", "id", "search_form_team_name_advanced_collection_0");
		VoodooControl selectCtrl= new VoodooControl("li", "css", "#search_form_search_form_team_name_advanced_collection_0_results li:nth-child(1)");
		VoodooControl searchTypeCtrl = new VoodooControl("input", "css", "#team_name_advanced_type[value='exact']");
		VoodooControl searchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		VoodooControl listRowCtrl = new VoodooControl("td", "css", "#MassUpdate > table");

		// input teams id (eg. team1)
		teamCtrl.set(fs.get("teamName"));

		// search and select team id
		selectCtrl.click();
		VoodooUtils.pause(200); //small pause required to check checkbox

		// Select a radio button below the team id (i.e. At least)
		searchTypeCtrl.click();

		// click search
		searchCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("div", "css", "#MassUpdate > div.list.view.listViewEmpty").assertContains(fs.get("assert_string"), true);

		// search record for default team i.e Global
		teamCtrl.set(fs.get("defaultTeam"));
		// select teams id
		selectCtrl.click();
		VoodooUtils.pause(200); // small pause required to select team id

		// Select a radio button below the team id (eg. Exact)
		searchTypeCtrl.click();

		// click search
		searchCtrl.click();
		sugar.alerts.waitForLoadingExpiration();

		// Assert list record for searched team id 
		listRowCtrl.assertContains(ds.get(1).get("documentName"), true);
		listRowCtrl.assertContains(ds.get(2).get("documentName"), true);

		new VoodooControl("button", "css", "#search_form_team_name_advanced_table  button[name='teamAdd']").click();
		VoodooUtils.pause(300); // small pause required for add new field

		// TODO: VOOD-892
		// search record for two  teams i.e Global, team1
		teamCtrl.set(fs.get("teamName"));

		// select teams id
		selectCtrl.click();
		VoodooUtils.pause(200); // small pause required to select team id

		// set Global team for search
		new VoodooControl("input", "id", "search_form_team_name_advanced_collection_1").set(fs.get("defaultTeam"));
		VoodooUtils.pause(200);

		// select teams id
		new VoodooControl("li", "css", "#search_form_search_form_team_name_advanced_collection_1_results li:nth-child(1)").click();
		searchTypeCtrl.waitForVisible(); // small pause required to select team id

		// Select a radio button below the team id (eg. Exact)
		searchTypeCtrl.click();

		// click search
		searchCtrl.click();
		sugar.alerts.waitForLoadingExpiration();

		// Assert record with searched team ids i.e. Global, team1 in the list 
		listRowCtrl.assertContains(ds.get(0).get("documentName"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}