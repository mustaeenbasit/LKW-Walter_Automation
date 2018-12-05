package com.sugarcrm.test.sweetspot;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Sweetspot_28730_Sidecar extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify user can call Sweet Spot (with correct key combo),
	 * AND config drawer opens/closes correctly in sidecar modules
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28730_Sidecar_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to Accounts List View and Open SweetSpot
		sugar.accounts.navToListView();
		sugar.sweetspot.show();

		// Verify sweetspot is appeared on the Accounts ListView
		sugar.sweetspot.getControl("searchBox").assertVisible(true);

		// Click anywhere outside the search box
		sugar().accounts.listView.sortBy("headerName", true);

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
		String account = customData.get("keyword");
		String accountName = sugar.accounts.moduleNamePlural;
		sugar.sweetspot.addKeyword(accountName,account );

		// Verify Sweetspot disappears and changes are saved
		sugar.sweetspot.getControl("searchBox").assertVisible(false);
		sugar.sweetspot.show();
		sugar.sweetspot.configure();
		sugar.sweetspot.getControl("hotkeysAction").assertEquals(accountName, true);
		sugar.sweetspot.getControl("hotkeysKeyword").assertEquals(account, true);
		sugar.sweetspot.cancelConfiguration();

		// Open Sweetspot again
		sugar.sweetspot.show();
		sugar.sweetspot.search(customData.get("search_String"));
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