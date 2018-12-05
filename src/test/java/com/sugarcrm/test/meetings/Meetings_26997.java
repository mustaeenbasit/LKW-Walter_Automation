package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_26997 extends SugarTest {
	DataSource meetingData;

	public void setup() throws Exception {
		// Multiple meeting records
		meetingData = testData.get(testName);
		sugar().meetings.api.create(meetingData);
		sugar().login();

		// Assigned To QAuser in one of meeting record
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("assignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().meetings.recordView.save();

		// Mark Favorite
		sugar().meetings.navToListView();
		sugar().meetings.listView.toggleFavorite(2);
	}

	/**
	 * Verify that default filter works correctly in Meetings list view 
	 * @throws Exception
	 */
	@Test
	public void Meetings_26997_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.listView.openFilterDropdown();

		// Verify assigned to the user are displayed (i.e Admin user)
		sugar().meetings.listView.selectFilterAssignedToMe();
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.verifyField(1, "name", meetingData.get(1).get("name"));
		sugar().meetings.listView.verifyField(2, "name", meetingData.get(0).get("name"));

		// Verify favorite records
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterMyFavorites();
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.verifyField(1, "name", meetingData.get(1).get("name"));

		// Verify all records
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterAll();
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.verifyField(1, "name", meetingData.get(2).get("name"));
		sugar().meetings.listView.verifyField(2, "name", meetingData.get(1).get("name"));
		sugar().meetings.listView.verifyField(3, "name", meetingData.get(0).get("name"));

		// Verify recently created records
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterRecentlyCreated();
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.verifyField(1, "name", meetingData.get(2).get("name"));
		sugar().meetings.listView.verifyField(2, "name", meetingData.get(1).get("name"));
		sugar().meetings.listView.verifyField(3, "name", meetingData.get(0).get("name"));

		// Verify recently view records
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterRecentlyViewed();
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.listView.verifyField(1, "name", meetingData.get(2).get("name"));

		// Reset filter to defaults
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterAll();
		sugar().alerts.waitForLoadingExpiration();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}