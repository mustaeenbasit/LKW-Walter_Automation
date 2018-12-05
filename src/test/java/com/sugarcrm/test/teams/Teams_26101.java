package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26101 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 *  [Pro/Ent Edition]-[Dynamic team]-Convert lead-Create record can select multiple teams
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26101_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Click on convert link
		// TODO: VOOD-585
		new VoodooControl("a", "css", "[name='lead_convert_button']").click();	
		sugar().alerts.waitForLoadingExpiration();
		FieldSet customData = testData.get(testName).get(0);

		// Associate account with lead
		new VoodooControl("input", "css", "#collapseAccounts input[name='name']").set(customData.get("account_name"));
		new VoodooControl("a", "css", "[data-module='Accounts'] [name='associate_button']").click();
		sugar().alerts.waitForLoadingExpiration();

		// Associate Opportunity with lead
		new VoodooControl("input", "css", "#collapseOpportunities input[name='name']").set(customData.get("opportunity_name"));
		new VoodooControl("a", "css", "[data-module='Opportunities'] [name='associate_button']").click();			
		sugar().alerts.waitForLoadingExpiration();

		// Save & Convert
		new VoodooControl("a", "css", ".layout_Leads.drawer.active [name='save_button']").click();

		// Verify that converted Contact displayed properly
		new VoodooControl("div", "css", "[data-voodoo-name='convert-results'] table tbody tr:nth-child(1) td:nth-child(1) div").assertContains(sugar().contacts.moduleNameSingular+":"+" "+sugar().leads.getDefaultData().get("firstName")+" "+sugar().leads.getDefaultData().get("lastName"), true);

		// Verify that converted Account displayed properly
		new VoodooControl("div", "css", "[data-voodoo-name='convert-results'] table tbody tr:nth-child(2) td:nth-child(1) div").assertContains(sugar().accounts.moduleNameSingular+":"+" "+customData.get("account_name"), true);

		// Verify that converted Opportunity displayed properly
		new VoodooControl("div", "css", "[data-voodoo-name='convert-results'] table tbody tr:nth-child(3) td:nth-child(1) div").assertContains(sugar().opportunities.moduleNameSingular+":"+" "+customData.get("opportunity_name"), true);
		VoodooUtils.waitForReady();

		// Go to created contact and verify team
		String teamName = sugar().leads.defaultData.get("relTeam");
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.showMore();
		VoodooUtils.waitForReady();
		sugar().contacts.recordView.getDetailField("relTeam").assertContains(teamName, true);

		// Go to account and verify team
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relTeam").assertContains(teamName, true);

		// Go to opportunity and verify team
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.showMore();
		sugar().opportunities.recordView.getDetailField("relTeam").assertContains(teamName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}