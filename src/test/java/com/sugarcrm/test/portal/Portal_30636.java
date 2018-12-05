package com.sugarcrm.test.portal;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Portal_30636 extends SugarTest {

	public void setup() throws Exception {
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		sugar().contacts.api.create(portalSetupData);
		sugar().login();

		// Disable Cases module and subpanel
		sugar().admin.disableModuleDisplayViaJs(sugar().cases);
		sugar().admin.disableSubpanelDisplayViaJs(sugar().cases);
		
		// Disable KB subpanel
		sugar().admin.disableSubpanelDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Test Case 30636: Verify that in Configure Portal page list should show disable modules
	 * @throws Exception
	 */
	@Test
	public void Portal_30636_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to Sugar Portal
		sugar().admin.navToPortalSettings();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Clicking on configuration link of portal
		sugar().admin.portalSetup.getControl("configurePortal").click();
		
		// TODO: VOOD-1119
		// Verifying Bugs, Cases, Knowledge Base modules are showing in portal configuration page.
		new VoodooControl("ul", "css", ".bodywrapper div:nth-of-type(2) ul").assertEquals(sugar().cases.moduleNamePlural + "\n" + customData.get("kbModuleNamePlural") + "\n" + sugar().bugs.moduleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}