package com.sugarcrm.test.tags;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_29990 extends SugarTest {
	public void setup() throws Exception {
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify that when moving tags from hidden to default, Previous record should display in list view 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_29990_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Navigate to calls -> Layouts -> Listview
		// TODO: VOOD-542 - Need lib support for studio
		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		new VoodooControl("a", "id", "studiolink_Calls").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Moving Tags to default list
		VoodooControl tagHidden = new VoodooControl("li", "css", "[data-name='tag']");
		VoodooControl defaultList = new VoodooControl("li", "css", "[data-name='date_start']");
		tagHidden.dragNDrop(defaultList);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to Calls list view and asserting Tags column
		// TODO: VOOD-845 - Need defined control for list view column title row
		sugar().calls.navToListView();
		new VoodooControl("th", "css", "[data-fieldname='tag']").assertExists(true);
		Assert.assertTrue("Record count is not equal to 1 on List view", sugar().calls.listView.countRows() == 1);

		// Adding value to tag field in record view
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.showMore();
		sugar().calls.recordView.getEditField("tags").set(testName);
		sugar().calls.recordView.save();

		// Verifying the 'Tag' value in list view
		sugar().calls.navToListView();
		sugar().calls.listView.getDetailField(1, "tags").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}