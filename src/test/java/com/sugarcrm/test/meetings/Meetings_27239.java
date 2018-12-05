package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27239 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that meeting preview correctly for non-recurring meeting from list view
	 * @throws Exception
	 */
	@Test
	public void Meetings_27239_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Goto List view
		sugar().meetings.navToListView();

		// TODO: VOOD-498 - Need ListView functionality for all row actions
		sugar().meetings.listView.previewRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		// Confirm RHS Meeting Preview
		// TODO: VOOD-1217
		new VoodooControl("div", "css", "div.preview-headerbar[data-voodoo-name='preview-header']").assertVisible(true);
		new VoodooControl("div", "css", "div.preview-data span.fld_picture.detail").assertContains("Me", true);;
		sugar().previewPane.getPreviewPaneField("name").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("status").assertVisible(true);
		new VoodooControl("div", "css", "div.preview-data span.fld_duration.detail").assertContains(sugar().meetings.getDefaultData().get("date_start_date"), true);
		new VoodooControl("div", "css", "div.preview-data span.fld_location.detail").assertContains(sugar().meetings.getDefaultData().get("location"), true);
		new VoodooControl("div", "css", "div.preview-data span.fld_description.detail").assertContains(sugar().meetings.getDefaultData().get("description"), true);
		new VoodooControl("div", "css", "div.preview-data span.fld_invitees.detail").assertContains("Administrator", true);
		new VoodooControl("div", "css", "div.preview-data span.fld_assigned_user_name.detail").assertContains("Administrator", true);
		new VoodooControl("div", "css", "div.preview-data span.fld_team_name.detail").assertContains("Global", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}