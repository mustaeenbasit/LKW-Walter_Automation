package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28622 extends SugarTest {
	public void setup() throws Exception {
		// Create a record in a sidecar module, and in a BWC module
		sugar.tasks.api.create();
		sugar.quotes.api.create();
		sugar.login();
	}

	/**
	 * Verify user can search any record by name/subject with Sweet Spot
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28622_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Activate Sweetspot
		sugar.sweetspot.show();
		
		// Search for the created sidecar record
		sugar.sweetspot.search(sugar.tasks.getDefaultData().get("subject"));
		VoodooUtils.waitForReady();
		
		// Verify that Sweet Spot searches out the correct record
		sugar.sweetspot.getRecordsResult().assertContains(sugar.tasks.getDefaultData().get("subject"), true);
		
		// Click to go to the record
		sugar.sweetspot.clickRecordsResult();
		
		// Verify that user directed to the correct record
		sugar.tasks.recordView.getDetailField("subject").assertEquals(sugar.tasks.getDefaultData().get("subject"), true);

		// Activate Sweetspot
		sugar.sweetspot.show();
		
		// Search for the created BWC record
		sugar.sweetspot.search(sugar.quotes.getDefaultData().get("name"));
		VoodooUtils.waitForReady();
		
		// Verify that Sweet Spot searches out the correct record
		sugar.sweetspot.getRecordsResult().assertContains(sugar.quotes.getDefaultData().get("name"), true);
		
		// Click to go to the record
		sugar.sweetspot.clickRecordsResult();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that user directed to the correct record
		sugar.quotes.detailView.getDetailField("name").assertEquals(sugar.quotes.getDefaultData().get("name"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}