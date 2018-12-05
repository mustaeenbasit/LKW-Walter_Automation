package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Notes_17525 extends SugarTest {
	
	VoodooControl filterDropdown, allActivityStreamCtrl;
	
	public void setup() throws Exception {
		sugar().login();
		sugar().notes.create();
	}

	/**
	 * Verify no error occurs with filter dropdown if module doesn't contain the sub panel layout metadata.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_17525_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-955 need defined control of filter dropdown on activity stream page
		filterDropdown = new VoodooControl("div", "css", ".select2-container.select2.search-filter");
		VoodooControl msgForCreateCtrl = new VoodooControl("li", "css", ".select2-drop-active .select2-results li:nth-of-type(2)");
		
		VoodooControl dataViewButton = sugar().notes.recordView.getControl("dataViewButton");
		VoodooControl activityStreamButton = sugar().notes.recordView.getControl("activityStreamButton");
		
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.activityStream.assertVisible(true);
		dataViewButton.assertAttribute("class", "active", false);
		activityStreamButton.assertAttribute("class", "active", true);
		
		filterDropdown.click();
		msgForCreateCtrl.click();
		
		// Verify that by Applying filter does not change the View
		dataViewButton.assertAttribute("class", "active", false);
		activityStreamButton.assertAttribute("class", "active", true);
		sugar().notes.recordView.activityStream.assertContains("Created "+sugar().notes.defaultData.get("subject"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}