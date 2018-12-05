package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26107 extends SugarTest {
	DataSource oppData;

	public void setup() throws Exception {
		oppData = testData.get(testName);
		sugar.accounts.api.create();
		sugar.login();

		// 2 opportunities with different team names
		sugar.opportunities.create(oppData);
	}

	/**
	 * Verify that Merge Duplicates of records assigned to different teams works properly 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26107_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Navigate to opportunities module. Select two opportunities with different teams from the list view.
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.toggleSelectAll();
		sugar.opportunities.listView.openActionDropdown();

		// TODO: VOOD-681
		//  Merge two opportunity
		new VoodooControl("a", "css", ".fld_merge_button.list").click();

		// Earlier only Administrator checkbox was checked but now administrator and qauser both are checked  
		new VoodooControl("input", "css", ".fld_team_name.edit div:nth-child(2) div input").set(Boolean.toString(true));
		new VoodooControl("a", "css", ".fld_save_button.merge-duplicates-headerpane a").click();
		sugar.alerts.getAlert().confirmAlert();

		// Verify that selected teams are display properly on detail page
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);
		sugar.opportunities.recordView.showMore();

		// TODO: VOOD-1160
		VoodooControl adminTeamCtrl = new VoodooControl("div", "css", ".fld_team_name.detail div:nth-child(1)");
		VoodooControl qauserTeamCtrl = new VoodooControl("div", "css", ".fld_team_name.detail div:nth-child(2)");
		qauserTeamCtrl.assertContains(oppData.get(1).get("relTeam"), true);
		adminTeamCtrl.assertContains(oppData.get(0).get("relTeam"), true);
		sugar.logout();

		// Login as QAUser
		sugar.login(sugar.users.getQAUser());

		// Verify that merged opportunity record is visible
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.assertContains(oppData.get(1).get("name"), true);
		sugar.opportunities.listView.clickRecord(1);
		sugar.opportunities.recordView.showMore();

		// Verify that selected teams are display properly on detail page
		qauserTeamCtrl.assertContains(oppData.get(1).get("relTeam"), true);
		adminTeamCtrl.assertContains(oppData.get(0).get("relTeam"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}