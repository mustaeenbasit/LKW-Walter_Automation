package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_26109 extends SugarTest {
	DataSource userDataSet;
	UserRecord user1;
	
	public void setup() throws Exception {
		userDataSet = testData.get(testName);
		sugar.login();
	}

	/**
	 * Merge Duplicates in detail view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26109_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// Create users		
		user1 = (UserRecord)sugar.users.create(userDataSet.get(0));
		sugar.logout();
		VoodooUtils.focusDefault();
		
		sugar.login(user1);
		sugar.alerts.waitForLoadingExpiration();
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "tab2").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that global team in default team field
		new VoodooControl("td", "css", "#settings > table > tbody > tr:nth-child(3) > td:nth-child(2)").assertContains("Global", true);
		VoodooUtils.focusDefault();
		sugar.logout();
		sugar.login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}