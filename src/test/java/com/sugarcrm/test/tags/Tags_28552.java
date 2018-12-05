package com.sugarcrm.test.tags;
import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28552 extends SugarTest {
	DataSource tagsData = new DataSource();
	String accountName, contactName, oppName, leadsName, targetName, callName, meetingName, noteName, caseName, bugName;

	public void setup() throws Exception {
		tagsData = testData.get(testName);

		// Create tag records
		sugar().tags.api.create(tagsData);

		// Login
		sugar().login();

		// Enable KB and Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// TODO: VOOD-1772
		VoodooControl tagsEditCtrl = new VoodooControl("input", "css", ".fld_tag.edit .select2-input");
		accountName = sugar().accounts.getDefaultData().get("name");
		contactName = sugar().contacts.getDefaultData().get("lastName");
		oppName = sugar().opportunities.getDefaultData().get("name");
		leadsName = sugar().leads.getDefaultData().get("lastName");
		targetName = sugar().targets.getDefaultData().get("lastName");
		callName = sugar().calls.getDefaultData().get("name");
		meetingName = sugar().meetings.getDefaultData().get("name");
		noteName = sugar().notes.getDefaultData().get("subject");
		caseName = sugar().cases.getDefaultData().get("name");
		bugName = sugar().bugs.getDefaultData().get("name");

		// TODO: VOOD-444
		// Create several tags across different modules. Such as "tag1" in Account, Contact, Opp., Lead, KB and Target records
		// Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("name").set(accountName);
		sugar().accounts.createDrawer.getEditField("tags").set(tagsData.get(0).get("name"));
		sugar().accounts.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Contacts
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.showMore();
		sugar().contacts.createDrawer.getEditField("lastName").set(contactName);
		sugar().contacts.createDrawer.getEditField("tags").set(tagsData.get(0).get("name"));
		sugar().contacts.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Opportunity
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.showMore();
		sugar().opportunities.createDrawer.getEditField("name").set(oppName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(accountName);
		// TODO: CB-252, VOOD-1437
		tagsEditCtrl.set(tagsData.get(0).get("name")  + '\uE007');
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.getDefaultData().get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.getDefaultData().get("rli_likely"));
		sugar().opportunities.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Lead
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.getEditField("lastName").set(leadsName);
		sugar().leads.createDrawer.getEditField("tags").set(tagsData.get(0).get("name"));
		sugar().leads.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// KB
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("tags").set(tagsData.get(0).get("name"));
		sugar().knowledgeBase.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Target
		sugar().targets.navToListView();
		sugar().targets.listView.create();
		sugar().targets.createDrawer.showMore();
		sugar().targets.createDrawer.getEditField("lastName").set(targetName);
		sugar().targets.createDrawer.getEditField("tags").set(tagsData.get(0).get("name"));
		sugar().targets.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// "tag2" in Call and Meeting records
		// Calls
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.showMore();
		sugar().calls.createDrawer.getEditField("name").set(callName);
		sugar().calls.createDrawer.getEditField("tags").set(tagsData.get(1).get("name"));
		sugar().calls.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Meetings
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.showMore();
		sugar().meetings.createDrawer.getEditField("name").set(meetingName);
		sugar().meetings.createDrawer.getEditField("tags").set(tagsData.get(1).get("name"));
		sugar().meetings.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// "tag3" in Note record
		sugar().notes.navToListView();
		sugar().notes.listView.create();
		sugar().notes.createDrawer.showMore();
		sugar().notes.createDrawer.getEditField("subject").set(noteName);
		// TODO: CB-252, VOOD-1437
		tagsEditCtrl.set(tagsData.get(2).get("name")  + '\uE007');
		sugar().notes.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// "tag4" in Case record
		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().cases.createDrawer.showMore();
		sugar().cases.createDrawer.getEditField("name").set(caseName);
		sugar().cases.createDrawer.getEditField("relAccountName").set(accountName);
		sugar().cases.createDrawer.getEditField("tags").set(tagsData.get(3).get("name"));
		sugar().cases.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// "tag5" in Bug record
		sugar().bugs.navToListView();
		sugar().bugs.listView.create();
		sugar().bugs.createDrawer.showMore();
		sugar().bugs.createDrawer.getEditField("name").set(bugName);
		// TODO: CB-252, VOOD-1437
		tagsEditCtrl.set(tagsData.get(4).get("name")  + '\uE007');
		sugar().bugs.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * [Global Search and Tags] Verify Tags on Global Search is working properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28552_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter search query such as "tag" in the search bar
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		VoodooUtils.waitForReady(); // Wait needed
		globalSearchCtrl.set(tagsData.get(0).get("name").substring(0, 3));
		VoodooUtils.waitForReady();

		// Define controls for search dropdown
		// TODO: VOOD-1774
		VoodooControl tagRibbonCtrl = new VoodooControl("span", "css", ".quicksearch-tags.dropdown-menu .qs-tag-icon");
		VoodooControl tagTextRowCtrl = new VoodooControl("div", "css", ".quicksearch-tags.dropdown-menu");
		VoodooControl tagRowCtrl = new VoodooControl("div", "css", ".quicksearch-tags.dropdown-menu .qs-tag a");

		// Verify the results will show "tag1", "tag2", "tag3", "tag4", "tag5" are shown in the tag ribbon
		tagRibbonCtrl.assertVisible(true);
		tagTextRowCtrl.assertContains(tagsData.get(0).get("name"), true);
		tagTextRowCtrl.assertContains(tagsData.get(1).get("name"), true);
		tagTextRowCtrl.assertContains(tagsData.get(2).get("name"), true);
		tagTextRowCtrl.assertContains(tagsData.get(3).get("name"), true);
		tagTextRowCtrl.assertContains(tagsData.get(4).get("name"), true);

		// Choose a tag from the tags ribbon, such as "tag1"
		globalSearchCtrl.set(tagsData.get(0).get("name"));
		VoodooUtils.waitForReady();
		tagRowCtrl.click();
		VoodooUtils.waitForReady();

		// Verify results returned for record matches with tag matches. In the quick search panel, it should displayed five records
		// TODO: VOOD-1849 and VOOD-1868
		Assert.assertTrue("Not showing 5 records in the Search result dropdown", new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass() == 5);
		VoodooControl searchDropdownResultCtrl = sugar().navbar.search.getControl("searchResults");
		// verify that not other record that not containing tag1 are not exist
		searchDropdownResultCtrl.assertContains(callName, false);
		searchDropdownResultCtrl.assertContains(meetingName, false);
		searchDropdownResultCtrl.assertContains(noteName, false);
		searchDropdownResultCtrl.assertContains(caseName, false);
		searchDropdownResultCtrl.assertContains(bugName, false);

		// Verify that "View all results" link displayed underneath those records
		VoodooControl viewAllResultsBtnCtrl = sugar().navbar.search.getControl("viewAllResults");
		viewAllResultsBtnCtrl.assertExists(true);

		// Click on "View all results" in quick search panel
		viewAllResultsBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that in Global search page, it should displayed all six records that match "tag1"
		// TODO: VOOD-1848 and VOOD-1868
		VoodooControl globalSearchResultPageCtrl = new VoodooControl("li", "css", ".layout_simple .nav.search-results");
		Assert.assertTrue("Not show 6 records in the Search result dropdown", new VoodooControl("li", "css", ".layout_simple .nav.search-results .search-result").countWithClass() == 6);
		globalSearchResultPageCtrl.assertContains(accountName, true);
		globalSearchResultPageCtrl.assertContains(contactName, true);
		globalSearchResultPageCtrl.assertContains(oppName, true);
		globalSearchResultPageCtrl.assertContains(leadsName, true);
		globalSearchResultPageCtrl.assertContains(testName, true);
		globalSearchResultPageCtrl.assertContains(targetName, true);

		// Type in another search query "tag2" next to the "tag1" and select tag2
		globalSearchCtrl.set(tagsData.get(1).get("name"));
		VoodooUtils.waitForReady();

		// Assert tag ribbon and choose "tag2"
		tagRibbonCtrl.assertVisible(true);
		tagTextRowCtrl.assertContains(tagsData.get(1).get("name"), true);
		VoodooUtils.waitForReady();
		tagRowCtrl.click();
		VoodooUtils.waitForReady();

		// Verify results for record matches with tag matches "tag1" and "tag2". Verify the results with records that have "tag1" and "tag2"
		// TODO: VOOD-1849 and VOOD-1868
		Assert.assertTrue("Not showing 5 records in the Search result dropdown", new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass() == 5);
		// verify that not other record that not containing tag1 are not exist
		searchDropdownResultCtrl.assertContains(noteName, false);
		searchDropdownResultCtrl.assertContains(caseName, false);
		searchDropdownResultCtrl.assertContains(bugName, false);

		// Click on a record in tag result in the quick search result
		sugar().navbar.clickSearchResult(1);

		// Verify that the Quick Search will then display records that have that either of the tag. the record should not be without tag in the list
		// TODO: VOOD-1772
		VoodooControl tagsDetailFieldCtrl = new VoodooControl("a", "css", ".detail.fld_tag a");
		if(tagsDetailFieldCtrl.queryContains(tagsData.get(0).get("name"), true))
			tagsDetailFieldCtrl.queryContains(tagsData.get(1).get("name"), false);
		else
			tagsDetailFieldCtrl.queryContains(tagsData.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}