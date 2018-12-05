package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27218 extends SugarTest {

	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().users.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that user is able to preview the record in meeting invite list
	 * @throws Exception
	 */
	@Test
	public void Meetings_27218_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to meetings record view and click edit
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Selecting contact
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().contacts.getDefaultData().get("lastName"));		

		// Selecting Lead
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().leads.getDefaultData().get("lastName"));

		// Add user in guest list
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().users.getDefaultData().get("lastName"));
		sugar().meetings.recordView.save();

		// Open the newly created meeting record
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);

		// Verify Preview button is disabled for BWC module(Admin)
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		VoodooControl previewBtnForAdmin = new VoodooControl("button", "css", ".fld_invitees .participant div:nth-child(3) button");
		Assert.assertTrue("Preview button is enabled for BWC module Admin", previewBtnForAdmin.isDisabled());

		// "Legacy modules cannot be previewed" tooltip should be displayed when hovering over Admin preview button. 
		previewBtnForAdmin.hover();
		VoodooUtils.waitForReady();
		VoodooControl previewTooltip = new VoodooControl("div", "css", ".tooltip .tooltip-inner");
		previewTooltip.assertEquals(testData.get(testName).get(0).get("bwc_preview_text"), true);

		// Verify Preview button is disabled for BWC module(Users)
		VoodooControl previewBtnForUsers = new VoodooControl("button", "css", ".fld_invitees div div:nth-child(7).participant div:nth-child(3) button");
		Assert.assertTrue("Preview button is enabled for BWC module Users", previewBtnForUsers.isDisabled());

		// "Legacy modules cannot be previewed" tooltip should be displayed when hovering over Users preview button. 
		previewBtnForAdmin.hover();
		VoodooUtils.waitForReady();
		previewTooltip.assertEquals(testData.get(testName).get(0).get("bwc_preview_text"), true);

		// Click Leads preview button
		new VoodooControl("button", "css", ".fld_invitees div div:nth-child(5).participant div:nth-child(3) button").click();
		VoodooUtils.waitForReady();

		// Confirm RHS Leads view
		// TODO: VOOD-976 - Need lib support of RHS on record view
		sugar().previewPane.assertVisible(true); 
		VoodooControl fullNameCtrl = new VoodooControl("a", "css", ".preview-pane .fld_full_name a");
		fullNameCtrl.assertEquals(sugar().leads.getDefaultData().get("fullName"), true);
		sugar().previewPane.closePreview();

		// Click Contacts preview button
		new VoodooControl("button", "css", ".fld_invitees div div:nth-child(9).participant div:nth-child(3) button").click();
		VoodooUtils.waitForReady();

		// Confirm RHS Contacts view
		sugar().previewPane.assertVisible(true);
		fullNameCtrl.assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}