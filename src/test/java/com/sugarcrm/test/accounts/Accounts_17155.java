package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;

public class Accounts_17155 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Team set should be reverted on cancel of detail view inline edit
	 * @throws Exception
	 */	
	@Test
	public void Accounts_17155_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamInfo = testData.get(testName).get(0);
		VoodooControl teamName = sugar().accounts.recordView.getDetailField("relTeam");

		// Navigate to the Accounts module and click name link to open its record view
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();

		// Assert that by default the team is Global
		teamName.assertContains(teamInfo.get("team"), true);

		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		new VoodooControl("span", "css", "span[data-voodoo-name='team_name']").hover();
		new VoodooControl("i", "css", "span[data-name='team_name'] .fa.fa-pencil").click();
		sugar().accounts.recordView.getEditField("relTeam").set(teamInfo.get("team1"));
		sugar().accounts.recordView.cancel();

		// Assert that the team does not change when edit action was canceled
		teamName.assertContains(teamInfo.get("team"), true);
		teamName.assertContains(teamInfo.get("primary"), true);		
		teamName.assertContains(teamInfo.get("team1"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}