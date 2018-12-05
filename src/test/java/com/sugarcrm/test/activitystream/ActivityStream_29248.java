package com.sugarcrm.test.activitystream;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class ActivityStream_29248 extends SugarTest {
	FieldSet customSettings = new FieldSet();
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		// Create a Lead Record
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that "More Posts" option is appearing in Preview Pane
	 * 
	 */
	@Test
	public void ActivityStream_29248_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Perform some updations
		sugar().leads.navToListView();
		sugar().leads.listView.editRecord(1);
		// Update status
		customData = testData.get(testName);
		sugar().leads.listView.getEditField(1, "status").set(customData.get(0).get("status"));
		// Update Office Phone
		sugar().leads.listView.getEditField(1, "phoneWork").set(customData.get(0).get("phoneWork"));
		// Save
		sugar().leads.listView.saveRecord(1);

		// Navigate to System Settings
		customSettings.put("maxEntriesPerPage", customData.get(0).get("numberOfEntries"));
		sugar().admin.setSystemSettings(customSettings);

		// Navigate to Leads list view
		sugar().leads.navToListView();

		// Preview
		sugar().leads.listView.previewRecord(1);

		// Verify More Posts" option should be available on Preview pane.
		// TODO: VOOD-1944
		VoodooControl morePostsCtrl = new VoodooControl("button", "css", ".more.padded");
		morePostsCtrl.assertVisible(true);
		morePostsCtrl.assertEquals(customData.get(0).get("morePostsText"), true);

		// Go to Activity Stream.
		sugar().leads.listView.showActivityStream();

		// Click on the Preview option of Activity Stream.
		sugar().leads.listView.activityStream.togglePreviewButton(1);

		// Verify "More Posts" option should be available on Preview pane.
		morePostsCtrl.assertVisible(true);
		morePostsCtrl.assertEquals(customData.get(0).get("morePostsText"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}