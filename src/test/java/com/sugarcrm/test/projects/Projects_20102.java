package com.sugarcrm.test.projects;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_20102 extends SugarTest {
	DataSource customDS = new DataSource();
	VoodooControl studioCtrl, studioProjectCtrl, projectLayoutCtrl, projectSearchCtrl, projectAdvSearchCtrl, projectSaveBtnCtrl;

	public void setup() throws Exception {
		sugar().projects.api.create();
		sugar().login();

		// TODO: VOOD-542
		// Go to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Go to Projects > Layouts > Search > Advanced Search
		studioProjectCtrl = new VoodooControl("a", "id", "studiolink_Project");
		studioProjectCtrl.click();
		VoodooUtils.waitForReady();
		projectLayoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		projectLayoutCtrl.click();
		VoodooUtils.waitForReady();
		projectSearchCtrl = new VoodooControl("td", "id", "searchBtn");
		projectSearchCtrl.click();
		VoodooUtils.waitForReady();
		projectAdvSearchCtrl = new VoodooControl("td", "id", "AdvancedSearchBtn");
		projectAdvSearchCtrl.click();
		VoodooUtils.waitForReady();

		// Drag and drop
		VoodooControl dragTeamFieldCtrl = new VoodooControl("li", "css", "#Hidden [data-name='team_name']");
		VoodooControl dropTeamFieldCtrl = new VoodooControl("ul", "css", "#Default ul");
		dragTeamFieldCtrl.dragNDrop(dropTeamFieldCtrl);
		projectSaveBtnCtrl = new VoodooControl("input", "css", "input[name='savebtn']");
		projectSaveBtnCtrl.click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Enable Projects module from Admin > Display modules and subpanels
		sugar().admin.enableModuleDisplayViaJs(sugar().projects);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().projects);
	}

	/**
	 * Allow to Clear all values in Team fields in Advanced Search in BWC modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Projects_20102_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Go to calls listView
		sugar().projects.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-975
		new VoodooControl("a", "id", "advanced_search_link").click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", "#search_form_team_name_advanced_table [name='teamSelect']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		
		// click on arrow in Team field, select multiple teams, e.g. West, East, Global
		for(int i = 0; i < 3; i++)
			new VoodooControl("input", "css", "#MassUpdate > table.list.view tr:nth-child("+(i+3)+") input[name='mass[]']").click();
		
		// Select checked teams
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Check radio button of East as "Primary"
		VoodooControl teamsRadioBtnCtrl = new VoodooControl("input", "css", "#search_form_team_name_advanced_table tr:nth-child(3) td:nth-of-type(3) input");
		teamsRadioBtnCtrl.click();
		
		// Click on remove "-" for East team
		new VoodooControl("td", "css", "#search_form_team_name_advanced_table tr:nth-child(3) td:nth-of-type(2)").click();
		VoodooUtils.waitForReady();
		
		// Verify that East is removed
		new VoodooControl("input", "css", "#search_form_team_name_advanced_table tr:nth-child(3) input").assertContains("East", false);
		
		// check radio button of West as "Primary"
		teamsRadioBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that West is checked as "Primary" team
		new VoodooControl("input", "css", "#search_form_team_name_advanced_table tr:nth-child(3) td:nth-of-type(3) input").assertChecked(true);
		
		// Click on "Clear" button
		new VoodooControl("input", "id", "search_form_clear_advanced").click();
		VoodooUtils.waitForReady();
		
		// Verify that West and Global are cleared, leave team fields blank
		VoodooControl teamsNameDisplayCtrl = new VoodooControl("input", "css", "#search_form_team_name_advanced_table tr:nth-child(2) #search_form_team_name_advanced_collection_0");
		teamsNameDisplayCtrl.assertContains("East", false);
		teamsNameDisplayCtrl.assertContains("West", false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}