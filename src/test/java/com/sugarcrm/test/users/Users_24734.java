package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24734 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord)sugar().users.api.create();	
		sugar().login();
	}

	/**
	 * Create_User_Edit_Cancel and verify the Switching of Tabs on Advanced/Access
	 * @throws Exception
	 */
	@Test
	public void Users_24734_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Navigate to Users ListView
		sugar().users.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.listView.getControl("nameBasic").set(myUser.getRecordIdentifier());
		VoodooUtils.focusDefault();
		sugar().users.listView.submitSearchForm();

		// Click Edit and Cancel on the Users Detail View 
		sugar().users.listView.clickRecord(1);
		sugar().users.detailView.edit();
		sugar().users.editView.cancel();

		// Verify no Errors are displayed and nothing have been changed
		VoodooControl errorMsg = sugar().alerts.getError();
		errorMsg.assertVisible(false);
		VoodooUtils.focusFrame("bwc-frame");
		FieldSet defaultData = sugar().users.getDefaultData();
		sugar().users.detailView.getDetailField("userName").assertEquals(defaultData.get("userName"), true);

		// Click Advanced Tab
		// TODO: VOOD-563
		new VoodooControl("a", "id", "tab2").click();

		// Verify you are on the Advanced Tab page and no Errors are displayed
		errorMsg.assertVisible(false);
		new VoodooControl("slot", "css", "#settings tr:nth-child(3) td:nth-child(2) slot").assertEquals(customData.get("teamName"), true);
		new VoodooControl("slot", "css", "#settings tr:nth-child(7) td:nth-child(2) slot").assertEquals(customData.get("characterSet"), true);

		// Verify you are on the Access tab page and no errors are displayed
		errorMsg.assertVisible(false);
		new VoodooControl("a", "id", "tab3").click();
		new VoodooControl("td", "css", "#list_subpanel_aclroles tr:nth-child(3) td").assertEquals(customData.get("message"), true);
		new VoodooControl("a", "css", "#user_detailview_tabs .oddListRowS1:nth-child(2) td a").assertEquals(defaultData.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}