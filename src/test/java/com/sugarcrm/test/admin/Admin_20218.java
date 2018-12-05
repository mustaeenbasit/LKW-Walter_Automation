package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20218 extends SugarTest { 
	FieldSet customData;
	VoodooControl enableImage;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
	}

	/**
	 * Verify module name in connector sync with updating module name 
	 * @throws Exception
	 */
	@Test
	public void Admin_20218_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.renameModule(sugar().contracts, customData.get("singularName"), customData.get("pluralName"));
		
		// Connector checking
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("connector").click();
		VoodooUtils.focusFrame("bwc-frame");
		enableImage = new VoodooControl("img", "name", "enableImage");
		enableImage.click();

		// TODO: VOOD-637
		// Assert
		new VoodooControl("li", "xpath", "//ul[@id='ext_rest_twitter:enabled_ul']").waitForVisible();
		new VoodooControl("li", "xpath", "//ul[@id='ext_rest_twitter:disabled_ul']/li[contains(.,'"+customData.get("pluralName")+"')]").assertExists(true);
		new VoodooControl("li", "xpath", "//ul[@id='ext_rest_twitter:disabled_ul']/li[contains(.,'"+sugar().contracts.moduleNamePlural+"')]").assertExists(false);
		// Drag to enabled
		new VoodooControl("li", "xpath", "//ul[@id='ext_rest_twitter:disabled_ul']/li[contains(.,'"+customData.get("pluralName")+"')]").dragNDrop(new VoodooControl("ul", "xpath", "//ul[@id='ext_rest_twitter:enabled_ul']"));
		new VoodooControl("input", "id", "connectors_top_save").click();
		new VoodooControl("img", "name", "connectorMapImg").click();

		// Assert
		new VoodooControl("table", "xpath", "//*[@id='ext_rest_twitter_add_tables']/table").waitForVisible();
		new VoodooControl("table", "xpath", "//*[@id='ext_rest_twitter_add_tables']/table[contains(.,'"+customData.get("pluralName")+"')]").hover();
		new VoodooControl("table", "xpath", "//*[@id='ext_rest_twitter_add_tables']/table[contains(.,'"+customData.get("pluralName")+"')]").assertExists(true);
		new VoodooControl("table", "xpath", "//*[@id='ext_rest_twitter_add_tables']/table[contains(.,'"+sugar().contracts.moduleNamePlural+"')]").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}