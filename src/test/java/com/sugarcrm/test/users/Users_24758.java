package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24758 extends SugarTest { 
	UserRecord chris;

	public void setup() throws Exception {
		sugar().login();

		// Create new user by setting 'New User Wizard' to 'true' to run 'User Wizard'
		FieldSet fs = new FieldSet();
		fs.put("newUserWizard", "true");
		chris = (UserRecord) sugar().users.create(fs);
		sugar().logout();
	}

	/**
	 * Verify links of last page of New User Wizard
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24758_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Define Controls
		// TODO: VOOD-1893
		VoodooControl sugarTrainingIcon = new VoodooControl("i", "css", ".modal-body .fa.fa-info-circle");
		VoodooControl documentationIcon = new VoodooControl("i", "css", ".modal-body .fa.fa-book");
		VoodooControl knowledgeBaseIcon = new VoodooControl("i", "css", ".modal-body .fa.fa-briefcase");
		VoodooControl forumsIcon = new VoodooControl("i", "css", ".modal-body .fa.fa-comments-o");
		VoodooControl wizardLinks = new VoodooControl("ul", "css", ".thumbnails.row-fluid");

		// Login with newly created user
		chris.login();

		// Click Next button
		sugar().newUserWizard.clickNextButton();
		VoodooUtils.waitForReady();

		// Click Next button on second step
		sugar().newUserWizard.clickNextButton();
		VoodooUtils.waitForReady();

		// In the latest step, verify icons
		sugarTrainingIcon.assertVisible(true);
		documentationIcon.assertVisible(true);
		knowledgeBaseIcon.assertVisible(true);
		forumsIcon.assertVisible(true);

		// Verify links
		wizardLinks.assertContains(customData.get("sugarTraining"), true);
		wizardLinks.assertContains(customData.get("documentation"), true);
		wizardLinks.assertContains(customData.get("knowledgeBase"), true);
		wizardLinks.assertContains(customData.get("forums"), true);

		// Verify the redirection pages after clicking the links
		// Click Sugar Training Icon
		sugarTrainingIcon.click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("h1", "css", ".home-hero-image-wrapper.full h1").assertContains(customData.get("sugarTrainingTitle"), true);
		VoodooUtils.closeWindow();

		// Click Documentation Icon
		documentationIcon.click();
		VoodooUtils.focusWindow(1);
		VoodooControl title = new VoodooControl("li", "css", ".nav.navbar-nav.navbar-collapse.collapse .active");
		title.assertContains(customData.get("documentation").toUpperCase(), true);
		VoodooUtils.closeWindow();

		// Click Knowledge Base Icon
		knowledgeBaseIcon.click();
		VoodooUtils.focusWindow(1);
		title.assertContains(customData.get("knowledgeBase").toUpperCase(), true);
		VoodooUtils.closeWindow();

		// Click Forums Icon
		forumsIcon.click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("div", "id", "jive-body").assertContains(customData.get("forumsTitle"), true);
		VoodooUtils.closeWindow();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 