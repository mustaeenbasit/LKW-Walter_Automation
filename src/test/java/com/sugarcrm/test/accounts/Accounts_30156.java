package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_30156 extends SugarTest {
	public void setup() throws Exception {
		DataSource accRecords = testData.get(testName + "_accounts");

		// Create Account records 'TestAcc1' and 'TestAcc2'
		sugar().accounts.api.create(accRecords);
		
		// Login to Sugar
		sugar().login();
		sugar().accounts.navToListView();
		
		// Sorting account records w.r.t name to maintain order
		sugar().accounts.listView.sortBy("headerName", true);
		
		// Edit 'TestAcc2' to make 'Assigned to' to 'qauser'
		sugar().accounts.listView.clickRecord(2);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.recordView.save();
	}

	/**
	 * Verify Follow/Unfollow state in detail view
	 * @throws Exception
	 */
	@Test
	public void Accounts_30156_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet statuses = testData.get(testName).get(0);
		sugar().accounts.navToListView();
		
		// Unfollow 'TestAcc1'
		sugar().accounts.listView.toggleFollow(2);
		sugar().accounts.listView.clickRecord(2);
		
		// TODO: VOOD-555
		VoodooControl followStatusBadge = new VoodooControl("a", "css", "[name='follow']");
		
		// Verify that account record is in unfollowed status i.e. displays 'Follow'
		followStatusBadge.assertEquals(statuses.get("statusWhenNotFollowed"), true);

		sugar().accounts.navToListView();
		
		// Unfollow 'TestAcc2'
		sugar().accounts.listView.toggleFollow(1);
		sugar().accounts.listView.clickRecord(1);
		
		// Verify that account record is in unfollowed status i.e. displays 'Follow'
		followStatusBadge.assertEquals(statuses.get("statusWhenNotFollowed"), true);
		
		sugar().accounts.navToListView();

		// Follow 'TestAcc2'
		sugar().accounts.listView.toggleFollow(1);
		sugar().accounts.listView.clickRecord(1);

		// Verify that account record is in followed status i.e. displays 'Following'
		followStatusBadge.assertEquals(statuses.get("statusWhenFollowed"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}