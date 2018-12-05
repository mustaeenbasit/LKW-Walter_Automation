package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class ActivityStream_20117 extends SugarTest {		
	public void setup() throws Exception {

		// Login as QAUser
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * No activities stream in Home when user posts a text without /@user_id
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_20117_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to List view of Leads module -> Activities Stream 
		DataSource ds = testData.get(testName);
		sugar.leads.navToListView();
		sugar.leads.listView.showActivityStream();
		sugar.leads.listView.activityStream.createComment(ds.get(0).get("post"));

		// Verify the activities stream message
		sugar.leads.listView.activityStream.assertCommentContains(ds.get(0).get("post"), 1, true);

		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();

		// TODO: VOOD-969
		VoodooControl activityStreamCtrl  = new VoodooControl("span", "css", ".activitystream-list.results li:nth-of-type(1) div .tagged");
		if(activityStreamCtrl.queryVisible())
			stream.assertCommentContains(ds.get(0).get("post"), 1, false);	

		// Logout from QAUser and login as admin
		sugar.logout();
		sugar.login();

		// Go to Home-Activities
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");

		// Verify the activities stream message
		if(activityStreamCtrl.queryVisible())
			stream.assertCommentContains(ds.get(0).get("post"), 1, false);	

		// TODO: VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);

		// TODO: VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();	

		sugar.leads.navToListView();
		sugar.leads.listView.showActivityStream();
		sugar.alerts.waitForLoadingExpiration(); // Need to add extra wait to resolve 'StaleElementReferenceException' issue, as the page is not getting loaded.
		sugar.leads.listView.activityStream.assertCommentContains(ds.get(0).get("post"), 1, true);	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 