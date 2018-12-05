package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Roles_24701 extends SugarTest {
	FieldSet messagesData;

	public void setup() throws Exception {
		messagesData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Role management_Verify that current user cannot view the information when viewing a record whose assigned team does not include current logging user.
	 * @throws Exception
	 */
	@Test
	public void Roles_24701_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaigns module and click "Create Campaign (Classic)" link in shortcuts navigation
		sugar().campaigns.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().campaigns);
		sugar().campaigns.menu.getControl("createCampaignClassic").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Enter required fields
		sugar().campaigns.editView.getEditField("name").set(sugar().campaigns.getDefaultData().get("name"));
		sugar().campaigns.editView.getEditField("status").set(sugar().campaigns.getDefaultData().get("status"));
		sugar().campaigns.editView.getEditField("type").set(sugar().campaigns.getDefaultData().get("type"));
		sugar().campaigns.editView.getEditField("date_end").set(sugar().campaigns.getDefaultData().get("date_end"));

		// Select another team for this creating campaign, set newly added team as Primary and Save
		// TODO: VOOD-1072
		new VoodooControl("button", "id", "teamAdd").click();
		new VoodooControl("input", "id", "EditView_team_name_collection_1").set(messagesData.get("secondTeam"));
		new VoodooControl("div", "css", "#EditView_EditView_team_name_collection_1_results div").click();
		new VoodooControl("input", "id", "primary_team_name_collection_1").click();
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();

		// Verify that the User would find 'Global' and selected team(QAUser) being displayed as Teams
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl teameCtrl = sugar().campaigns.detailView.getDetailField("teams");
		teameCtrl.assertContains(messagesData.get("secondTeam"), true);
		teameCtrl.assertContains(messagesData.get("global"), true);
		VoodooUtils.focusDefault();

		// Logout and login as QAUser(or the user who is not included in selected team above)
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Campiagns and click to open detailed view of the campaign created above and click Edit button
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Click 'Show' link of the team, remove 'Global' team of the campaign and click Save
		// TODO: VOOD-1072
		new VoodooControl("span", "id", "more_div_EditView_team_name").click();
		new VoodooControl("input", "id", "remove_team_name_collection_1").click();
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the User would see this message on the page: 'Error retrieving record. This record may be deleted or you may not be authorized to view it.'
		sugar().campaigns.detailView.assertContains(messagesData.get("assertMessage"), true);
		VoodooUtils.focusDefault();

		// Click Campaigns module in the navigation bar
		sugar().campaigns.navToListView();

		// Verify that the User would not see the campaign now in the list
		sugar().campaigns.listView.getControl("checkbox02").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}