package com.sugarcrm.test.home;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Home_28416 extends SugarTest {

	public void setup() throws Exception {
		sugar.loginScreen.getControl("loginUserName").set(sugar.users.getQAUser().get("userName"));
		sugar.loginScreen.getControl("loginPassword").set(sugar.users.getQAUser().get("password"));
		sugar.loginScreen.getControl("login").click();
	}

	/**
	 * Verify Feedback form can be displayed while in first login wizard
	 * 
	 * @throws Exception
	 */
	@Test
	public void Home_28416_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// While at the first login wizard, click Feedback in the footer
		VoodooControl feedback = new VoodooControl("span", "css", "[data-action='feedback'] .action-label");
		feedback.click();

		// Verify that Feedback form is displayed and you can send feedback.
		new VoodooControl("textarea", "css", ".fld_feedback_text textarea").assertExists(true);
		new VoodooControl("button", "css", "[data-action='submit']").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}