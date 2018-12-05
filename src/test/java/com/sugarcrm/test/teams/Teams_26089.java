package com.sugarcrm.test.teams;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26089 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		
		sugar.accounts.api.create();
		
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Verify that for BWC modules - Create record successfully with adding multiple teams at the same time
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26089_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
			
		sugar.navbar.selectMenuItem(sugar.contracts, "createContract" );
		sugar.contracts.editView.setFields(sugar.contracts.getDefaultData());

		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that Global is the default team
		new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(2) td:nth-of-type(1) input:nth-of-type(1)").assertAttribute("value", "Global", true);

		// Click Add button
		new VoodooControl("button", "id", "teamSelect").click();
		VoodooUtils.focusWindow(1);
		
		// Verify all teams are displayed
		new VoodooControl("tr", "xpath", "//tr[contains(.,'qauser')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Administrator')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Global')]").assertExists(true);

		// Select multiple teams
		new VoodooControl("input", "xpath", "//tr[contains(.,'qauser')]/td[1]/input").set("true");
		new VoodooControl("input", "xpath", "//tr[contains(.,'Administrator')]/td[1]/input").set("true");
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Assert - All teams are visible
		new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(2) td:nth-of-type(1) input:nth-of-type(1)").assertAttribute("value", "Global", true);
		// As order of appearance is unpredictable, hence below assertTrue statements
		assertTrue("Wrong Value in 2nd row!!", 
				new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(3) td:nth-of-type(1) input:nth-of-type(1)").queryAttributeEquals("value", "qauser") ||
				new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(3) td:nth-of-type(1) input:nth-of-type(1)").queryAttributeEquals("value", "Administrator"));
		assertTrue("Wrong Value in 3rd row!!", 
				new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(4) td:nth-of-type(1) input:nth-of-type(1)").queryAttributeEquals("value", "qauser") || 
				new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(4) td:nth-of-type(1) input:nth-of-type(1)").queryAttributeEquals("value", "Administrator"));

		// Save
		VoodooUtils.focusDefault();
		sugar.contracts.editView.save();
		
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that selected teams appear
		new VoodooControl("td", "xpath", "//tr[contains(.,'Team')]/td[contains(.,'Global, Administrator, qauser')]").assertExists(true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}