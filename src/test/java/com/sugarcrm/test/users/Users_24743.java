package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24743 extends SugarTest {
	UserRecord customUser;
	String customUserName;
	String qaUserName;

	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("userName", testName);
		fs.put("firstName", "");
		fs.put("lastName", testName);
		customUser = (UserRecord) sugar().users.api.create(fs);
		fs.clear();
		sugar().login();

		// create Account Record
		customUserName = customUser.getRecordIdentifier();
		fs.put("relAssignedTo", customUserName);
		fs.put("relTeam", customUserName);
		sugar().accounts.create(fs);
	}

	/**
	 * Verify reassign records page displays when change user's status to 'inactive'
	 * @throws Exception
	 */
	@Test
	public void Users_24743_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to user management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("userManagement").click();
		VoodooUtils.focusDefault();

		// Change status to 'inactive' for user record 'customUser'
		// TODO: VOOD-563 - need lib support for user profile edit page
		customUser.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("select", "id", "status").set(customData.get("userStatus"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "css", ".first-child button").click();
		VoodooUtils.waitForReady();

		// Reassign customUser records to qauser
		// TODO: VOOD-1023
		new VoodooControl("select", "css", "#fromuser").set(customUser.getRecordIdentifier());
		qaUserName = sugar().users.getQAUser().get("userName");
		new VoodooControl("select", "id", "touser").set(qaUserName);	
		new VoodooControl("button", "id", "remove_team_name_collection_0").click();
		new VoodooControl("input", "id", "primary_team_name_collection_0").click();
		new VoodooControl("button", "id", "teamSelect").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "team_name_input").set(qaUserName);
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "id", "massall").click();
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");	
		new VoodooControl("input", "css", "#modulemultiselect option:nth-child(1)").click();
		new VoodooControl("input", "css", "#reassign_Accounts select option:nth-child(4)").click();
		new VoodooControl("input", "css", ".button[name='steponesubmit']").click();
		new VoodooControl("td", "css", "#contentTable tr td tr td").assertContains(customData.get("updateStarted"), true);
		new VoodooControl("input", "css", ".button[type='submit']").click();
		new VoodooControl("td", "css", "#contentTable table td").assertContains(customData.get("updatedCompleted"), true);
		VoodooUtils.focusDefault();

		// Navigating to Accounts module to verify the Changes in assigned to and team field
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relAssignedTo").assertEquals(qaUserName, true);
		sugar().accounts.recordView.getDetailField("relTeam").assertContains(qaUserName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}