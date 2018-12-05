package com.sugarcrm.test.passwordmanagement;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19996 extends SugarTest {
	DataSource ds, ds1;
	UserRecord qaUser;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Selected"Must contain one upper case letter (A-Z)" and "Must contain one of the following special characters",Verify user according with the rules set password and login successful
	 *
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19996_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);
		ds1 = testData.get(testName + "_1");
		sugar().admin.passwordSettings(ds.get(0));
		qaUser = new UserRecord(sugar().users.getQAUser());
		sugar().logout();

		// login as qaUser and change password
		sugar().login(qaUser);
		// TR-1898 log in failed when password contain &, so no execute the last row in csv file until the bug is fixed
		// for(int i=0;i<ds1.size();i++) {
		for(int i=0;i<ds1.size()-1;i++) {
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

			// TODO VOOD-987
			new VoodooControl("input", "css", "#old_password").set(qaUser.get("password"));
			// TODO VOOD-947
			new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(ds1.get(0).get("assert1"), true);
			new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='1upcase']").assertVisible(true);
			new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(ds1.get(0).get("assert2"), true);
			new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='1special']").assertVisible(true);

			sugar().users.editView.getEditField("newPassword").set(ds1.get(i).get("password"));
			new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1upcase']").assertVisible(true);
			new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1special']").assertVisible(true);
			sugar().users.editView.getEditField("confirmPassword").set(ds1.get(i).get("password"));
			VoodooUtils.focusDefault();
			sugar().users.editView.save();
			
			VoodooUtils.focusFrame("bwc-frame");
			sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
			VoodooUtils.focusDefault();

			sugar().logout();

			// login use new password
			qaUser.put("password", ds1.get(i).get("password"));
			sugar().login(qaUser);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}