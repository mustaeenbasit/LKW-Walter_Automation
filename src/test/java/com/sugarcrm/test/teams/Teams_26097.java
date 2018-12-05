package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26097 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();	
	}

	/**
	 * Edit record- Remove all teams and save.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26097_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teams = testData.get(testName).get(0);

		// Go to Contacts,choose a record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.edit();

		// TODO: VOOD-518, VOOD-1397
		// Add one non-primary team.
		new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container").set(sugar().users.getQAUser().get("userName"));
		sugar().contacts.recordView.save();

		// Go to edit view
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.edit();

		// TODO: VOOD-518, VOOD-1397
		// In edit view, remove all teams and save.
		new VoodooControl("button", "css", ".fld_team_name.edit div:nth-child(2) div .second").click();
		sugar().contacts.recordView.save();
		sugar().contacts.recordView.showMore();

		// Verify that all teams are removed except primary team.
		new VoodooSelect("div","css", "span.fld_team_name div.control-group:nth-of-type(2)").assertVisible(false);
		sugar().contacts.recordView.getDetailField("relTeam").assertContains(teams.get("primary_team"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}