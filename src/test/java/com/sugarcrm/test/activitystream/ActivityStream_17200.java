package com.sugarcrm.test.activitystream;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_17200 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName + "_" + sugar().accounts.moduleNamePlural);

		// Create 40+ record to add more than 40 messages in activity stream for accounts module
		sugar().accounts.api.create(accountData);

		// Login as Admin user
		sugar().login();
	}

	/**
	 * Show "More messages..." link at the bottom of activity stream if there are more than 20 messages (on module list view)
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17200_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the accounts list page
		sugar().accounts.navToListView();

		// Click activity stream icon next to filter bar
		sugar().accounts.listView.showActivityStream();

		// Define controls for Activity Stream result page
		// TODO: VOOD-1944, VOOD-474
		VoodooControl showPostsCtrl = new VoodooControl("button", "css", "div[data-voodoo-name='activitystream-bottom'] button");
		VoodooControl messageRowCtrl = new VoodooControl("li", "css", ".activitystream-posts-comments-container");
		FieldSet activityStreamPageData = testData.get(testName).get(0);
		int totalMessagesOnActivityStreamPage = accountData.size();

		// Verify that 20 messages are displayed on the Activity Stream page
		Assert.assertTrue("Not exactly 20 messages are visible", messageRowCtrl.countWithClass() == (totalMessagesOnActivityStreamPage/2 -1));

		// Scroll down to the bottom of activity stream
		showPostsCtrl.scrollIntoViewIfNeeded(false);

		// Verify "More posts..." link
		showPostsCtrl.assertEquals(activityStreamPageData.get("morePosts"), true);

		// Click "More posts..." link
		showPostsCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that it will load the next batch of messages
		Assert.assertTrue("Not exactly 40 messages are visible", messageRowCtrl.countWithClass() == (totalMessagesOnActivityStreamPage-2));

		// Verify that it should show "More posts..." link if there are more messages can be loaded
		// Verify "More post..." link
		showPostsCtrl.assertEquals(activityStreamPageData.get("morePosts"), true);

		// Click "More posts..." link
		showPostsCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that it will load the next batch of messages
		Assert.assertTrue("Not exactly 42 messages are visible", messageRowCtrl.countWithClass() == totalMessagesOnActivityStreamPage);

		// Verify "More post..." link is no longer visible on the Activity Stream page
		showPostsCtrl.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}