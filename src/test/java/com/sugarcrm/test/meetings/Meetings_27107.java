package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27107 extends SugarTest {

	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that Default Dashlets are available in Meetings
	 * @throws Exception
	 */
	@Test
	public void Meetings_27107_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Meetings module 
		sugar().meetings.navToListView();
		DataSource meetingsData = testData.get(testName);

		// In listview, create a new Dashboard.  
		VoodooControl dashletTitleCtrl = sugar().meetings.dashboard.getControl("title");
		VoodooControl addDashlet = sugar().meetings.dashboard.getControl("addRow");
		sugar().meetings.dashboard.clickCreate();
		dashletTitleCtrl.set(meetingsData.get(0).get("dashlet_title"));
		addDashlet.click();

		// Click on Add Dashlets, look at the list of available dashlets.  
		sugar().meetings.dashboard.addDashlet(1, 1);

		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl learningCtrl = new VoodooControl("span", "css", ".list-view tr td span");
		VoodooControl listViewCtrl = new VoodooControl("span", "css", ".list-view tr:nth-child(2) td span");
		VoodooControl rssFeedCtrl = new VoodooControl("span", "css", ".list-view tr:nth-child(5) td span");
		VoodooControl twitterCtrl = new VoodooControl("span", "css", ".list-view tr:nth-child(7) td span");
		VoodooControl myActivityCtrl  = new VoodooControl("span", "css", ".list-view tr:nth-child(4) td span");
		VoodooControl webPageCtrl = new VoodooControl("span", "css", ".list-view tr:nth-child(8) td span");
		VoodooControl reportCtrl = new VoodooControl("span", "css", ".list-view tr:nth-child(6) td span");

		// Verify the availablibility of dashlets in dashlet list.
		learningCtrl.assertEquals(meetingsData.get(0).get("dashlet_name"), true);
		listViewCtrl.assertEquals(meetingsData.get(1).get("dashlet_name"), true);
		rssFeedCtrl.assertEquals(meetingsData.get(2).get("dashlet_name"), true);
		twitterCtrl.assertEquals(meetingsData.get(3).get("dashlet_name"), true);
		myActivityCtrl.assertEquals(meetingsData.get(4).get("dashlet_name"), true);
		webPageCtrl.assertEquals(meetingsData.get(5).get("dashlet_name"), true);
		reportCtrl.assertEquals(meetingsData.get(6).get("dashlet_name"), true);

		// Select "List View" in dashlets list
		VoodooControl dashletSearchBar = new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']");
		VoodooControl dashletSearch = new VoodooControl("a", "css", ".list.fld_title [data-placement='bottom'] a");
		VoodooControl saveButton = new VoodooControl("span", "css", "#drawers .fld_save_button");
		dashletSearchBar.set(meetingsData.get(1).get("dashlet_name"));
		dashletSearch.click();
		VoodooUtils.waitForReady();
		saveButton.click();
		VoodooUtils.waitForReady();

		// Save Dashboard.
		sugar().meetings.dashboard.save();

		// Verify Dashboard is saved with a "List View" of Meetings 
		VoodooControl dashletTitle = new VoodooControl("h4", "css", "[data-voodoo-name='dashlet-toolbar'] .dashlet-title");
		dashletTitle.assertContains(meetingsData.get(0).get("dashlet_title"), true);

		// Open meeting record view. 
		sugar().meetings.listView.clickRecord(1);

		// In record view, create a new Dashboard.  
		sugar().meetings.dashboard.clickCreate();
		dashletTitleCtrl.set(meetingsData.get(1).get("dashlet_title"));
		addDashlet.click();

		// Click on Add Dashlets, look at the list of available dashlets. 
		sugar().meetings.dashboard.addDashlet(1, 1);

		// Assert available dashlets in dashlet list.
		learningCtrl.assertEquals(meetingsData.get(0).get("dashlet_name"), true);
		listViewCtrl.assertEquals(meetingsData.get(1).get("dashlet_name"), true);
		rssFeedCtrl.assertEquals(meetingsData.get(2).get("dashlet_name"), true);
		twitterCtrl.assertEquals(meetingsData.get(3).get("dashlet_name"), true);
		myActivityCtrl.assertEquals(meetingsData.get(4).get("dashlet_name"), true);
		webPageCtrl.assertEquals(meetingsData.get(5).get("dashlet_name"), true);
		reportCtrl.assertEquals(meetingsData.get(6).get("dashlet_name"), true);

		// Add a "Saved Reports Chart Dashlet" Dashlet
		dashletSearchBar.set(meetingsData.get(6).get("dashlet_name"));
		VoodooUtils.waitForReady();
		dashletSearch.click();
		new VoodooSelect("a", "css", ".fld_saved_report_id.edit a").set(meetingsData.get(1).get("dashlet_title"));
		saveButton.click();
		VoodooUtils.waitForReady();

		// Save the  Dashboard. 
		sugar().meetings.dashboard.save();

		// Verify the Dashboard is saved with a "Meeting by team by user" report saved
		dashletTitle.assertContains(meetingsData.get(1).get("dashlet_title"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}