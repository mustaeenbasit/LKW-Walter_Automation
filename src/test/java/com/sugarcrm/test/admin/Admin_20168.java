package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20168 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Web Logic Hook record can be created successfully
	 * @throws Exception
	 */
	@Test
	public void Admin_20168_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String contactName = sugar().contacts.moduleNamePlural;
		FieldSet customFS = testData.get(testName).get(0);
		sugar().admin.navToAdminPanelLink("webLogicHook");

		// TODO: VOOD-816, VOOD-817 Lib support to create and verify Web Logic Hooks record
		new VoodooControl("a", "css", "a[name='create_button'].btn.btn-primary").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", ".fld_name input").set(customFS.get("name"));
		new VoodooControl("input", "css", ".fld_url input").set(customFS.get("url"));
		new VoodooSelect("span", "css", "[data-voodoo-name='webhook_target_module']").set(contactName);
		new VoodooControl("a", "css", "a[name='save_button'].btn.btn-primary:not(.hide)").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify Web Logic Hook record created successfully
		sugar().alerts.getSuccess().assertEquals(customFS.get("successMsg") + customFS.get("name") + customFS.get("stringDelimiter"), true);
		new VoodooControl("div", "css", "[data-voodoo-name='webhook_target_module'] div").assertEquals(contactName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}