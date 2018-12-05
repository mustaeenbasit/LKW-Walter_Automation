package com.sugarcrm.test.sweetspot;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Sweetspot_29095 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user can ESC out of Sweet Spot on a drawer
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_29095_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to list view to Open a drawer
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();

		// Verifying sweetspot is not present
		VoodooControl sweetspotBarCtrl = sugar().sweetspot.getControl("sweetspotBar");
		sweetspotBarCtrl.assertVisible(false);

		// Call sweetspot
		sugar().sweetspot.show();

		// Verifying sweetspot is present
		sweetspotBarCtrl.assertVisible(true);

		// Press ESC on the keyboard
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		sugar().sweetspot.getControl("searchBox").set(""+'\uE00C');
		VoodooUtils.waitForReady();
		
		// Verifying sweetspot is not present
		sweetspotBarCtrl.assertVisible(false);

		// Press ESC on the keyboard again
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		sugar().accounts.createDrawer.getEditField("website").set(""+'\uE00C');
		VoodooUtils.waitForReady();

		// Verifying create drawer is not present, verifying save button
		sugar().accounts.createDrawer.getControl("saveButton").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
