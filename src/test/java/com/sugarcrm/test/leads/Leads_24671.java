package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_24671 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/** 
	 * Disable convert lead action for the converted leads' options is removed from System Settings	
	 *	@throws Exception
	 */
	@Test
	public void Leads_24671_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("systemSettings").click();

		// Verify "Disable convert lead action for converted leads" is not available on the page
		// TODO: VOOD-1903
		new VoodooControl("input", "css", "[name='disable_convert_lead']").assertExists(false);

		// Verify "Lead Conversion Options" is available on the page.
		new VoodooControl("select", "css", "[name='lead_conv_activity_opt']").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}