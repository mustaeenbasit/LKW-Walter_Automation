package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_create extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	@Test 
	public void User_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		UserRecord demoUser = (UserRecord)sugar().users.create();
		demoUser.verify();
 
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 
