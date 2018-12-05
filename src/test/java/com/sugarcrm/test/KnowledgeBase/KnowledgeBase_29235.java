package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vishal Kumar <vkumar@sugarcrm.com>
 */
public class KnowledgeBase_29235 extends SugarTest {
	DataSource kbRecords = new DataSource();
	UserRecord jim, max;

	public void setup() throws Exception {
		DataSource users = testData.get(testName);
		kbRecords = testData.get(testName + "_kbRecords");

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Creating 3 user
		// TODO: VOOD-1200 - Authentication failed on calling Users default data
		UserRecord sally = (UserRecord) sugar().users.create(users.get(0));
		jim = (UserRecord) sugar().users.create(users.get(1));
		max = (UserRecord) sugar().users.create(users.get(2));
		sugar().logout();

		// Login as Sally and creating two kb record as kb1 and kb2.
		sugar().login(sally);
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().knowledgeBase.create(kbRecords.get(0));
		sugar().knowledgeBase.create(kbRecords.get(1));
		sugar().logout();
	}

	/**
	 * Verify that If the same score and number of positive votes, than first is the article with a higher frequency
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29235_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initialize dashboard test data
		DataSource dashboardName = testData.get(testName + "_dashboard");

		// Login as Jim and creating Dashboard with most useful dashlet.
		sugar().login(jim);

		// TODO: VOOD-960 - Dashlet selection
		VoodooControl mostUsefulDashlet = new VoodooControl("div", "css", ".table-striped tr:nth-child(4) a");
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().knowledgeBase.dashboard.clickCreate();
		sugar().knowledgeBase.dashboard.getControl("title").set(dashboardName.get(0).get("dashboardTitle"));
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(1, 1);
		mostUsefulDashlet.click();
		sugar().knowledgeBase.createDrawer.save();
		sugar().knowledgeBase.dashboard.save();

		// Clicking on useful button in created KB record view
		// TODO: VOOD-1783 - Need lib support for Vote buttons('Not Useful' and 'Useful') on KB record view page.
		VoodooControl kbUsefulLink = new VoodooControl("a", "css", "a[data-action='useful']");
		sugar().knowledgeBase.listView.clickRecord(1);
		kbUsefulLink.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(2);
		kbUsefulLink.click();
		VoodooUtils.waitForReady();
		sugar().logout();

		// Login as Max and creating dashboard with most useful dashlet
		sugar().login(max);
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().knowledgeBase.dashboard.clickCreate();
		sugar().knowledgeBase.dashboard.getControl("title").set(dashboardName.get(1).get("dashboardTitle"));
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(1, 1);
		mostUsefulDashlet.click();
		sugar().knowledgeBase.createDrawer.save();
		sugar().knowledgeBase.dashboard.save();

		// Clicking on useful button in created KB record view
		sugar().knowledgeBase.listView.clickRecord(1);
		kbUsefulLink.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(2);
		kbUsefulLink.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.navToListView();

		// Max open kb2 record view one more time.
		sugar().knowledgeBase.listView.clickRecord(1);
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.navToListView();

		// Verifying kb2 is listed in the first, then kb1.
		// TODO: VOOD-960 - Dashlet selection
		// TODO: VOOD-592 - Add dashlet support to home screen model.
		new VoodooControl("div", "css", "div[data-voodoo-name='kbs-dashlet-most-useful'] li").assertContains(kbRecords.get(1).get("name"), true);
		new VoodooControl("div", "css", "div[data-voodoo-name='kbs-dashlet-most-useful'] li:nth-child(2)").assertContains(kbRecords.get(0).get("name"), true);

		// Verifying Kb records with its frequency in list view
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(kbRecords.get(1).get("name"), true);
		sugar().knowledgeBase.listView.getDetailField(1, "viewCount").assertEquals("4", true);
		sugar().knowledgeBase.listView.getDetailField(2, "name").assertEquals(kbRecords.get(0).get("name"), true);
		sugar().knowledgeBase.listView.getDetailField(2, "viewCount").assertEquals("3", true);
	}

	public void cleanup() throws Exception {}
}