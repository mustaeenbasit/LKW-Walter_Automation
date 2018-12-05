package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19986 extends SugarTest {
	DataSource ds; 
	UserRecord qaUser;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user set password must contain special characters 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19986_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Password settings by admin
		ds = testData.get(testName);
		sugar().admin.passwordSettings(ds.get(0));
		qaUser = new UserRecord(sugar().users.getQAUser());
		sugar().logout();

		// Login with qauser
		sugar().login(sugar().users.getQAUser());
		FieldSet customData = testData.get(testName + "_1").get(0);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();

		// TODO: VOOD-987
		new VoodooControl("input", "css", "#old_password").set(sugar().users.getQAUser().get("password"));

		// Verifying Password Requirements such as contains one special character
		// TODO: VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get("assert"), true);			
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='1special']").assertVisible(true);		

		// Set new password
		sugar().users.editView.getEditField("newPassword").set(customData.get("password"));
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1special']").assertVisible(true);
		sugar().users.editView.getEditField("confirmPassword").set(customData.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();

		// Login use new password
		qaUser.put("password", customData.get("password"));
		sugar().logout();
		sugar().login(qaUser);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 