package com.sugarcrm.test.projects;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_29163 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();

		// Enable Projects module via Admin -> Display Modules and Subpanels
		sugar.admin.enableModuleDisplayViaJs(sugar.projects);
	}

	/**
	 * Verify that Create link is not redirecting on wrong page at Project Templates List
	 * 
	 * @throws Exception
	 */
	@Test
	public void Projects_29163_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet moduleTitle = testData.get(testName).get(0);

		// Go to "Project -> View Project Templates"
		sugar.navbar.selectMenuItem(sugar.projects, "viewProjectsTemplates");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1363
		// Click on "Create" link
		new VoodooControl("a", "css", ".listViewEmpty .msg a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Create link should be redirected on 'Project Template' page like, On Click on "Project -> Create Project Template"
		new VoodooControl("div", "css", ".moduleTitle").assertContains(moduleTitle.get("moduleTitle"), true);
		Assert.assertTrue("URL does not contain string Project&action=ProjectTemplatesEditView when it should", VoodooUtils.getUrl().contains(moduleTitle.get("urlData")));

		// Click on Cancel button
		new VoodooControl("input", "id", "CANCEL_HEADER").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}