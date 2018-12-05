package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TeamRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_30174 extends SugarTest {
	TeamRecord myCustomTeam;

	public void setup() throws Exception {
		// Create a team named Team1
		myCustomTeam = (TeamRecord) sugar().teams.api.create();

		// Create any type of record in any module.
		sugar().leads.api.create();

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Verify Team should be deleted without asking for re-assigned the record when record already assigned to other team.
	 *
	 * @throws Exception
	 */
	@Test
	public void Teams_30174_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// The created record should have Global as default team and the custom created Team
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.edit();

		// TODO: VOOD-518 and VOOD-1169
		new VoodooControl("button", "css", ".fld_team_name button[name='add']").click();
		new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(2) a.select2-choice").set(myCustomTeam.getRecordIdentifier());

		// Save the record
		sugar().leads.recordView.save();

		// Edit the record and remove custom team from Teams
		sugar().leads.recordView.edit();
		// TODO: VOOD-518 and VOOD-1169
		new VoodooControl("button", "css", ".fld_team_name div.control-group:nth-child(2) button[name='remove']").click();
		// Save the record
		sugar().leads.recordView.save();

		// Navigate to Team Management module and try to delete cusom team
		myCustomTeam.navToRecord();
		VoodooUtils.waitForReady();
		sugar().teams.detailView.delete();
		VoodooUtils.acceptDialog();

		// Verify that the Sugar instance not navigates the user to Re-assigned page to re-assign the record(s) to another team.
		new VoodooControl("form", "id", "reassign_team").assertExists(false);
		new VoodooControl("input", "css", "#reassign_team [title='Reassign']").assertExists(false);

		// Clear the Teams list view
		VoodooUtils.focusDefault();
		sugar().teams.listView.clearSearchForm();
		sugar().teams.listView.submitSearchForm();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the team should be deleted
		sugar().teams.listView.assertContains(myCustomTeam.getRecordIdentifier(), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}