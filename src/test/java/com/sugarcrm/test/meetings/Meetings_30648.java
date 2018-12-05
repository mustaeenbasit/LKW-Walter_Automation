package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_30648 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		
		// Login with Admin
		sugar().login();
		
		// Navigate to system settings
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Enable the Preview Editing checkbox
		// TODO: VOOD-1903 Additional System Settings support
		new VoodooControl("input", "css", "input[name='preview_edit'].checkbox").click();
		
		// Save change settings
		sugar().admin.systemSettings.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Verify the "Meeting type" is a drop down field in Meeting edit preview pane.
	 * @throws Exception
	 */
	@Test
	public void Meetings_30648_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to the meetings module
		sugar().meetings.navToListView();
		
		// Click on Preview icon
		sugar().meetings.listView.previewRecord(1);
		// TODO: VOOD-2064 Need Lib support for Preview Pane's Edit View controls.
		new VoodooControl("i", "css", "i[class='fa fa-pencil']").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2064 Need Lib support for Preview Pane's Edit View controls.
		// Verify Meeting Type is a drop-down
		new VoodooControl("div", "css", "div[name='type'] span > div > a").assertAttribute("class", "select2-choice");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}