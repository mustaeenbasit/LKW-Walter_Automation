package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TeamRecord;
import com.sugarcrm.test.SugarTest;

import static org.junit.Assert.assertEquals;

public class Teams_delete extends SugarTest {
	TeamRecord myTeam;

	public void setup() throws Exception {
		myTeam = (TeamRecord)sugar().teams.api.create();
		sugar().login();
	}

	@Test
	public void Teams_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the team using the UI.
		myTeam.delete();

		// Verify the team was deleted.
		assertEquals(VoodooUtils.contains(myTeam.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}