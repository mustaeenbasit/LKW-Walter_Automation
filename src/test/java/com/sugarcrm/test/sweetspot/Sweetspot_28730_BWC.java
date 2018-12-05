package com.sugarcrm.test.sweetspot;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Ignore;
import org.junit.Test;

public class Sweetspot_28730_BWC extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify user can call Sweet Spot (with correct key combo),
	 * AND config drawer opens/closes correctly in bwc modules.
	 * @throws Exception
	 */
	@Ignore("SC-4407: [Sweet Spot] window is not dismissed while clicking outside on iframe(Bwc-Module)")
	@Test
	public void Sweetspot_28730_BWC_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Navigate to Quotes List View and Open SweetSpot
		sugar.quotes.navToListView();
		sugar.sweetspot.show();

		// Verify sweetspot is appeared on the ListView
		sugar.sweetspot.getControl("searchBox").assertVisible(true);

		// Click anywhere outside the search box
		// TODO: VOOD-1892
		VoodooUtils.focusDefault();
		sugar().quotes.listView.clearSearchForm();

		// Verify Sweetspot disappears
		sugar.sweetspot.getControl("searchBox").assertVisible(false);

		// Open sweetspot again
		sugar.sweetspot.show();
		sugar.sweetspot.configure();

		// Verify Configure page appears
		sugar.sweetspot.assertConfigurePage();
		sugar.alerts.getError().assertVisible(false);

		// Cancel the sweetspot configuration page
		sugar.sweetspot.cancelConfiguration();

		// Verify Sweetspot Configuration page disappears
		sugar.sweetspot.getControl("cancelConfiguration").assertVisible(false);

		// Make some Changes in the Configuration Page
		String quotes = customData.get("keyword");
		sugar.sweetspot.addKeyword(sugar.quotes.moduleNamePlural,quotes );

		// Verify Sweetspot disappears and changes are saved
		sugar.sweetspot.getControl("searchBox").assertVisible(false);
		sugar.sweetspot.show();
		sugar.sweetspot.configure();
		sugar.sweetspot.getControl("hotkeysAction").assertEquals(sugar.quotes.moduleNamePlural, true);
		sugar.sweetspot.getControl("hotkeysKeyword").assertEquals(quotes, true);
		sugar.sweetspot.cancelConfiguration();

		// Open sweetspot again
		sugar.sweetspot.show();
		sugar.sweetspot.search(customData.get("searchString"));
		sugar.sweetspot.getActionsResult().assertContains(customData.get("assertString"),true);

		// Click on the result listed
		sugar.sweetspot.clickActionsResult(1);

		// Verify sweetspot configuration page opens
		sugar.sweetspot.assertConfigurePage();
		sugar.sweetspot.cancelConfiguration();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}