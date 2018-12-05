package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19977 extends SugarTest {
	DataSource ds;
	FieldSet customData;
	UserRecord qaUser;
	
	public void setup() throws Exception {
		ds = testData.get(testName);
		customData = testData.get(testName + "_1").get(0);
		sugar().login();
	}

	/**
	 * Verify user set password must contain one upper letter 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19977_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Password settings by admin
		sugar().admin.passwordSettings(ds.get(0));
		qaUser = new UserRecord(sugar().users.getQAUser());
		qaUser.navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		
		// from the CI failures, it seems like there is a message popup block click edit button sometimes
		if(sugar().users.editView.getControl("confirmCreate").queryExists())
			sugar().users.editView.getControl("confirmCreate").click();
		
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		
		// Verifying Password Requirements such as contains one upper case letter
		// TODO: VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get("assert"), true);			
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='1upcase']").assertVisible(true);		
		
		// Set new password and save
		sugar().users.editView.getEditField("newPassword").set(customData.get("password"));
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1upcase']").assertVisible(true);
		sugar().users.editView.getEditField("confirmPassword").set(customData.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		// login with new password
		qaUser.put("password", customData.get("password"));
		sugar().logout();
		sugar().login(qaUser);
						
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 