package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21984 extends SugarTest {

	public void setup() throws Exception {	
		// Login in as admin
		sugar().login();
	}

	/**
	 * Import Leads_Verify that the mapping between sugar fields and the incoming fields in import file
	 * is defined automatically when using saved custom mapping.
	 * @throws exception
	 */
	@Test
	public void Leads_21984_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName + "_customData");

		// Going on the Lead module and selecting the import leads
		sugar().navbar.selectMenuItem(sugar().leads, "importLeads");
		// Need to change focus for bwc-frame
		VoodooUtils.focusFrame("bwc-frame");

		// Upload the CSV file
		// TODO: VOOD-1396 Need Controls for the Import Tasks functionality
		VoodooFileField browseToImport = new VoodooFileField("input", "id", "userfile");
		VoodooControl nextButton = new VoodooControl("input", "id", "gonext");
		String filePath = "src/test/resources/data/" + testName + ".csv";
		nextButton.click();
		browseToImport.set(filePath);
		VoodooUtils.waitForReady();
		nextButton.click();
		nextButton.click();

		// Defining the custom mapping fields
		for (int i = 2; i < 6; i++) {
			new VoodooControl("select", "css", String.format("#importTable tr:nth-child(%d) td:nth-child(2) .fixedwidth", i)).set(customData.get(i-2).get("changedFields"));
			VoodooUtils.waitForReady();
		}

		// Save import setting
		nextButton.click();
		new VoodooControl("input", "id", "save_map_as").set(testName);
		new VoodooControl("input", "id", "importnow").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		sugar().leads.navToListView();

		// Sorting to verify specific records.
		sugar().leads.listView.sortBy("headerFullname", true);
		VoodooUtils.waitForReady();
		DataSource importedData =  testData.get(testName);

		// Verify that the imported leads are correctly displayed in the leads list view
		for (int j = 1; j <= customData.size(); j++) {
			sugar().leads.listView.getDetailField(j, "fullName").assertEquals(importedData.get(j-1).get("lastName") + " " + importedData.get(j-1).get("firstName"), true);
			VoodooUtils.waitForReady();
		}

		// Selecting the custom saved mapping
		sugar().navbar.selectMenuItem(sugar().leads, "importLeads");
		VoodooUtils.focusFrame("bwc-frame");
		nextButton.click();
		// TODO: VOOD-1396 Need Controls for the Import Tasks functionality
		new VoodooFileField("input","css",".preset-row.preset-custom .preset-label input").click();
		VoodooUtils.waitForReady();

		// Import the file again
		browseToImport.set(filePath);
		VoodooUtils.waitForReady();

		// Move to the custom mapping fields
		nextButton.click();
		nextButton.click();

		// Verify the custom fields mappings as we have saved the mapping
		for (int i = 2; i < 6; i++) {
			new VoodooControl("td", "css", String.format(".detail.view tbody tr:nth-child(%d) td", i)).assertEquals(customData.get(i-2).get("headerName"), true);
			new VoodooControl("td", "css", String.format(".detail.view tbody tr:nth-child(%d) td:nth-child(2)", i)).assertContains(customData.get(i-2).get("changedFields"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}