package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19987 extends SugarTest {
	DataSource ds = new DataSource();
	UserRecord chrisUser;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Set Minimum Length equal to Maximum Length, Verify can save correctly
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19987_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Updating new Password settings by Admin
		ds = testData.get(testName);
		chrisUser = (UserRecord) sugar().users.create();
		sugar().admin.passwordSettings(ds.get(0));
		sugar().logout();

		// Login with chrisUser and updating its password
		chrisUser.login();
		FieldSet customData = testData.get(testName + "_1").get(0);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();

		// TODO: VOOD-987
		new VoodooControl("input", "css", "#old_password").set(sugar().users.getDefaultData().get("password"));

		// TODO: VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get("assert"), true);			
		new VoodooControl("div", "css", "div#generate_password table.x-sqs-list div.bad[id='lengths']").assertVisible(true);		

		// Set the new password
		sugar().users.editView.getEditField("newPassword").set(customData.get("password"));		
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='lengths']").assertVisible(true);
		sugar().users.editView.getEditField("confirmPassword").set(customData.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		sugar().logout();

		// Login using new password
		chrisUser.put("password", customData.get("password"));
		chrisUser.login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 