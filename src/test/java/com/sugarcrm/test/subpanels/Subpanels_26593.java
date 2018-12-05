package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Subpanels_26593 extends SugarTest {

	public void setup() throws Exception {
		sugar().bugs.api.create();
		sugar().accounts.api.create();
		
		// Logging in as admin
		sugar().login();
		
		// Enabling Bugs module
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/**
	 * Verify "Link Existing Record" can display record in custom relationship sub panel
	 * @throws Exception
	 */
	@Test
	public void Subpanels_26593_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Creating a custom Many-to-Many relationship Accounts-Bugs
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// Clicking on Accounts Module
		// TODO: VOOD-1505
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		
		// Clicking on Relationship Button
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		
		// Adding Relationship
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		
		// Adding Bugs module to the right pane
		new VoodooControl("select", "id", "rhs_mod_field").set(sugar().bugs.moduleNamePlural);
		VoodooUtils.waitForReady();
		
		// Clicking 'Save and Deploy' button
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		VoodooUtils.waitForReady(); 
		VoodooUtils.focusDefault();

		// Navigating to Accounts ListView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Search and Selecting the Bugs Record through Bugs Subpanel
		// TODO: VOOD-1382
		new VoodooControl("span","css",".filtered.layout_Bugs").hover();
		new VoodooControl("span","css",".filtered.layout_Bugs .fa.fa-caret-down").click();
		new VoodooControl ("a", "css", "[name='select_button']").click();
		sugar().bugs.searchSelect.selectRecord(1);
		sugar().bugs.searchSelect.link();

		// TODO: VOOD-1424
		new VoodooControl("div", "css", ".filtered.layout_Bugs .fld_name").assertEquals(sugar().bugs.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}