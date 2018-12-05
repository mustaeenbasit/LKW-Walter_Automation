package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17152 extends SugarTest {		
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Open Search and select Teams drawer after click the "Search for more..." option
	 * @throws Exception
	 */		
	@Test	
	public void Accounts_17152_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet searchSelectDrawerTitle = testData.get(testName).get(0);
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();

		//TODO: VOOD-1040 - need defined control for the team widget on user profile edit page
		VoodooSelect teamDropdown = new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] a.select2-choice");
		teamDropdown.click();
		teamDropdown.selectWidget.getControl("searchForMoreLink").click();
		VoodooUtils.waitForReady();

		// Assert the "Search and Select" drawer and the module title
		sugar().teams.searchSelect.getControl("moduleTitle").assertContains(searchSelectDrawerTitle.get("title"), true);

		// Close the "search and select" drawer
		sugar().teams.searchSelect.cancel();

		// Close the accounts create drawer
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}