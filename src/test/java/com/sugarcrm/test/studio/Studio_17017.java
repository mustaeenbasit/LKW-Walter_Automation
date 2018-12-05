package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_17017 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that TextField type field has default max width of 255 characters
	 * in Studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_17017_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();

		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-517 Create Studio Module (BWC) - will provide the
		// references to replace these explicit VoodooControls

		new VoodooControl("a", "id", "studio").click();

		new VoodooControl("a", "id", "studiolink_Accounts").click();

		new VoodooControl("a", "css", "td#fieldsBtn a").click();

		new VoodooControl("button", "name", "addfieldbtn").click();

		new VoodooControl("select", "id", "type").assertContains("TextField",
				true);

		new VoodooControl("input", "id", "field_len").assertContains("255",
				true);

		new VoodooControl("button", "name", "cancelbtn").click();

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
