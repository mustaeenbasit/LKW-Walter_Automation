package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class PortalSettingsTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet portalSettings = new FieldSet();
		portalSettings.put("enablePortal", "true");
		portalSettings.put("logoURL", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQ54-iT1IUT9jhRUBleLcUc_7t4C1WvFlYqrouzkI3XXO8GUIFoo7XW4uo");
		portalSettings.put("maxQueryResult", "10");
		portalSettings.put("defaultUser", "qauser");
		sugar().admin.portalSetup.configurePortal(portalSettings);

		FieldSet themeSettings = new FieldSet();
		themeSettings.put("borderColor", "#4ee619");
		themeSettings.put("navigationBarColor", "#120b0b");
		themeSettings.put("primaryButtonColor", "#1ee6b6");
		sugar().admin.portalSetup.configurePortalTheme(themeSettings);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
