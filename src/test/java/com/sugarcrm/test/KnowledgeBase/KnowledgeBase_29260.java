package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class KnowledgeBase_29260 extends SugarTest {
	DataSource kbData = new DataSource();
	UserRecord myUser;

	public void setup() throws Exception {
		kbData = testData.get(testName);
		sugar().knowledgeBase.api.create(kbData);

		// Login as an admin user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a custom user
		// TODO: VOOD-1200
		myUser = (UserRecord)sugar().users.create();

		// Update author = Chris, for KB records created. 
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		for (int i = 0; i < kbData.size(); i++) { 
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.showMore();
			sugar().knowledgeBase.recordView.getEditField("relAssignedTo").set(myUser.get("userName"));
			sugar().knowledgeBase.recordView.save();
			if (i != kbData.size()-1)
				sugar().knowledgeBase.recordView.gotoNextRecord();
		} 

		// Logout and login with custom user
		sugar().logout();
		sugar().login(myUser);
	}

	/**
	 * Verify that if score is 0 or negative, then won't list them in "Most useful published article" Dashlet  
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29260_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();

		// Sorting performed as order is not consistent
		sugar().knowledgeBase.listView.sortBy("headerName", false);

		// Adding Dashlet Most Useful Published Knowledge Base Articles in the Knowledge Base listView
		// TODO: VOOD-960,VOOD-670
		sugar().knowledgeBase.dashboard.clickCreate();
		sugar().knowledgeBase.dashboard.getControl("title").set(testName);
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(1, 1);
		new VoodooControl("input", "css", ".span4.search").set(kbData.get(0).get("body"));
		VoodooUtils.waitForReady(); // Needed wait
		new VoodooControl("a", "css", "tr.single .fld_title a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		sugar().knowledgeBase.dashboard.save();

		// Clicking the Useful button on record kb3 & kb2
		// TODO: VOOD-1783
		VoodooControl notUsefulCtrl= new VoodooControl("a", "css", "[data-action='notuseful']");
		VoodooControl usefulCtrl = new VoodooControl("a", "css", "[data-action='useful']");
		sugar().knowledgeBase.listView.clickRecord(1);
		usefulCtrl.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.gotoNextRecord();
		usefulCtrl.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.navToListView();

		// TODO: VOOD-960, VOOD-670
		VoodooControl kbDashlet = new VoodooControl("div", "css", "[data-voodoo-name='kbs-dashlet-most-useful']");
		kbDashlet.assertContains(kbData.get(1).get("name"), true);
		kbDashlet.assertContains(kbData.get(2).get("name"), true);

		// Click Record kb1 in the list View and clicking Unuseful button.
		sugar().knowledgeBase.listView.clickRecord(3);
		notUsefulCtrl.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.navToListView();

		// Verifying only kb2 & kb3 record are shown in Most Useful Published Dashlet
		kbDashlet.assertContains(kbData.get(0).get("name"), false);
		kbDashlet.assertContains(kbData.get(1).get("name"), true);
		kbDashlet.assertContains(kbData.get(2).get("name"), true);

		// Navigating to kb2 record and clicking Unuseful button
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(2);
		notUsefulCtrl.click();
		VoodooUtils.waitForReady();

		// Verifying that only kb3 record is now listed in Most Useful Published Dashlet
		sugar().knowledgeBase.navToListView();
		kbDashlet.assertContains(kbData.get(1).get("name"), false);
		kbDashlet.assertContains(kbData.get(2).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}