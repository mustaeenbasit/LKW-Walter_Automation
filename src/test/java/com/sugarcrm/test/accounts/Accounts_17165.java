package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17165 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("team"));
		sugar().teams.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify searching team in mass update works fine
	 * @throws Exception
	 */			
	@Test
	public void Accounts_17165_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// go to accounts module and select one or more records in list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);

		// Select mass update action
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.massUpdate();

		// Select Teams from fields drop down
		sugar().accounts.massUpdate.getControl("massUpdateField02").set(sugar().teams.moduleNamePlural);
		VoodooControl massUpdateValueCtrl = sugar().accounts.massUpdate.getControl("massUpdateValue02");
		massUpdateValueCtrl.click();

		// Input data in search box matching with existing teams
		// TODO: VOOD-1003
		new VoodooControl("input", "css", "#select2-drop div input").set(customData.get("team").substring(0, 3));
		sugar().alerts.waitForLoadingExpiration();

		// Verify all teams are shown which satisfied to input characters for searching
		VoodooControl searchResultCtrl = new VoodooControl("ul", "css", "#select2-drop ul:nth-child(2)");
		searchResultCtrl.assertContains(customData.get("team"), true);
		searchResultCtrl.assertContains(customData.get("defaultTeam"), true);

		// Click "search and select" option (appears below search results)
		new VoodooControl("div", "css", "#select2-drop ul:nth-child(3) li div").click();

		// Verify that the Search and select Teams drawer is opened
		sugar().teams.searchSelect.getControl("moduleTitle").assertContains(customData.get("title"), true);

		// Select the team
		// TODO: VOOD-1162
		new VoodooControl("input", "css", ".layout_Teams [data-voodoo-name='filter-quicksearch'] input").set(customData.get("team")); 
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", ".list.fld_Teams_select .selection").click();
		VoodooUtils.waitForReady();

		// Verify selected team will show in the mass update value.
		massUpdateValueCtrl.assertEquals(customData.get("team"), true);

		// Cancel the mass update form 
		sugar().accounts.massUpdate.cancelUpdate();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}