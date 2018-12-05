package com.sugarcrm.test.projects;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProjectRecord;
import com.sugarcrm.test.SugarTest;

public class Projects_create extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.projects);
	}

	@Test
	public void Projects_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ProjectRecord myProject = (ProjectRecord) sugar.projects.create();
		myProject.verify();

		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}