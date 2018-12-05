package com.sugarcrm.test.teams;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TeamRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_30172 extends SugarTest {
	TeamRecord myCustomTeam;

	public void setup() throws Exception {
		// Create a team named Team1
		myCustomTeam = (TeamRecord) sugar().teams.api.create();
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
	public void Teams_30172_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.edit();

		// set custom team as primary
		// TODO: VOOD-518 and VOOD-1169
		new VoodooControl("button", "css", ".fld_team_name button[name='add']").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(2) a.select2-choice").set(myCustomTeam.getRecordIdentifier());
		new VoodooControl("button", "css", ".record-cell.edit div:nth-child(2) button.btn.third").click();
		// remove Global team
		new VoodooControl("i", "css", ".fld_team_name.edit [name='remove'] i").click();

		// Save the record
		sugar().leads.recordView.save();

		// Edit the record and remove custom team from Teams
		sugar().leads.recordView.edit();
		// TODO: VOOD-518 and VOOD-1169
		new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(2) a.select2-choice").set(fs.get("teamName"));
		// Save the record
		sugar().leads.recordView.save();

		// Navigate to Team Management module and try to delete custom team
		myCustomTeam.navToRecord();
		VoodooUtils.waitForReady();
		sugar().teams.detailView.delete();
		VoodooUtils.acceptDialog();

		// Verify that the Sugar instance not navigates the user to Re-assigned page to re-assign the record(s) to another team.
		new VoodooControl("form", "id", "reassign_team").assertExists(false);
		new VoodooControl("input", "css", "#reassign_team [title='Reassign']").assertExists(false);

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