package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19979 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * enter password unsuitable to password rules 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19979_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		sugar().users.editView.getEditField("newPassword").set(customData.get("password"));
		
		VoodooUtils.waitForReady();
		
		// Verifying minimum length and the frame of "Confirm Password" field is red. 
		// TODO: VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get("min_length"), true);			
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='lengths']").assertVisible(true);	
		sugar().users.editView.getEditField("confirmPassword").assertAttribute("style", "border-color: red;");
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}