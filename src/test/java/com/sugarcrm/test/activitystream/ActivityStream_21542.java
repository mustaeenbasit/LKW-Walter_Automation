package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.ActivityStream;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_21542 extends SugarTest {
	UserRecord qauser;

	public void setup() throws Exception {
		sugar.login();					
	}

	/**
	 * Verify that needs to handle special chars in activities stream.
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_21542_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds=testData.get(testName);

		//  As admin, change user QAUser to QAUser O'Neal. 
		qauser = new UserRecord(sugar.users.getQAUser());
		qauser.navToRecord();
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.editView.getEditField("firstName").set(ds.get(0).get("firstName"));
		VoodooUtils.focusDefault();
		sugar.users.editView.save();
		sugar.alerts.waitForLoadingExpiration();
		sugar.logout();

		// Login as QAUser
		qauser.login();

		// Post a comment in omni bar in Home Activities.
		sugar.navbar.selectMenuItem(sugar.home, "activityStream");
		ActivityStream stream = new ActivityStream();
		stream.createComment(ds.get(0).get("post"));

		// TODO: VOOD-981
		//  Verify "O'Neal qauser " display correct, as well as the posted message. 
		new VoodooControl("a", "css", ".activitystream-list.results li:nth-of-type(1) span.details a").assertEquals(ds.get(0).get("assert"), true);

		// change back to my dashboard
		sugar.navbar.clickModuleDropdown(sugar.home);

		// TODO: VOOD-953
		new VoodooControl("a", "css", "li[data-module='Home'] ul[role='menu'] li:nth-of-type(5) a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 