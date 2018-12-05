package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19521 extends SugarTest {
	FieldSet fs, maxEntries;
	UserRecord chrisUser, sallyUser;

	public void setup() throws Exception {
		maxEntries = testData.get(testName).get(0);
		FieldSet user = testData.get(testName+"_sally").get(0);
		sugar.targetlists.api.create();
		chrisUser = (UserRecord) sugar.users.api.create();
		sallyUser = (UserRecord) sugar.users.api.create(user);
		sugar.login();

		// Changing subpanel Items per page in Admin >> System Settings to '2'
		fs = new FieldSet();
		fs.put("maxEntriesPerSubPanel", maxEntries.get("maxEntriesPerPage2"));
		sugar.admin.setSystemSettings(fs);
	}

	/**
	 * Target List - Users management_Verify that the "More" pagination function in the "Users" sub-panel
	 * works correctly
	 * 
	 * 
	 */
	@Test
	public void TargetLists_19521_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Nav to TargetList listview and add five users to Users subpanel
		sugar.targetlists.navToListView();	
		sugar.targetlists.listView.clickRecord(1);
		StandardSubpanel userSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.users.moduleNamePlural);
		userSubpanel.scrollIntoViewIfNeeded(false);
		userSubpanel.clickLinkExisting();

		// TODO: VOOD-726
		new VoodooControl("div", "css", ".btn.checkall").click();
		sugar.users.searchSelect.link();

		// Asserting the no. of records default present in users subpanel
		Assert.assertTrue("No. of Records in leads subpanel is not equal to '2'. Actual count is "+userSubpanel.countRows(), userSubpanel.countRows() == 2 );

		// Asserting the no. of records present in users subpanel
		userSubpanel.showMore();
		VoodooUtils.waitForReady();
		Assert.assertTrue("No. of Records in leads subpanel is not equal to '4'. Actual count is "+userSubpanel.countRows(), userSubpanel.countRows() == 4 );

		// Asserting the no. of records present in users subpanel and non-existence of 'More' link after second click on 'More' link
		userSubpanel.showMore();
		VoodooUtils.waitForReady();
		userSubpanel.getControl("moreLink").assertVisible(false);
		Assert.assertTrue("No. of Records in leads subpanel is not equal to '5'. Actual count is "+userSubpanel.countRows(), userSubpanel.countRows() == 5 );

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}