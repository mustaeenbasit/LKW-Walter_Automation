package com.sugarcrm.test.grimoire;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.PortalTest;

public class PortalRecordCreateTest extends PortalTest {
	BugRecord portalBug;

	public void setup() throws Exception {
		// Setup Portal Access
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().contacts.api.create(portalSetupData);	
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", portalSetupData.get("portalName"));
		portalUser.put("password", portalSetupData.get("password"));
		sugar().login();

		// Enable portal
		sugar().admin.portalSetup.enablePortal();

		// Enable Bugs
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().logout();

		// Log into Portal
		portal.loginScreen.navigateToPortal();
		portal.login(portalUser);
	}

	@Ignore("VOOD-1096: ListView in Portal support is required for proper navigation to created records.")
	@Test
	public void PortalRecordCreateTest_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet bugData = new FieldSet();
		bugData.put("name", "Test");
		portalBug = (BugRecord)portal.bugs.portalCreate(bugData);
		portalBug.verifyInPortal();

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}