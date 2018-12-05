package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26093_contracts extends SugarTest {
	VoodooControl studioCtrl, contractsCtrl;

	public void setup() throws Exception {
		// Create Account & Contract
		sugar().accounts.api.create();
		sugar().contracts.api.create();

		sugar().login();

		// Enable Contracts Module
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);

		// Enable 'Teams' field in listview as verification is required in listView
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1507
		// Navigate to  Studio > Contracts > Layouts > ListView
		contractsCtrl = new VoodooControl("a", "id", "studiolink_Contracts");
		VoodooControl layoutsCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();
		contractsCtrl.click();
		layoutsCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "li[data-name='team_name']").dragNDrop(defaultSubPanelCtrl);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Edit Contract record, assign multiple teams.		
		sugar().contracts.navToListView();
		sugar().contracts.listView.clickRecord(1);
		sugar().contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("account_name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().contracts.editView.getEditField("teams").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall").click();
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		sugar().contracts.editView.save();
	}

	/**
	 * For BWC Module: Create record-Verify team in list view is the "primary" team in edit view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26093_contracts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contracts.navToListView();
		sugar().contracts.listView.clickRecord(1);

		// Verify that all teams display in detail view.
		VoodooUtils.focusFrame("bwc-frame");
		DataSource teamData = testData.get(testName);
		VoodooControl teamField = sugar().contracts.detailView.getDetailField("teams");
		for (int i = 0 ; i < teamData.size() ; i++) {
			teamField.assertContains(teamData.get(i).get("teamName"), true);
		}
		VoodooUtils.focusDefault();

		// Navigate to Edit view.
		sugar().contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify primary team in EditView.
		// TODO: VOOD-518
		new VoodooControl("input", "css", "#EditView_team_name_table tbody tr:nth-child(2) .yui-ac-input").assertEquals(teamData.get(0).get("teamName"), true);

		// Click on 'Show' to show all the teams.
		// TODO: VOOD-518, VOOD-1397
		VoodooControl showCtrl = new VoodooControl("span", "css", "#more_EditView_team_name span");
		showCtrl.scrollIntoViewIfNeeded(false);
		showCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that all teams display in edit view.
		String s = "#EditView_team_name_table tbody tr:nth-child(%d) td:nth-child(1) span .yui-ac-input";
		for (int i = 0 ; i < teamData.size() ; i++) {
			new VoodooControl("input", "css", String.format(s, i+2)).assertContains(teamData.get(i).get("teamName"), true);
		}

		VoodooUtils.focusDefault();
		// Navigate to contracts list view
		sugar().contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify only the primary team is shown in the list view
		VoodooControl teamListViewCtrl = new VoodooControl("tr", "css", "#MassUpdate tr.oddListRowS1");
		teamListViewCtrl.assertContains(teamData.get(0).get("teamName"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}