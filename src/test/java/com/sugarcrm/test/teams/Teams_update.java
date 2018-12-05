package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TeamRecord;

public class Teams_update extends SugarTest {
	TeamRecord myTeam;

	public void setup() throws Exception {
		myTeam = (TeamRecord)sugar().teams.api.create();
		sugar().login();
	}

	@Test
	public void Teams_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Name edited");
		newData.put("description", "Description edited");

		// Edit the team using the UI.
		myTeam.edit(newData);

		// Verify the team was edited.
		myTeam.verify();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}