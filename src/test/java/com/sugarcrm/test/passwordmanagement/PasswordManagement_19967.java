package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19967 extends SugarTest {
	DataSource ds;
	UserRecord qaUser;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify no minimum requirement of password if set minimum length to null.
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19967_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
       
		// Settings made to match the Password criteria
		FieldSet data = new FieldSet();
		data.put("passwordMinLength", ds.get(0).get("passwordMinLength"));
		data.put("passwordSettingOneLower", Boolean.toString(false));
		data.put("passwordSettingOneUpper", Boolean.toString(false));
		data.put("passwordSettingOneNumber", Boolean.toString(false));
		sugar().admin.passwordSettings(data);
		
		// Navigate to qauser
		qaUser= new UserRecord(sugar().users.getQAUser());
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
		new VoodooControl("td", "css", "div#generate_password").assertElementContains(ds.get(0).get("assert"), false);

		// save new password whose length is 1
		sugar().users.editView.getEditField("newPassword").set(ds.get(1).get("passwordMinLength"));
		sugar().users.editView.getEditField("confirmPassword").set(ds.get(1).get("passwordMinLength"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}