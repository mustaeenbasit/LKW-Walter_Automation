package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_22872 extends SugarTest {
	FieldSet customFS = new FieldSet();
	UserRecord myUserRecord;

	public void setup() throws Exception {
		sugar.leads.api.create();
		myUserRecord = (UserRecord) sugar.users.api.create();
		customFS = testData.get(testName).get(0);
		sugar.login();

		// Go to Team Management 
		BWCSubpanel userSubpanel = sugar.teams.detailView.subpanels.get(sugar.users.moduleNamePlural);

		// TODO: VOOD-776
		// Assign West team to QAUser
		sugar.teams.navToListView();
		sugar.teams.listView.clickRecord(2);
		VoodooUtils.focusFrame("bwc-frame");
		userSubpanel.getControl("teamMembership").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "user_name_advanced").set(sugar.users.getQAUser().get("userName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
		new VoodooControl("input", "css", "#MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		// Assign QAuser to a lead record
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.showMore();
		sugar.leads.recordView.edit();
		sugar.leads.recordView.getEditField("relTeam").set(customFS.get("teamName"));
		sugar.leads.recordView.save();

		// Logout as Admin & Login as QAuser
		sugar.logout();
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Search in activities stream works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_22872_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName+"_verify_string");

		// Go to leads recordView Activity Stream
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.showActivityStream();

		VoodooControl disableTagDropDown = new VoodooControl("li", "css", ".dropdown-menu.activitystream-tag-dropdown li.disabled");
		VoodooControl tagDropDownList = new VoodooControl("a", "css", ".dropdown-menu.activitystream-tag-dropdown li a");

		for(int i = 0; i < ds.size(); i++) {
			VoodooControl streamInputCtrl = sugar.leads.recordView.activityStream.getControl("streamInput");
			streamInputCtrl.set(ds.get(i).get("searchKey"));
			VoodooUtils.waitForReady();

			// Verify that tag drop-down works properly
			if(i <= 3)
				tagDropDownList.assertExists(false);
			else
				tagDropDownList.assertContains(ds.get(i).get("verifyString"), true);

			// Verify that QAuser have no access to select gray out, "no access" appears too.  User is not able to select that user
			if(i == 5) {
				disableTagDropDown.assertExists(true);
				disableTagDropDown.assertContains(customFS.get("noAccessMsg"), true);
			}
			sugar.leads.recordView.showActivityStream(); // Required to show fresh activity stream
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}