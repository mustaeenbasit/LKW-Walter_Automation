package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26086_contracts extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		
		sugar.accounts.api.create();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Verify that for BWC modules - Create record works correctly when only multiple teams are selected
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26086_contracts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
			
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);
		
		sugar.navbar.selectMenuItem(sugar.contracts, "createContract" );
		sugar.contracts.editView.setFields(sugar.contracts.getDefaultData());

		VoodooUtils.focusFrame("bwc-frame");
		
		// Remove default team
		new VoodooControl("button", "id", "remove_team_name_collection_0").click();
		
		// Click Add button
		new VoodooControl("button", "id", "teamSelect").click();
		VoodooUtils.focusWindow(1);
		
		// Verify all teams are displayed
		new VoodooControl("tr", "xpath", "//tr[contains(.,'qauser')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Administrator')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Global')]").assertExists(true);
		
		new VoodooControl("input", "xpath", "//tr[contains(.,'qauser')]/td[1]/input").set("true");
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Make it Primary team
		new VoodooControl("input", "id", "primary_team_name_collection_0").click();

		// Click Add button
		new VoodooControl("button", "id", "teamSelect").click();
		VoodooUtils.focusWindow(1);
		
		// Add Global
		new VoodooControl("input", "xpath", "//tr[contains(.,'Global')]/td[1]/input").set("true");
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Save
		VoodooUtils.focusDefault();
		sugar.contracts.editView.save();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that selected team appears
		new VoodooControl("td", "xpath", "//tr[contains(.,'Team')]/td[contains(.,'qauser, Global')]").assertExists(true);

		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}