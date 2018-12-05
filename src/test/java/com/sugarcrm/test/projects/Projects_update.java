package com.sugarcrm.test.projects;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.ProjectRecord;

public class Projects_update extends SugarTest {
	ProjectRecord myProject;
	
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.projects);
		myProject = (ProjectRecord)sugar.projects.api.create();
	}

	@Test
	public void Projects_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Project from International Business Machines, Inc.");
		newData.put("status", "In Review");
		newData.put("priority", "Medium");

		// Edit the Project using the UI.
		myProject.edit(newData);
		
		// Verify the Project was edited.
		myProject.verify(newData);
		
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}