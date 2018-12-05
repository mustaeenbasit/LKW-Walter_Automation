package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26092_notes extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that for sidecar modules - Create record works correctly duplicate team is added
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26092_notes_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.selectMenuItem(sugar.notes, "createNote" );
		sugar.notes.createDrawer.showMore();
		sugar.notes.createDrawer.setFields(sugar.notes.getDefaultData());

		// Click Team Drop down
		new VoodooControl("span", "css", ".fld_team_name.edit").click();
		
		// Click Search for more
		new VoodooControl("li", "css", "#select2-drop > ul:nth-child(3) > li").click();
		
		// Verify all teams are displayed
		new VoodooControl("tr", "xpath", "//tr[contains(.,'qauser')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Administrator')]").assertExists(true);
		new VoodooControl("tr", "xpath", "//tr[contains(.,'Global')]").assertExists(true);
		
		// Select qauser
		new VoodooControl("tr", "xpath", "//tr[contains(.,'qauser')]/td[1]/span/input").click();
		
		VoodooUtils.pause(2000); // Sometimes, wait is needed
		
		// Make it Primary team
		new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(1) button[name='primary']").click();

		// Add one more team 
		new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(1) button[name='add']").click();
		new VoodooControl("a", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) div.controls.controls-three.btn-fit a").click();
		new VoodooControl("li", "css", "#select2-drop > ul:nth-child(3) > li").click();

		// Select qauser again
		new VoodooControl("tr", "xpath", "//tr[contains(.,'qauser')]/td[1]/span/input").click();

		VoodooUtils.pause(2000); // Sometimes, wait is needed
		
		// Assert - in recordView, duplicate records are visible
		new VoodooControl("span", "css", "span.fld_team_name.edit div.control-group:nth-of-type(1) span.select2-chosen").assertContains("qauser", true);
		new VoodooControl("span", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) span.select2-chosen").assertContains("qauser", true);

		// Save
		sugar.notes.createDrawer.save();
		sugar.alerts.waitForLoadingExpiration();
		
		// Goto detail view
		sugar.notes.listView.clickRecord(1);
		sugar.notes.recordView.showMore(); 		
		
		// Assert 
		new VoodooControl("div", "css", "span.fld_team_name.detail > div:nth-child(1)").assertContains("Administrator", true);
		new VoodooControl("div", "css", "span.fld_team_name.detail > div:nth-child(2)").assertContains("qauser", true);
		new VoodooControl("div", "css", "span.fld_team_name.detail > div:nth-child(2)").assertContains("Primary", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}