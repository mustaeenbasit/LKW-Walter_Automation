package com.sugarcrm.test.tutorial;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class LoginLogout extends SugarTest {
	DataSource accounts, otherAccounts;
	FieldSet thirdAccount;

	public void setup() throws Exception {
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.login();
		sugar.logout();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
