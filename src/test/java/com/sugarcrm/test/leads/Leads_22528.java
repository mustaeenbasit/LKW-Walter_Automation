package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22528 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that the lead details can be viewed by clicking the Preview icon at the right edge of each lead record in list view
	 * @throws Exception
	 */
	@Test
	public void Leads_22528_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Go to "Leads" module
		sugar().leads.navToListView();
		
		// Click Preview icon at the right edge of a lead from leads list view
		sugar().leads.listView.previewRecord(1);
		
		// Verify that leads details are displayed in a Preview information box in the right part of the screen
		sugar().previewPane.getPreviewPaneField("fullName").assertEquals(sugar().leads.getDefaultData().get("fullName"), true);
		sugar().previewPane.getPreviewPaneField("title").assertEquals(sugar().leads.getDefaultData().get("title"), true);
		sugar().previewPane.showMore();
		sugar().previewPane.getPreviewPaneField("primaryAddressStreet").assertEquals(sugar().leads.getDefaultData().get("primaryAddressStreet"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}