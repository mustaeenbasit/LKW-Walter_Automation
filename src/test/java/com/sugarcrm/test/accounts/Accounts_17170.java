package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17170 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Shouldn't save duplicated teams in team field widget
	 * @throws Exception
	 */	
	@Test
	public void Accounts_17170_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar().accounts.createDrawer.showMore();

		//TODO VOOD-518 need lib support for team widget
		// enter duplicate teams into team field widget, such as enter 2 Administrator team and one of team is primary
		VoodooControl btnCtrl = new VoodooControl("button", "css", "button.btn.first");
		btnCtrl.click();
		new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(2) a.select2-choice").set(ds.get(0).get("team"));
		btnCtrl.click();
		new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(3) a.select2-choice").set(ds.get(0).get("team"));
		new VoodooControl("i", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(3) .fa.fa-star").click();
		sugar().accounts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().accounts.listView.clickRecord(1);

		// TODO VOOD-582
		VoodooControl teamNameCtrl = new VoodooControl("div", "css", "span[data-voodoo-name='team_name'] div:nth-child(1)");
		teamNameCtrl.assertContains(ds.get(0).get("team"), true);
		teamNameCtrl.assertContains(ds.get(0).get("primary"), true);		
		new VoodooControl("div", "css", "span[data-voodoo-name='team_name'] div:nth-child(2)").assertContains(ds.get(0).get("team1"), true);
		new VoodooControl("div", "css", "span[data-voodoo-name='team_name'] div:nth-child(3)").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
