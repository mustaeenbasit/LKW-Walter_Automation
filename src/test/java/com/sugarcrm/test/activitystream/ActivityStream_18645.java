package com.sugarcrm.test.activitystream;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class ActivityStream_18645 extends SugarTest {
	AccountRecord myAcc;

	public void setup() throws Exception {
		myAcc = (AccountRecord) sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify that no activities message is generated about non-audit field's change when edit a non-audit field and an audit field 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_18645_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		FieldSet data = new FieldSet();
		data.put("name", customData.get("name"));
		data.put("type", customData.get("type"));
		myAcc.edit(data);

		// check activity stream message on record view
		sugar.accounts.recordView.showActivityStream();
		sugar.alerts.waitForLoadingExpiration();
		sugar.accounts.recordView.activityStream.assertCommentContains(customData.get("assert_type"), 1, true);
		sugar.accounts.recordView.activityStream.assertCommentContains(customData.get("assert_name"), 1, true);
		sugar.accounts.recordView.activityStream.assertCommentContains(customData.get("assert_industry"), 1, false);

		// check activity stream message on list view
		sugar.accounts.navToListView();
		sugar.accounts.listView.showActivityStream();
		sugar.alerts.waitForLoadingExpiration();
		sugar.accounts.listView.activityStream.assertCommentContains(customData.get("assert_type"), 1, true);
		sugar.accounts.listView.activityStream.assertCommentContains(customData.get("assert_name"), 1, true);
		sugar.accounts.listView.activityStream.assertCommentContains(customData.get("assert_industry"), 1, false);

		// check activity stream message on activity stream home page
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		sugar.alerts.waitForLoadingExpiration();
		ActivityStream stream = new ActivityStream();
		stream.assertCommentContains(customData.get("assert_type"), 1, true);
		stream.assertCommentContains(customData.get("assert_name"), 1, true);
		stream.assertCommentContains(customData.get("assert_industry"), 1, false);

		// TODO VOOD-954 the clean up will fail if not change back to dashboard page
		sugar.navbar.clickModuleDropdown(sugar.home);

		// TODO VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();
		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}