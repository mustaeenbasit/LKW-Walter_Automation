package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26579 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Administration menu should still be highlighted in nav menu when
	 * navigate to admin links
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_26579_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Navigate to Admin -> System Setting
		new VoodooControl("a", "id", "configphp_settings").click();

		VoodooUtils.focusDefault();
		// TODO VOOD-827 Need lib support to verify active module in mega menu
		// Verify Administration menu is highlighted in mega menu
		new VoodooControl("li", "css", "div.module-list li.active")
				.assertAttribute("data-module", "Administration");

		// Navigate to Admin -> Currencies and Verify Administration menu is
		// highlighted in mega menu
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "currencies_management").click();
		VoodooUtils.focusDefault();
		new VoodooControl("li", "css", "div.module-list li.active")
				.assertAttribute("data-module", "Administration");

		// Navigate to Admin -> Studio and Verify Administration menu is
		// highlighted in mega menu
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studio").click();
		VoodooUtils.focusDefault();
		new VoodooControl("li", "css", "div.module-list li.active")
				.assertAttribute("data-module", "Administration");

		// Navigate to Admin -> Module Builder and Verify Administration menu is
		// highlighted in mega menu
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "moduleBuilder").click();
		VoodooUtils.focusDefault();
		new VoodooControl("li", "css", "div.module-list li.active")
				.assertAttribute("data-module", "Administration");

		// Navigate to Admin -> Password Management and Verify Administration
		// menu is highlighted in mega menu
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "password_management").click();
		VoodooUtils.focusDefault();
		new VoodooControl("li", "css", "div.module-list li.active")
				.assertAttribute("data-module", "Administration");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
