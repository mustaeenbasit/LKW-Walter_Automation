package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26090 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		
		sugar.accounts.api.create();
		
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Verify that for BWC modules - Create record successfully with multiple teams and hiding teams
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26090_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
			
		sugar.navbar.selectMenuItem(sugar.contracts, "createContract" );
		sugar.contracts.editView.setFields(sugar.contracts.getDefaultData());

		VoodooUtils.focusFrame("bwc-frame");
		
		// Assert - Hide button is not available
		new VoodooControl("span", "id", "more_div_EditView_team_name").assertVisible(false);;

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
		
		// Assert - Hide button becomes available
		new VoodooControl("span", "id", "more_div_EditView_team_name").assertContains("Hide", true);

		// Assert - Both teams are visible
		new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(2) td:nth-of-type(1) input:nth-of-type(1)").assertAttribute("value", "Global", true);
		new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(3) td:nth-of-type(1) input:nth-of-type(1)").assertAttribute("value", "qauser", true);

		// Now click "Hide"
		new VoodooControl("span", "css", "span#more_EditView_team_name span").click();
		
		// Assert - Only Primary team is visible
		new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(2) td:nth-of-type(1) input:nth-of-type(1)").assertAttribute("value", "Global", true);
		new VoodooControl("input", "css", "table#EditView_team_name_table tr:nth-of-type(3) td:nth-of-type(1) input:nth-of-type(1)").assertVisible(false);

		// Save
		VoodooUtils.focusDefault();
		sugar.contracts.editView.save();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that selected teams appear
		new VoodooControl("td", "xpath", "//tr[contains(.,'Team')]/td[contains(.,'Global, qauser')]").assertExists(true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}