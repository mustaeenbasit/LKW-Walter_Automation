package com.sugarcrm.test.subpanels;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import org.junit.*;

public class Subpanels_19477 extends SugarTest {
	DataSource ds = new DataSource();
	FieldSet recordFs = new FieldSet();
	VoodooControl studioLinkQuotes, studioCtrl;

	public void setup() throws Exception {
		ds = testData.get(testName);
		recordFs = testData.get(testName + "_1").get(0);

		sugar().targets.api.create(ds);
		sugar().quotes.api.create();
		sugar().login();

		// Navigate to Admin Module
		sugar().navbar.navToAdminTools();
		// TODO: VOOD-542
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();
		studioLinkQuotes = new VoodooControl("a", "id", "studiolink_Quotes");
		studioLinkQuotes.click();

		// create a many to many relationship between Quotes and targets module so that targets subpanel will be available in Quotes detail view
		new VoodooControl("a", "css", "#relationshipsBtn tr:nth-child(2) a").click();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		new VoodooControl("select", "id", "relationship_type_field").set(recordFs.get("relationship"));
		VoodooUtils.waitForReady(30000);
		new VoodooControl("select", "id", "rhs_mod_field").set(recordFs.get("moduleName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='saverelbtn']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Set System settings
		FieldSet systemSettingsData = new FieldSet();
		systemSettingsData.put("maxEntriesPerSubPanel", recordFs.get("maxEntriesPerSubPanel"));
		sugar().admin.setSystemSettings(systemSettingsData);
	}

	/**
	 * Target List - Targets management_Verify that the "Previous" pagination function in the "Targets" sub-panel works correctly.
	 *
	 * @throws Exception
	 */
	@Test
	public void Subpanels_19477_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotes.navToListView();
		// Go to Detail view of document record
		sugar().quotes.listView.clickRecord(1);

		// Link record to targets subpanel
		// TODO: VOOD-1713
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "#whole_subpanel_quotes_prospects_1 .pagination .sugar_action_button .ab").click();
		new VoodooControl("a", "css", "#quotes_prospects_1_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusWindow(1);

		// TODO: VOOD-1193
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-779
		VoodooControl pageNumber = new VoodooControl("span", "css", "#list_subpanel_quotes_prospects_1 .pageNumbers");
		VoodooControl endLinkButton = new VoodooControl("button", "css", "#whole_subpanel_quotes_prospects_1 [name = listViewEndButton]");
		VoodooControl startLinkButton = new VoodooControl("button", "css", "#whole_subpanel_quotes_prospects_1 [name = listViewStartButton]");

		// click on the end link
		endLinkButton.click();
		VoodooUtils.waitForReady();

		// Verify that end link disabled after click
		Assert.assertEquals("End link is visible when it should not.", true, endLinkButton.isDisabled());
		// Verify that page number count changed
		pageNumber.assertEquals(recordFs.get("nextPageNumber"), true);

		// click on the start link
		startLinkButton.click();
		// Verify that start link disabled after click
		Assert.assertEquals("Start link is visible when it should not.", true, startLinkButton.isDisabled());
		// Verify that page number count changed
		pageNumber.assertEquals(recordFs.get("pageNumber"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}