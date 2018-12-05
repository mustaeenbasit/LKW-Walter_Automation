package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19980 extends SugarTest {
	UserRecord qaUser;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user set password must contain one lower letter
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19980_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify the Checkbox in Password Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("passwordManagement").click();
		sugar().admin.passwordManagement.getControl("passwordSettingOneLower").assertExists(true);
		qaUser = new UserRecord(sugar().users.getQAUser());
		VoodooUtils.focusDefault();
		sugar().logout();

		// Login with QA user
		sugar().login(sugar().users.getQAUser());
		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();

		// TODO: VOOD-987
		new VoodooControl("input", "css", "#old_password").set(sugar().users.getQAUser().get("password"));

		// TODO: VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get("assert"), true);			
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='1lowcase']").assertVisible(true);		

		// Set new password
		sugar().users.editView.getEditField("newPassword").set(customData.get("password"));
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1lowcase']").assertVisible(true);
		sugar().users.editView.getEditField("confirmPassword").set(customData.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		// login use new password
		qaUser.put("password", customData.get("password"));
		sugar().logout();
		sugar().login(qaUser);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 