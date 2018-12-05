package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TeamRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_create extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Teams_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		TeamRecord myTeam = (TeamRecord)sugar().teams.create();
		myTeam.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}