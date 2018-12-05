package com.sugarcrm.test.passwordmanagement;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19965 extends SugarTest {
	DataSource minLength, customData;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the password minimum length set is success. 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19965_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		minLength = testData.get(testName);
		customData = testData.get(testName + "_1");

		sugar().admin.passwordSettings(minLength.get(0));
		UserRecord qaUser= new UserRecord(sugar().users.getQAUser());
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
		
		// TODO: VOOD-947
		new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(customData.get(0).get("assert")+ minLength.get(0).get("passwordMinLength"), true);
		sugar().users.editView.getEditField("newPassword").set(customData.get(0).get("newPass"));
		sugar().users.editView.getEditField("confirmPassword").set(customData.get(0).get("newPass"));
		sugar().users.editView.getControl("saveButton").click();
		VoodooUtils.acceptDialog();
		sugar().users.editView.getEditField("newPassword").set(qaUser.get("password"));
		sugar().users.editView.getEditField("confirmPassword").set(qaUser.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}