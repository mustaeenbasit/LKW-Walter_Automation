package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_28678 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Module selection should remain persistent (per browser) and will not reset
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28678_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		VoodooControl searchDropdown = sugar().navbar.search.getControl("searchModuleDropdown");
		VoodooControl moduleIcon = sugar().navbar.search.getControl("searchModuleIcons");
		VoodooControl searchContact = sugar().navbar.search.getControl("searchContacts");
		VoodooControl searchOrCancelIcon = sugar().navbar.search.getControl("cancelSearch");
		String contactModuleIcon = sugar().contacts.moduleNamePlural.substring(0, 2);

		// Verify "All" as module icon by default on global search
		globalSearch.click();
		moduleIcon.assertEquals(customData.get("default_module_text"), true);

		// Select Contacts module and verify its module Icon on global search
		searchDropdown.click();
		searchContact.click();
		moduleIcon.assertEquals(contactModuleIcon, true);
		searchContact.assertAttribute("class", customData.get("selected_class_name"), true);
		searchOrCancelIcon.click();

		// Verify Contacts Module selection should remain persistent and will not reset while clicking on search icon on global search bar
		searchOrCancelIcon.click();
		VoodooUtils.waitForReady();
		moduleIcon.assertEquals(contactModuleIcon, true);
		searchDropdown.click();
		searchContact.assertAttribute("class", customData.get("selected_class_name"), true);

		// Verify Contacts Module selection should remain persistent and will not reset while clicking on [X]/Clear icon
		searchOrCancelIcon.click();
		globalSearch.click();
		moduleIcon.assertEquals(contactModuleIcon, true);
		searchDropdown.click();
		searchContact.assertAttribute("class", customData.get("selected_class_name"), true);
		searchOrCancelIcon.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}