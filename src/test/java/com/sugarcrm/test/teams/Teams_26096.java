package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26096 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();	
	}

	/**
	 * Edit record-Select only one team and cancel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26096_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts,choose a record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.edit();

		// TODO: VOOD-518, VOOD-1397
		// Add Team and click cancel button.
		new VoodooControl("button","css", "span.fld_team_name.edit button.btn.first").click();
		VoodooUtils.waitForReady();
		String qauser = sugar().users.getQAUser().get("userName");
		new VoodooSelect("div","css", ".fld_team_name.edit div:nth-child(2) div.select2-container.select2.inherit-width").set(qauser);
		VoodooUtils.waitForReady();
		sugar().accounts.recordView.cancel();
		sugar().accounts.recordView.showMore();

		// Verify that team display without modified in Detail view.
		sugar().accounts.recordView.getDetailField("relTeam").assertContains(qauser, false);

		// Verify that team display without modified in Edit view 
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("relTeam").assertContains(qauser, false);

		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}