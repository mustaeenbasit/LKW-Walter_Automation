package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17604 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
	}

	/**
	 * Verify not see a success saved message while clicking on the favorite icon on list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17604_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();

		sugar().accounts.listView.toggleFavorite(1);
		new VoodooControl("div", "css", "div.alert.alert-success.alert-block").assertExists(false);	
		new VoodooControl("div", "css", "div#alerts").assertElementContains("Success", false);	
		sugar().accounts.listView.getControl("favoriteStar01").assertAttribute("class", "active");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}