package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_28072 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord) sugar.users.api.create();
		sugar.login();	
	}

	/**
	 * Verify the team's full name in "Search and Select team" drawer
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_28072_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts and Create a new record.
		sugar.accounts.navToListView();
		sugar.accounts.listView.create();
		sugar.accounts.createDrawer.showMore();

		// Go to the "Teams" field
		sugar.accounts.createDrawer.getEditField("relTeam").click();

		// TODO: VOOD-1162, VOOD-795
		// Select link labeled 'Search and Select'
		//new VoodooControl("span", "css",".fld_team_name.edit span").click();
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']//div[text()='Search and Select...']").click();
		VoodooUtils.waitForReady();

		// Verify that the Team's full name i.e. Chris Oliver is available in the List. Also verify all teams are displayed
		new VoodooControl("tr", "xpath", "//tr[contains(.,'"+sugar.users.getDefaultData().get("fullName")+"')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'qauser')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Administrator')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Global')]").assertVisible(true);

		// Cancel the "Search and Select Teams" view
		new VoodooControl("a", "css", ".fld_close.selection-headerpane a").click();

		// Cancel the Account record
		sugar.accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}