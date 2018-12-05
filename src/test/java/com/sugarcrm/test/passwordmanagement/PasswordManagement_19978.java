package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19978 extends SugarTest {	
	FieldSet settings;

	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * can't save password when "new password" isn't equal as "confirm password"
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19978_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName);
		settings= testData.get(testName+"_settings").get(0);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();

		// Password Settings
		// from the CI failures, it seems like there is a message popup block click edit button sometimes
		if(sugar().users.editView.getControl("confirmCreate").queryExists())
			sugar().users.editView.getControl("confirmCreate").click();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		sugar().users.editView.getEditField("newPassword").set(customData.get(0).get("newPass"));
		sugar().users.editView.getEditField("confirmPassword").assertAttribute("style", "border-color: red;");
		sugar().users.editView.getEditField("confirmPassword").set(customData.get(0).get("conPass"));
		sugar().users.editView.getEditField("confirmPassword").assertAttribute("style", "border-color: red;");

		// TODO: VOOD-971
		new VoodooControl("div", "css", "div#generate_password #comfirm_pwd_match").assertEquals(customData.get(0).get("assert"), true);
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}