package com.sugarcrm.test.projects;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_29164 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();

		// Enable Projects module via Admin -> Display Modules and Subpanels
		sugar.admin.enableModuleDisplayViaJs(sugar.projects);
	}

	/**
	 * Verify that Correct messages are showing in Project Task module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Projects_29164_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet projectsListViewMessages = testData.get(testName).get(0);

		// Go to Project -> View Project Tasks
		sugar.navbar.selectMenuItem(sugar.projects, "viewProjectsTasks");
		VoodooUtils.focusFrame("bwc-frame");

		// Define list view controls for Projects module
		// TODO: VOOD-1363
		VoodooControl listViewMessageCtrl = new VoodooControl("p", "css", ".listViewEmpty .msg");
		VoodooControl listViewLinkMessageCtrl = new VoodooControl("a", "css", ".listViewEmpty .msg a");

		// If no projects are saved, Check the message
		// Verify the message "You currently have no projects saved. Create one now".
		listViewMessageCtrl.assertContains(projectsListViewMessages.get("emptyListViewMessage"), true);

		// Verify that 'Create' should be a link to Create Project view
		listViewLinkMessageCtrl.assertEquals(projectsListViewMessages.get("create"), true);
		VoodooUtils.focusDefault();

		// If projects are saved, Check the message
		sugar.projects.create();

		// Go to Project -> View Project Tasks
		sugar.navbar.selectMenuItem(sugar.projects, "viewProjectsTasks");
		VoodooUtils.focusFrame("bwc-frame");

		// Verify the message "You can create projects tasks from a project. View project list".
		listViewMessageCtrl.assertContains(projectsListViewMessages.get("listViewMessage"), true);

		// Verify that 'View' should be a link to Project List view
		listViewLinkMessageCtrl.assertEquals(projectsListViewMessages.get("view"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}