package com.sugarcrm.test.sweetspot;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Sweetspot_29232 extends SugarTest {
	public void setup() throws Exception {
		// Login as admin
		sugar().login();
	}

	/**
	 * Check Global keyboard shortcut for Sweet Spot
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_29232_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet fs = testData.get(testName).get(0);

		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		// Cannot press "?" on the keyboard to call the Keyboard Shortcuts Drawer as of now
		new VoodooControl("button", "css", "button[data-action='shortcuts']").click();
		VoodooUtils.waitForReady();

		// Verify that Keyboard Shortcuts page is open
		new VoodooControl("div", "css", ".fld_title div").assertEquals(fs.get("key"), true);

		// Verify that the shortcut for Sweet Spot is listed as 'mod+shift+space' under Global Shortcuts
		new VoodooControl("td", "css", "tbody[data-render='global'] tr:last-child td:first-child").assertEquals(fs.get("shortcut"), true);
		new VoodooControl("td", "css", "tbody[data-render='global'] tr:last-child td:last-child").assertEquals(fs.get("toggleShortcut"), true);

		// Activate Sweetspot
		sugar().sweetspot.show();

		sugar().sweetspot.getControl(fs.get("bar")).assertVisible(true);

		sugar().sweetspot.hide();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}