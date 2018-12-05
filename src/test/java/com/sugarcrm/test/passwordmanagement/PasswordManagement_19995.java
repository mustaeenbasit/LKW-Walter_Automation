package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19995 extends SugarTest {
	UserRecord qaUser;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Selected"Must contain one upper case letter (A-Z)" and"Must contain one lower case letter (a-z)",Verify user according with the rules set password and login successful
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19995_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify the passwordSettingOneLower and passwordSettingOneUpper  in Password Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("passwordManagement").click();
		sugar().admin.passwordManagement.getControl("passwordSettingOneLower").assertExists(true);
		sugar().admin.passwordManagement.getControl("passwordSettingOneUpper").assertExists(true);
		qaUser = new UserRecord(sugar().users.getQAUser());
		VoodooUtils.focusDefault();
		sugar().logout();

		// login as qaUser and change password
		sugar().login(qaUser);
		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();

		// from the CI failures, it seems like there is a message popup block click edit button sometimes
		if(sugar().users.editView.getControl("confirmCreate").queryExists())
			sugar().users.editView.getControl("confirmCreate").click();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();

		// TODO: VOOD-987
		new VoodooControl("input", "css", "#old_password").set(qaUser.get("password"));
		// TODO: VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get("upperCaseLetter"), true);			
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='1upcase']").assertVisible(true);	
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get("lowerCaseLetter"), true);			
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='1lowcase']").assertVisible(true);	

		sugar().users.editView.getEditField("newPassword").set(customData.get("password"));
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1upcase']").assertVisible(true);
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1lowcase']").assertVisible(true);
		sugar().users.editView.getEditField("confirmPassword").set(customData.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		sugar().logout();
		// login use new password
		qaUser.put("password", customData.get("password"));
		sugar().login(qaUser);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}