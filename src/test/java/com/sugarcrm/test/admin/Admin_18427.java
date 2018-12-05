package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_18427 extends SugarTest {
	FieldSet customData;
	public void setup() throws Exception {
		sugar().login();
		customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// create custom person module with import enabled.
		// TODO: VOOD-788 need lib support of module builder functions
		new VoodooControl("a", "css" ,"#moduleBuilder").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"td#newPackage a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(customData.get("pack_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='key']").set(customData.get("key"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"table#new_module a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(customData.get("mod_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label']").set(customData.get("label"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label_singular']").set(customData.get("sin_label"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='importable']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "css" ,"tr#factory_modules table#type_person").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").click();
		VoodooUtils.waitForReady(75000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify the vCard import label contains the module name.
	 * @throws Exception
	 */
	@Test
	public void Admin_18427_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying import message is shown
		// TODO: VOOD-789 need lib support of using navbar for custom module
		sugar().navbar.navToModule(customData.get("data_module"));
		new VoodooControl("button", "css" ,"li[data-module='"+customData.get("data_module")+"'] button[data-toggle='dropdown']").click();
		new VoodooControl("a", "css" ,"li.dropdown.active[data-module='"+customData.get("data_module")+"'] a[data-navbar-menu-item='LNK_IMPORT_VCARD']").assertEquals(customData.get("import_vcard"), true);
		new VoodooControl("a", "css" ,"li.dropdown.active[data-module='"+customData.get("data_module")+"'] a[data-navbar-menu-item='LNK_IMPORT_VCARD']").click();
		new VoodooControl("div", "css" ,"div[data-voodoo-name='vcard-import']").assertContains(customData.get("import_msg"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
