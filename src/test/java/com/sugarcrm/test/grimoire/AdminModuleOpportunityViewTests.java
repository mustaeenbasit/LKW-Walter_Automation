package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * Test class for Admin Module Opportunity View switch
 */
public class AdminModuleOpportunityViewTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void switchToOpportunityViewUI() throws Exception {
		VoodooUtils.voodoo.log.info("Running switchToOpportunityViewUI()...");

		// Set Opportunity settings
		FieldSet oppSettingsData = new FieldSet();
		oppSettingsData.put("desiredView", "Opportunities");
		oppSettingsData.put("rollUp", "latestCloseDate");

		sugar().admin.switchOpportunityView(oppSettingsData);
		if (sugar().alerts.getSuccess().queryVisible()) 
			sugar().alerts.getSuccess().closeAlert();

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertExists(false);
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("switchToOpportunityViewUI() complete.");
	}

	@Test
	public void switchToRliViewUI() throws Exception {
		VoodooUtils.voodoo.log.info("Running switchToRliViewUI()...");

		// First ensure we are in Opp View
		sugar().admin.api.switchToOpportunitiesView();
		
		// TODO: VOOD-784
		new VoodooControl("li", "css", ".nav.megamenu[data-container=module-list] li[data-module=RevenueLineItems] span.btn-group").assertVisible(false);
		VoodooControl showAllModules = sugar.navbar.getControl("showAllModules");
		showAllModules.click();
		new VoodooControl("li","css","ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='RevenueLineItems']").assertVisible(false);
		showAllModules.click();
		
		// Set Opportunity + RLI settings
		FieldSet oppSettingsData = new FieldSet();
		oppSettingsData.put("desiredView", "RevenueLineItems");

		sugar().admin.switchOpportunityView(oppSettingsData);
		if (sugar().alerts.getSuccess().queryVisible()) 
			sugar().alerts.getSuccess().closeAlert();

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertExists(true);
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("switchToRliViewUI() complete.");
	}

	public void cleanup() throws Exception {}
}