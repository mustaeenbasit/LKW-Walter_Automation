package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26076 extends SugarTest {
	DataSource documentData = new DataSource();
	VoodooControl studioCtrl, moduleCtrl, searchBtnCtrl, searchCtrl;

	public void setup() throws Exception {
		documentData = testData.get(testName + "_documents");

		// Create Document records 
		sugar().documents.api.create(documentData);
		sugar().login();

		// Admin -> Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Admin -> Studio -> Documents
		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Documents");
		moduleCtrl.click();
		VoodooUtils.waitForReady();

		// Select Documents -> layouts -> search -> advanced search 
		// TODO: VOOD-1510
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "AdvancedSearchBtn").click();
		VoodooUtils.waitForReady();

		//  Drag teams field from Hidden panel to Default panel 
		VoodooControl dropCtrl = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden [data-name='team_name']").dragNDrop(dropCtrl);
		VoodooUtils.waitForReady();

		// Save & Deploy
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 *  Search records by team id (at least)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26076_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().documents.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// search the record to update team 
		// TODO: VOOD-975
		searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl nameBasic = new VoodooControl("input", "id", "document_name_basic");
		nameBasic.set(documentData.get(0).get("documentName"));
		searchBtnCtrl.click();
		VoodooUtils.focusDefault();

		// update the team for the searched record
		sugar().documents.listView.checkRecord(1);
		sugar().documents.listView.openActionDropdown();
		sugar().documents.listView.massUpdate();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-768, VOOD-1160
		new VoodooControl("button", "css", "#MassUpdate_team_name_table button.button.firstChild").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall").click();
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// clear the search
		nameBasic.set("");
		searchBtnCtrl.click();

		// TODO: VOOD-975
		//  Go to advance search 
		new VoodooControl("a", "id", "advanced_search_link").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1162
		VoodooControl teamCtrl= new VoodooControl("input", "css", "#search_form_team_name_advanced_table .yui-ac-input");
		VoodooControl selectCtrl= new VoodooControl("li", "css", "#search_form_team_name_advanced_table ul li:nth-child(1)");
		VoodooControl searchTypeCtrl = new VoodooControl("input", "css", "#team_name_advanced_type[value='all']");
		searchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		VoodooControl docListCtrl = new VoodooControl("td", "css", "#MassUpdate > table");

		// input team i.e East
		FieldSet teamData = testData.get(testName).get(0);
		teamCtrl.set(teamData.get("teamName"));
		// search and select team 
		selectCtrl.click();
		VoodooUtils.waitForReady();

		// Select a radio button below the team id (i.e. At least)
		searchTypeCtrl.click();

		// click search
		searchCtrl.click();

		// Assert record with searched team id (team1) in the list 
		docListCtrl.assertContains(documentData.get(0).get("documentName"), true);

		// search record for default team i.e Global
		teamCtrl.set(teamData.get("defaultTeam"));
		// select team
		selectCtrl.click();
		VoodooUtils.waitForReady();

		// click the search button
		searchCtrl.click();

		// Assert list record for searched team id 
		for (int i = 0 ; i < documentData.size() ; i++) {
			docListCtrl.assertContains(documentData.get(i).get("documentName"), true);
		}

		// Add Team
		new VoodooControl("button", "css", "#search_form_team_name_advanced_table  button[name='teamAdd']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#search_form_team_name_advanced_table tr:nth-child(3) td .yui-ac-input").set(teamData.get("teamName"));
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#search_form_team_name_advanced_table tbody tr:nth-child(3) ul li:nth-child(1)").click();

		// click the search button
		searchCtrl.click();

		// Assert record with searched teams i.e. Global, East in the list 
		docListCtrl.assertContains(documentData.get(0).get("documentName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}