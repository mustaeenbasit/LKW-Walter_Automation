package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_28895 extends SugarTest {
	public void setup() throws Exception {
		sugar().notes.api.create();

		// Login as Admin user
		sugar().login();
	}

	/**
	 * Tool tip details should be shown properly in the Activity Stream for the updated Notes
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_28895_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Notes record view and edit "Assigned to" field for the above created note
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.showMore();
		sugar().notes.recordView.edit();
		sugar().notes.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().notes.recordView.save();

		// Go to Activity stream
		sugar().notes.recordView.showActivityStream();

		// From Filter -> Select Messages for Update
		// TODO: VOOD-474
		new VoodooControl("a", "css", ".filter-view.search .table-cell:nth-child(2) .search-filter a").click();
		new VoodooControl("li", "css", "#select2-drop .select2-results li:nth-child(6)").click();
		VoodooUtils.waitForReady();

		FieldSet activityStreamMessageData = testData.get(testName).get(0);

		// Hover mouse on "Assigned User Id"
		// TODO: VOOD-474
		new VoodooControl("a", "css", ".activitystream-list.results > li div .tagged a").hover();

		// Verify that the correct tool tip should be shown for the Changed user.
		// TODO: VOOD-1292
		new VoodooControl("div", "css", ".tooltip-inner").assertEquals(activityStreamMessageData.get("updatedMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}