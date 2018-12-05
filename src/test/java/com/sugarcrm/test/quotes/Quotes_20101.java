package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_20101 extends SugarTest {
	VoodooControl moduleCtrl, layoutSubPanelCtrl, searchBtn, advanceSearchBtn;
	
	public void setup() throws Exception {
		sugar().quotes.api.create();
		sugar().login();
		
		// TODO: VOOD-1509
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Quotes");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		searchBtn = new VoodooControl("td", "id", "searchBtn");
		advanceSearchBtn = new VoodooControl("td", "id", "AdvancedSearchBtn");
		
		// Go to Admin > Studio > Quotes > Layouts > Search > Advanced Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		searchBtn.click();
		VoodooUtils.waitForReady();
		advanceSearchBtn.click();
		VoodooUtils.waitForReady();
		
		// Add Team Field to Advanced Search
		// TODO: VOOD-542
		VoodooControl defaulColoumCtrl = new VoodooControl("li", "css", "#Default ul li:nth-child(5)");
		VoodooControl teamCtrl = new VoodooControl("li", "css", "#Hidden [data-name='team_name']");
		teamCtrl.dragNDrop(defaulColoumCtrl);
		
		// Save and Deploy
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Remove Team text in Advanced Search
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_20101_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource teamData = testData.get(testName);
		
		// Go to Quote module
		sugar().quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on "Advanced Search" link
		sugar().quotes.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();
		
		// Click on arrow in Team field
		// TODO: VOOD-975
		new VoodooControl("button", "css", "#search_form_team_name_advanced_table .button.firstChild").click();
		VoodooUtils.focusWindow(1);
		
		// Verify that 3 teams are displaying (West, East, Global) and select teams
		// TODO: VOOD-975
		for (int i = 0; i < 3; i++) {
			new VoodooControl("a", "css", ".list.view tr:nth-child("+(i+3)+") a").assertContains(teamData.get(i).get("team"), true);
			new VoodooControl("input", "css", ".list.view tr:nth-child("+(i+3)+") td:nth-child(1) input").click();
		}
		
		// TODO: VOOD-975
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on remove "-" for all of the 3 teams and verify that teams are removed
		// TODO: VOOD-975
		for (int i = 0; i < 3; i++) {
			new VoodooControl("button", "id", "remove_team_name_advanced_collection_"+i).click();
			new VoodooControl("table", "id", "search_form_team_name_advanced_table").assertContains(teamData.get(i).get("team"), false);
		}
		VoodooUtils.waitForReady();
		
		// Verify team input box remains without any value
		// TODO: VOOD-975
		new VoodooControl("input", "id", "search_form_team_name_advanced_collection_2").assertContains("", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
