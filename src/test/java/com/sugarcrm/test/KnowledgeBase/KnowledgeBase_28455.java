package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class KnowledgeBase_28455 extends SugarTest {
	DataSource myKbDS = new DataSource();
	FieldSet myKbFS = new FieldSet();

	public void setup() throws Exception {
		// Create 4 KB articles to test list view filter
		myKbDS= testData.get(testName);
		sugar().knowledgeBase.api.create(myKbDS);

		// Create 5th KB article: 1-day-old, to test list view Date-Created filter
		myKbFS = testData.get(testName+"_2").get(0);
		DateTime date = DateTime.now();
		FieldSet myKbFStemp = new FieldSet();
		myKbFStemp.put("date_entered_date", date.minusDays(1).toString("MM/dd/yyyy"));
		myKbFStemp.put("date_entered_time", date.toString("hh:mma"));
		myKbFStemp.put("name", myKbFS.get("name"));
		sugar().knowledgeBase.api.create(myKbFStemp);

		// Add 1-day-old KB article name in DS for assertions later
		myKbFStemp.remove("date_entered_date");
		myKbFStemp.remove("date_entered_time");
		myKbFStemp.put("status", myKbFS.get("status"));
		myKbDS.add(myKbFStemp);

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Sort Desc KB listview items by Article name
		// Sorting is needed because order is not consistent in list view
		sugar().knowledgeBase.listView.sortBy("headerName", false);

		// Set 'favKB' article as favorite
		sugar().knowledgeBase.listView.toggleFavorite(4);

		// Open KB article to update Author fields
		// For Filter All Articles - Author = current user or status = published
		sugar().knowledgeBase.listView.clickRecord(1);

		// Update author = qauser, recently viewed flag for first 2 records 
		String author = sugar().users.qaUser.get("userName");
		for (int i = 0; i < 2; i++) 
		{ 
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.showMore();
			sugar().knowledgeBase.recordView.getEditField("relAssignedTo").set(author);
			sugar().knowledgeBase.recordView.save();
			if (i==0) sugar().knowledgeBase.recordView.gotoNextRecord();
		}

		// Go back to list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
	}

	/**
	 * Verify the correct default filters are in list view, and that the filters work correctly
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28455_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Filter My Articles
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterAssignedToMe();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countMy")+" FAILED",
				Integer.parseInt(myKbFS.get("countMy")), sugar().knowledgeBase.listView.countRows());
		for (int i = 1; i < 4; i++) 
			sugar().knowledgeBase.listView.verifyField(i, "name", myKbDS.get(i+1).get("name"));

		// Filter My Favorites
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterMyFavorites();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countFav")+" FAILED",
				Integer.parseInt(myKbFS.get("countFav")), sugar().knowledgeBase.listView.countRows());
		sugar().knowledgeBase.listView.verifyField(1, "name", myKbDS.get(3).get("name"));

		// Filter Recently Viewed -- Note order of records is also tested
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterRecentlyViewed();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countViewed")+" FAILED",
				Integer.parseInt(myKbFS.get("countViewed")), sugar().knowledgeBase.listView.countRows());
		sugar().knowledgeBase.listView.verifyField(1, "name", myKbDS.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", myKbDS.get(0).get("name"));

		// Filter Recently Created
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterRecentlyCreated();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countCreated")+" FAILED",
				Integer.parseInt(myKbFS.get("countCreated")), sugar().knowledgeBase.listView.countRows());
		for (int i = 0; i < 4; i++) 
			sugar().knowledgeBase.listView.verifyField(i+1, "name", myKbDS.get(i).get("name"));

		// Filter All Articles - Author = current user or status = published
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterAll();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countAll")+" FAILED",
				Integer.parseInt(myKbFS.get("countAll")), sugar().knowledgeBase.listView.countRows());
		for (int i = 0; i < 5; i++) 
			sugar().knowledgeBase.listView.verifyField(i+1, "name", myKbDS.get(i).get("name"));

		// Login with QAuser and repeat all filters
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		myKbFS.clear();
		myKbFS = testData.get(testName+"_2").get(1);

		// Nav to KB list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		// Sort Desc KB listview items by Article name
		// Sorting is needed because order is not consistent in list view
		sugar().knowledgeBase.listView.sortBy("headerName", false);

		// Filter My Articles
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterAssignedToMe();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countMy")+" FAILED",
				Integer.parseInt(myKbFS.get("countMy")), sugar().knowledgeBase.listView.countRows());
		sugar().knowledgeBase.listView.verifyField(1, "name", myKbDS.get(0).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", myKbDS.get(1).get("name"));

		// Filter My Favorites
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterMyFavorites();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countFav")+" FAILED",
				Integer.parseInt(myKbFS.get("countFav")), sugar().knowledgeBase.listView.countRows());

		// Filter Recently Viewed
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterRecentlyViewed();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countViewed")+" FAILED",
				Integer.parseInt(myKbFS.get("countViewed")), sugar().knowledgeBase.listView.countRows());

		// Filter Recently Created
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterRecentlyCreated();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countCreated")+" FAILED",
				Integer.parseInt(myKbFS.get("countCreated")), sugar().knowledgeBase.listView.countRows());
		for (int i = 0; i < 4; i++) 
			sugar().knowledgeBase.listView.verifyField(i+1, "name", myKbDS.get(i).get("name"));

		// Filter All Articles - Author = current user or status = published
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterAll();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = "+myKbFS.get("countAll")+" FAILED",
				Integer.parseInt(myKbFS.get("countAll")), sugar().knowledgeBase.listView.countRows());
		for (int i = 0; i < 5; i++) 
			sugar().knowledgeBase.listView.verifyField(i+1, "name", myKbDS.get(i).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}