package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28870 extends SugarTest {
	UserRecord chris;

	public void setup() throws Exception {
		// Creating Leads & Contracts record
		sugar().leads.api.create();
		sugar().contracts.api.create();
		sugar().login();

		// Changing display module settings for Leads & Contracts
		sugar().admin.disableModuleDisplayViaJs(sugar().leads);
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);

		// Creating custom user chris
		// TODO: VOOD-1200
		chris = (UserRecord) sugar().users.create();

		// Go to Admin -> search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();

		// Asserting Leads & contracts present in Enabled module section
		// TODO: VOOD-1860
		VoodooControl enabledSearchModules = new VoodooControl("div", "id", "enabled_div");
		enabledSearchModules.assertContains(sugar().leads.moduleNamePlural, true);
		enabledSearchModules.assertContains(sugar().contracts.moduleNamePlural, true);
		VoodooUtils.focusDefault();
		sugar().logout();
	}

	/**
	 * Verify that module is still search able if you disable modules in My Preference or in Enabled Modules and Subpanels
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28870_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login with chris and navigating to its profile
		chris.login();
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Moving Contracts from display section to hidden section for chris
		// TODO: VOOD-1867
		new VoodooControl("a", "id", "tab4").click();
		new VoodooControl("option", "css", "#display_tabs [value='Contracts']").click();
		new VoodooControl("a", "id", "chooser_display_tabs_left_to_right").click();
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady(30000); // Wait required here to reload module list in module panel and its module dropdown
		VoodooUtils.focusDefault();

		// Asserting Leads & Contracts module is not present in module panel and its module dropdown. 
		// TODO: VOOD-1866	
		sugar().navbar.getControl("showAllModules").click();
		new VoodooControl("li", "css", String.format("ul.nav.megamenu li[data-module='%s'] a", sugar().leads.moduleNamePlural)).assertVisible(false);
		new VoodooControl("li", "css", String.format("ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='%s']", sugar().leads.moduleNamePlural)).assertVisible(false);
		new VoodooControl("li", "css", String.format("ul.nav.megamenu li[data-module='%s'] a", sugar().contracts.moduleNamePlural)).assertVisible(false);
		new VoodooControl("li", "css", String.format("ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='%s']", sugar().contracts.moduleNamePlural)).assertVisible(false);

		// Asserting Leads & Contracts records are search able in Global search even if module are disabled to be be displayed
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.set(sugar().leads.defaultData.get("firstName"));
		VoodooUtils.waitForReady();
		String leadsName = sugar().leads.getDefaultData().get("fullName").substring(4, 12);
		VoodooControl searchResults = sugar().navbar.search.getControl("searchResults");
		searchResults.assertContains(leadsName, true);
		globalSearch.set(sugar().contracts.defaultData.get("name"));
		VoodooUtils.waitForReady();
		searchResults.assertContains(sugar().contracts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}