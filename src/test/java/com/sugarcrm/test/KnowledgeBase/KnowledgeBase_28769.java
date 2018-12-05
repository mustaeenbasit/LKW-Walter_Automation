package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class KnowledgeBase_28769 extends SugarTest {
	DataSource kbData = new DataSource();
	UserRecord chris, bill;

	public void setup() throws Exception {
		// Create 3 KB articles (to test sort by frequency-used + is-useful in dash-let)
		kbData = testData.get(testName);
		sugar().knowledgeBase.api.create(kbData);
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create 2 users
		// TODO: VOOD-1200
		FieldSet userDS = testData.get(testName+"_users").get(0);
		chris = (UserRecord) sugar().users.create();
		bill = (UserRecord) sugar().users.create(userDS);

		// Logout Admin
		sugar().logout();
	}

	/**
	 * Verify that KB that has the most positive scores will be listed first in "Most useful published article" Dashlet  
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28769_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser, enable My Dashboard in KB list view
		sugar().login(sugar().users.getQAUser());

		// Sorting as order is not consistent
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.sortBy("headerName", false);

		// Select My Dashboard from the right hand side
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(kbData.get(1).get("body"), true))
			sugar().dashboard.chooseDashboard(kbData.get(1).get("body"));

		// Assert Dashlet is initially empty
		// TODO: VOOD-960, VOOD-670
		VoodooControl kbDashlet = new VoodooControl("div", "css", "[data-voodoo-name='kbs-dashlet-most-useful']");
		VoodooControl usefulCtrl = new VoodooControl("a", "css", "[data-action='useful']");
		kbDashlet.assertContains(kbData.get(2).get("body"), true);
		VoodooUtils.waitForReady(30000); // Wait needed

		// Update KB record owner, useful status- to populate Dashlet
		sugar().knowledgeBase.listView.clickRecord(1);
		for (int i = 0; i < kbData.size(); i++) 
		{ 
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.showMore();
			sugar().knowledgeBase.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
			sugar().knowledgeBase.recordView.save();

			// Clicking the Useful button on all 3 KB articles, kb3 kb2 kb1
			usefulCtrl.click();
			VoodooUtils.waitForReady();

			if (i != kbData.size()-1)
				sugar().knowledgeBase.recordView.gotoNextRecord();
		} 

		// Increase frequency of kb3 by 1, kb2 by 2 - since Dash-let sorts by frequency 
		sugar().knowledgeBase.recordView.gotoPreviousRecord();
		sugar().knowledgeBase.recordView.gotoPreviousRecord();
		sugar().knowledgeBase.recordView.gotoNextRecord();

		// Assert Dashlet contains kb2, kb3, kb1 in that order
		sugar().knowledgeBase.navToListView();
		VoodooControl kbDashletRow1 = new VoodooControl("div", "css", "[data-voodoo-name='kbs-dashlet-most-useful'] li:nth-child(1)");
		VoodooControl kbDashletRow2 = new VoodooControl("div", "css", "[data-voodoo-name='kbs-dashlet-most-useful'] li:nth-child(2)");
		VoodooControl kbDashletRow3 = new VoodooControl("div", "css", "[data-voodoo-name='kbs-dashlet-most-useful'] li:nth-child(3)");

		kbDashletRow1.assertContains(kbData.get(1).get("name"), true);
		kbDashletRow2.assertContains(kbData.get(2).get("name"), true);
		kbDashletRow3.assertContains(kbData.get(0).get("name"), true);

		// Log out qauser, Login as Chris, enable My Dashboard in KB list view
		sugar().logout();
		sugar().login(chris);
		sugar().knowledgeBase.navToListView();
		// Sorting as order is not consistent
		sugar().knowledgeBase.listView.sortBy("headerName", false);
		// Select My Dashboard from the right hand side
		if(!dashboardTitle.queryContains(kbData.get(1).get("body"), true))
			sugar().dashboard.chooseDashboard(kbData.get(1).get("body"));

		// Open kb1, kb2 (increases frequency) and mark useful
		sugar().knowledgeBase.listView.clickRecord(2);
		usefulCtrl.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.gotoNextRecord();
		usefulCtrl.click();
		VoodooUtils.waitForReady();

		// Assert Dashlet contains kb2, kb1, kb3 in that order
		sugar().knowledgeBase.navToListView();
		kbDashletRow1.assertContains(kbData.get(1).get("name"), true);
		kbDashletRow2.assertContains(kbData.get(0).get("name"), true);
		kbDashletRow3.assertContains(kbData.get(2).get("name"), true);

		// Log out chris, Login as bill, enable My Dashboard in KB list view
		sugar().logout();
		sugar().login(bill);
		sugar().knowledgeBase.navToListView();
		// Sorting as order is not consistent
		sugar().knowledgeBase.listView.sortBy("headerName", false);
		// Select My Dashboard from the right hand side
		if(!dashboardTitle.queryContains(kbData.get(1).get("body"), true))
			sugar().dashboard.chooseDashboard(kbData.get(1).get("body"));

		// Open kb1 (increases frequency) and mark useful
		sugar().knowledgeBase.listView.clickRecord(3);
		usefulCtrl.click();
		VoodooUtils.waitForReady();

		// Assert Dashlet contains kb1, kb2, kb3 in that order
		sugar().knowledgeBase.navToListView();
		kbDashletRow1.assertContains(kbData.get(0).get("name"), true);
		kbDashletRow2.assertContains(kbData.get(1).get("name"), true);
		kbDashletRow3.assertContains(kbData.get(2).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}