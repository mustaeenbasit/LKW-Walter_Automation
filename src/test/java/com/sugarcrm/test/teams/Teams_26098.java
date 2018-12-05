package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26098 extends SugarTest {
	String qauser = "";

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();

		// Go to Leads,choose a record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.edit();

		// TODO: VOOD-518, VOOD-1397
		// Add one non-primary team.
		new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first").click();
		VoodooUtils.waitForReady();
		qauser = sugar().users.getQAUser().get("userName");
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container.select2.inherit-width").set(qauser);
		sugar().leads.recordView.save();
	}

	/**
	 * Edit record-Remove all teams and cancel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26098_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit View
		sugar().leads.recordView.edit();

		// In edit view, remove all teams 
		// TODO: VOOD-518, VOOD-1397
		new VoodooControl("button", "css", ".fld_team_name.edit div:nth-child(2) div .second").click();

		// Verify that all teams are removed except primary team.
		VoodooControl secondTeamCtrl = new VoodooControl("span", "css", "span.normal span div:nth-child(2) .select2-chosen");
		FieldSet teamData = testData.get(testName).get(0);
		sugar().leads.recordView.getEditField("relTeam").assertEquals(teamData.get("team1"), true);
		secondTeamCtrl.assertVisible(false);

		// Click "cancel"
		sugar().leads.recordView.cancel();

		// Verify that team display without modified in Detail view.
		sugar().leads.recordView.getDetailField("relTeam").assertEquals(teamData.get("primary_team"), true);
		new VoodooControl("div","css", "span.fld_team_name div:nth-child(2)").assertContains(qauser, true);

		// Edit
		sugar().leads.recordView.edit();

		// Verify that team display without modified in Edit view.
		sugar().leads.recordView.getEditField("relTeam").assertEquals(teamData.get("team1"), true);
		secondTeamCtrl.assertContains(qauser, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}