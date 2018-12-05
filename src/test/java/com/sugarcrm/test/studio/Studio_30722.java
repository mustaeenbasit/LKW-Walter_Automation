package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_30722 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Restore" button on preview page(View History) is clickable.
	 * @throws Exception
	 */
	@Test
	public void Studio_30722_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-514 -> need lib support for studio
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Contacts").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1511
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#Buttons tr td:nth-of-type(2) a").click();
		VoodooUtils.waitForReady();
		
		// Moving favorite field from hidden to default
		new VoodooControl("li", "css", "#Hidden [data-name='my_favorite']").dragNDrop(new VoodooControl("li", "css", "#Default [data-name='status']"));
		VoodooControl saveAndDeployButton = new VoodooControl("input", "css", "[name='savebtn']");
		
		// Save and Deploy
		saveAndDeployButton.click();
		VoodooUtils.waitForReady();
		
		// Click on view history
		new VoodooControl("input", "id", "historyBtn").click();
		VoodooUtils.waitForReady();
		
		// Click on preview button in opened container
		new VoodooControl("input", "css", "#histWindow [value='Preview']").click();
		VoodooControl previewPageRestoreButton = new VoodooControl("input", "css", ".button[value='Restore']");
		
		// Verifying restore button is clickable on preview page
		previewPageRestoreButton.assertAttribute(customData.get("attribute"), customData.get("attributeValue"), true);
		
		// Clicking on restore button of preview page
		previewPageRestoreButton.click();
		VoodooUtils.waitForReady();
		
		// Save and Deploy
		new VoodooControl("input", "css", "[name='savebtn']").click();
		VoodooUtils.waitForReady();
		
		// Closing the container of history
		new VoodooControl("a", "class", "container-close").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}