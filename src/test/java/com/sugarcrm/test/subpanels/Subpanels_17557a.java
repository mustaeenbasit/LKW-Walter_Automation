package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17557a extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * 17557a: Verify user can create related records from subpanels -- a. Opportunity
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17557a_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts Record view
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// Click "+" in Opportunities sub-panel to create new opportunity
		StandardSubpanel opportunitiesSubpanel = 
				sugar.accounts.recordView.subpanels.get(sugar.opportunities.moduleNamePlural);
		opportunitiesSubpanel.addRecord();

		// Verify account name field is populated by default on opportunities create Drawer
		sugar.opportunities.createDrawer.getEditField("relAccountName")
			.assertEquals(sugar.accounts.getDefaultData().get("name"), true);

		// Complete all required information for both Opportunity and RLI
		sugar.opportunities.createDrawer.getEditField("name")
			.set(sugar.opportunities.getDefaultData().get("name"));
		sugar.opportunities.createDrawer.getEditField("rli_name")
			.set(sugar.revLineItems.getDefaultData().get("name"));
		sugar.opportunities.createDrawer.getEditField("rli_expected_closed_date")
			.set(sugar.revLineItems.getDefaultData().get("date_closed"));
		sugar.opportunities.createDrawer.getEditField("rli_likely")
			.set(sugar.revLineItems.getDefaultData().get("likelyCase"));
		sugar.opportunities.createDrawer.save();

		// Observe opportunities sub-panel of Account record view
		//TODO: VOOD-1480 use scrollIntoView type methods where needed
		opportunitiesSubpanel.scrollIntoViewIfNeeded(false);
		opportunitiesSubpanel.expandSubpanel();

		// Verify that created opportunities record is displayed in RLI sub-panel on Account record view
		opportunitiesSubpanel.getDetailField(1, "name")
			.assertEquals(sugar.opportunities.getDefaultData().get("name"),true);
		// TODO: VOOD-1443
		new VoodooControl ("span", "css", ".fld_date_closed.disabled")
			.assertEquals(sugar.revLineItems.getDefaultData().get("date_closed"),true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}