package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26088_notes extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that for sidecar modules - Create record works correctly with multiple teams and remove team
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26088_notes_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Note
		sugar().navbar.selectMenuItem(sugar().notes, "createNote" );
		sugar().notes.createDrawer.showMore();

		// Enter data in fields
		sugar().notes.createDrawer.setFields(sugar().notes.getDefaultData());

		// Add team
		new VoodooControl("button", "css", ".fld_team_name.edit button.btn.first").click();
		// TODO: VOOD-1005
		VoodooSelect teamNameCtrl = new VoodooSelect("a", "css", ".fld_team_name.edit div:nth-child(2) a");
		teamNameCtrl.click();
		teamNameCtrl.clickSearchForMore();

		// Select qauser
		UserRecord qauser = new UserRecord(sugar().users.getQAUser());
		sugar().teams.searchSelect.selectRecord(qauser);
		VoodooUtils.waitForReady();

		// Make it Primary team
		new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) button[name='primary']").click();

		// Add one more team 
		new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) button[name='add']").click();
		VoodooUtils.waitForReady();
		DataSource teamData = testData.get(testName);
		new VoodooSelect("a", "css", "span.fld_team_name.edit div.control-group:nth-of-type(3) div a").set(teamData.get(2).get("team"));

		// Assert - All three teams are available in Edit View
		String teams =  "span.fld_team_name.edit div.control-group:nth-of-type(%d) div span:nth-of-type(1)";
		for (int i = 1 ; i <= teamData.size() ; i++) {
			new VoodooControl("span", "css", String.format(teams, i)).assertContains(teamData.get(i-1).get("team"), true);
		}

		// Remove team Administrator
		new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(3) div button[name='remove']").click();
		VoodooUtils.waitForReady();

		// Verify that selected team is removed in edit view.
		new VoodooControl("span", "css", String.format(teams, 3)).assertExists(false);

		// Save
		sugar().notes.createDrawer.save();

		// Go to detail view
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.showMore(); 

		// Verify teams in detail view
		String teamsDetailSelector = "span.fld_team_name.detail div:nth-child(%d)";
		for (int i = 1 ; i < teamData.size() ; i++) {
			new VoodooControl("span", "css", String.format(teamsDetailSelector, i)).assertContains(teamData.get(i-1).get("team"), true);
		}

		// Verify that removed teams doesn't display in detail view.
		new VoodooControl("span", "css", String.format(teamsDetailSelector, 3)).assertExists(false);

		// Verify that primary team exists detail view
		new VoodooControl("span", "css", String.format(teamsDetailSelector, 2)).assertContains(teamData.get(1).get("label"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}