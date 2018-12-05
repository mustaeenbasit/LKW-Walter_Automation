package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_18926 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify multiple teams can be added to the module record
	 * @throws Exception
	 */
	@Test
	public void Teams_18926_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Record view of Leads Module
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.recordView.showMore();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1005
		String s1 = ".fld_team_name.edit div.control-group:nth-of-type(%d) .btn.first";
		String s2 = ".fld_team_name.edit div.control-group:nth-of-type(%d) .btn.second";
		VoodooControl firstAddIconCtrl = new VoodooControl("button", "css", String.format(s1, 1));
		VoodooControl firstRemoveIconCtrl = new VoodooControl("button", "css", String.format(s2, 1));
		VoodooControl secondAddIconCtrl = new VoodooControl("button", "css", String.format(s1, 2));
		VoodooControl secondRemoveIconCtrl = new VoodooControl("button", "css", String.format(s2, 2));
		VoodooControl thirdAddIconCtrl = new VoodooControl("button", "css", String.format(s1, 3));
		VoodooControl thirdRemoveIconCtrl = new VoodooControl("button", "css", String.format(s2, 3));
		firstAddIconCtrl.click();
		VoodooUtils.waitForReady();

		// Verify 2nd row is having + & - icon
		firstAddIconCtrl.assertExists(false);
		firstRemoveIconCtrl.assertExists(false);
		secondAddIconCtrl.assertExists(true);
		secondRemoveIconCtrl.assertExists(true);

		// Set team as Administrator
		FieldSet customData = testData.get(testName).get(0);
		// TODO: VOOD-1162
		new VoodooSelect("a", "css", ".fld_team_name.edit div:nth-of-type(2) a").set(customData.get("admin"));
		VoodooUtils.waitForReady();
		secondAddIconCtrl.click();
		VoodooUtils.waitForReady();

		// Verify 3rd row is having + & - icon
		firstAddIconCtrl.assertExists(false);
		firstRemoveIconCtrl.assertExists(true);
		secondAddIconCtrl.assertExists(false);
		secondRemoveIconCtrl.assertExists(true);		
		thirdAddIconCtrl.assertExists(true);
		thirdRemoveIconCtrl.assertExists(true);

		// Search team from select drawer
		new VoodooControl("a", "css", ".fld_team_name.edit div:nth-of-type(3) a").click();
		new VoodooControl("li", "css", "#select2-drop ul:nth-child(3) li").click();
		VoodooUtils.waitForReady();

		// Verify user is on SSV
		sugar().teams.searchSelect.assertVisible(true);
		UserRecord qauser = new UserRecord(sugar().users.getQAUser());
		// Select record from SSV
		sugar().teams.searchSelect.selectRecord(qauser);

		// Verify multiple teams are displayed but only one team can be a Primary Team
		String s = ".fld_team_name.edit div.control-group:nth-of-type(%d) .btn.third";
		for (int i = 1 ; i <= customData.size()+2 ; i++) {
			new VoodooControl("button", "css", String.format(s, i)).assertVisible(true);
		}
		new VoodooControl("button", "css", String.format(s, 1)).assertAttribute("class", "active", true);
		new VoodooControl("button", "css", String.format(s, 2)).assertAttribute("class", "active", false);
		new VoodooControl("button", "css", String.format(s, 3)).assertAttribute("class", "active", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}