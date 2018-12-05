package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_21999 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Convert Lead_Verify that the converted lead is displayed in "Leads" sub-panel of opportunity created for converting a lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_21999_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to leads record view and open primary button dropdown
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Click "Convert Lead" button in leads record view.
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Fill in Account name and click Associate Account
		new VoodooControl("input", "css", "#collapseAccounts .fld_name input").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();

		// Enter new opportunity required fields and click create
		new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input").set(testName);

		// Click Create Opportunity button.
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button.convert-panel-header a").click();

		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Click opportunity record link in conversion result on leads record view
		new VoodooControl("a", "xpath", "//*[contains(@class,'converted-results')]//tr[contains(@name,'Opportunities')]//td/div/a").click();

		// Verify the record in leads subpanel on opportunities record view
		StandardSubpanel leadsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.expandSubpanel();
		leadsSubpanel.getDetailField(1, "fullName").assertEquals(sugar().leads.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}