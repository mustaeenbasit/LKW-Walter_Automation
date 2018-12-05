package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19976 extends SugarTest {
	DataSource ds, ds1;
	UserRecord qaUser;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().admin.passwordSettings(ds.get(0));
	}

	/**
	 * Enter password suitable to password rules
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19976_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds1 = testData.get(testName + "_1");
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
		
		// After each criteria is met, the red X should turn to a green check mark
		for (int i = 0; i < ds.size() - 1; i++) {
			sugar().users.editView.getEditField("newPassword").set(ds1.get(i).get("password"));
			// TODO VOOD-947
			new VoodooControl("div", "css", "div#generate_password table.x-sqs-list div.good[id='"+ ds1.get(i).get("assert1") + "']").assertVisible(true);
			new VoodooControl("div", "css", "div#generate_password table.x-sqs-list div.good[id='"+ ds1.get(i).get("assert2") + "']").assertVisible(true);
			
			sugar().users.editView.getEditField("confirmPassword").set(ds1.get(i).get("confirm"));
			sugar().users.editView.getEditField("confirmPassword").assertAttribute("style", "border-color: red;");
			sugar().users.editView.getControl("saveButton").click();
			VoodooUtils.acceptDialog();			
		}

		sugar().users.editView.getEditField("newPassword").set(ds1.get(ds1.size()-1).get("password"));
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1upcase']").assertVisible(true);
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1lowcase']").assertVisible(true);
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='1number']").assertVisible(true);
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='lengths']").assertVisible(true);
		sugar().users.editView.getEditField("confirmPassword").set(ds1.get(ds1.size()-1).get("confirm"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 