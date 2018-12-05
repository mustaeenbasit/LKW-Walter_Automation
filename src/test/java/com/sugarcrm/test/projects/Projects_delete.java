package com.sugarcrm.test.projects;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProjectRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Projects_delete extends SugarTest {
	ProjectRecord myProject;
	
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.projects);
		myProject = (ProjectRecord)sugar.projects.create();
	}

	@Test
	public void Projects_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the Project using the UI.
		myProject.delete();

		// Verify the account was deleted.
		sugar.projects.navToListView();
		assertEquals(VoodooUtils.contains(myProject.getRecordIdentifier(), true), false);
		
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}