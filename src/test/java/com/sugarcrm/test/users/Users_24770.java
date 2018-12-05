package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24770 extends SugarTest {
	UserRecord max;

	public void setup() throws Exception {
		DataSource newUserData = testData.get(testName+"_user");
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();

		// Create user max
		max = (UserRecord) sugar().users.create(newUserData.get(0));

		// Assign account record to max user
		myAccount.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(max.getRecordIdentifier());
		sugar().accounts.recordView.save();
	}

	/**
	 * Verify that it's possible to reassign records from inactive user
	 *
	 * @throws Exception
	 */
	@Test
	public void Users_24770_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		max.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		FieldSet customFS = testData.get(testName).get(0);

		// TODO: VOOD-1053,994
		// Set the user as Inactive and click Save.
		VoodooControl statusCtrl = new VoodooControl("select", "css", "[name='status']");
		statusCtrl.click();
		VoodooUtils.waitForReady();
		statusCtrl.set(customFS.get("status"));
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "css", ".first-child button").click();
		VoodooUtils.waitForReady();

		// Reassign records to another user
		// In the 'Modules to Include in Reassignment' select Accounts, External Accounts, Notifications.
		// TODO: VOOD-1023
		VoodooControl fromUserCtrl = new VoodooControl("select", "id", "fromuser");
		fromUserCtrl.click();
		VoodooUtils.waitForReady();
		fromUserCtrl.set(max.get("firstName")+" "+max.get("lastName"));
		VoodooControl toUserCtrl = new VoodooControl("select", "id", "touser");
		toUserCtrl.click();
		VoodooUtils.waitForReady();
		toUserCtrl.set(sugar().users.qaUser.get("userName"));
		new VoodooControl("input", "css", "#modulemultiselect option:nth-child(1)").click();
		new VoodooControl("input", "css", ".button[name='steponesubmit']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".button[type='submit']").click();
		VoodooUtils.waitForReady();

		// Verify that External account, account and notifications are reassigned to another user successfully.
		VoodooControl verifyText = new VoodooControl("td", "css", "#contentTable table.detail.view:nth-child(4) tr td");
		verifyText.assertContains(customFS.get("msg1"), true);
		verifyText.assertContains(customFS.get("msg2"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}