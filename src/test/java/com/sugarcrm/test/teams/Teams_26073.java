package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26073 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify the "add team (+)" button of team id mass update(only update one record)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26073_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamsFS = testData.get(testName).get(0);

		// Create one record that has one or more team id
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.edit();
		sugar.accounts.recordView.showMore();

		// TODO VOOD-518
		// Add Team
		new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first").click();
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container.select2.inherit-width").set(teamsFS.get("team2"));
		sugar.accounts.recordView.save();

		sugar.accounts.navToListView();
		sugar.alerts.waitForLoadingExpiration();

		// Mass update the team of the record
		FieldSet myMassUpdate = new FieldSet();
		myMassUpdate.put(teamsFS.get("selectTeam"),teamsFS.get("team1"));
		sugar.accounts.listView.checkRecord(1);
		sugar.accounts.massUpdate.performMassUpdate(myMassUpdate);
		sugar.alerts.waitForLoadingExpiration();
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// TODO: VOOD-518, VOOD-1217
		// Verify the team id that input in mass update panel is added to the account, if have duplicated team id, then only display once
		new VoodooControl("span","css",".fld_team_name.detail").assertContains(teamsFS.get("team2"), true);

		// Verify the primary team should change accordingly if set some one as primary team in the mass update subpanel, if not then should keep the old value
		new VoodooControl("div","xpath", "//span[contains(@class,'fld_team_name')]//label/..").assertEquals(teamsFS.get("assert"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}