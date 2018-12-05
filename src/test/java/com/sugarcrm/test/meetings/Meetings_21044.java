package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21044 extends SugarTest {
	DataSource meetingsDS;

	public void setup() throws Exception {
		meetingsDS = testData.get(testName);
		sugar().meetings.api.create(meetingsDS);
		sugar().login();
		sugar().meetings.navToListView();
		sugar().meetings.listView.toggleFavorite(1);
		sugar().meetings.listView.editRecord(2);

		// TODO: VOOD-1330 - workaround on more appropriate scroll. Once fixed we need to change below line#22
		sugar().meetings.listView.getEditField(2, "assignedTo").scrollIntoViewIfNeeded(sugar().meetings.listView.getControl("horizontalScrollBar"), false);
		sugar().meetings.listView.getEditField(2, "assignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().meetings.listView.saveRecord(2);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify that scheduled meetings are searched with basic search condition.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21044_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl secondCheckboxCtrl = sugar().meetings.listView.getControl("checkbox02");
		VoodooControl thirdCheckboxCtrl = sugar().meetings.listView.getControl("checkbox03");
		// Verify assigned to me meeting records only
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterAssignedToMe();
		sugar().meetings.listView.verifyField(1, "name", meetingsDS.get(2).get("name"));
		sugar().meetings.listView.verifyField(2, "name", meetingsDS.get(0).get("name"));
		thirdCheckboxCtrl.assertVisible(false);

		// Verify favorite meeting records
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterMyFavorites();
		sugar().meetings.listView.verifyField(1, "name", meetingsDS.get(2).get("name"));
		secondCheckboxCtrl.assertVisible(false);
		thirdCheckboxCtrl.assertVisible(false);

		// Verify my scheduled meeting records
		sugar().meetings.listView.selectFilterMySchedule();
		sugar().meetings.listView.verifyField(1, "name", meetingsDS.get(2).get("name"));
		secondCheckboxCtrl.assertVisible(false);
		thirdCheckboxCtrl.assertVisible(false);

		// Verify search with name meeting record
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterAll();
		sugar().meetings.listView.setSearchString(meetingsDS.get(1).get("name"));
		sugar().meetings.listView.verifyField(1, "name", meetingsDS.get(1).get("name"));
		secondCheckboxCtrl.assertVisible(false);
		thirdCheckboxCtrl.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}