package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20789 extends SugarTest {
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Prefill Gmail Defaults for Inbound Email
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20789_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToAdminTools();
		sugar.alerts.waitForLoadingExpiration(); // It is required for loading the page
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("inboundEmail").click();
		VoodooUtils.focusDefault();		
		sugar.navbar.selectMenuItem(sugar.inboundEmail, "newGroupMailAccount");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();

		// Verify prefill Gmail Defaults for Inbound Email
		sugar.inboundEmail.editView.getEditField("url").assertEquals(customData.get("server_url"), true);
	    sugar.inboundEmail.editView.getEditField("ssl").assertAttribute("value", customData.get("ssl"), true);
	    
	    // TODO: VOOD-1099
		new VoodooControl("input", "id", "port").assertEquals(customData.get("port"), true);
		new VoodooControl("option", "css", "select#protocol option[selected]").assertAttribute("value", customData.get("protocol"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}