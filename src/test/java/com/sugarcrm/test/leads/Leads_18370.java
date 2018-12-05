package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Leads_18370 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Mouse over tooltip of module label seeing full module name in Leads conversion 
	 * @throws Exception
	 */
	@Test
	public void Leads_18370_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Convert lead
		sugar().leads.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-695
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Verify tool-tip showing Account and Ac is caption name
		VoodooControl accountsAvatar = new VoodooControl("span", "css", "#collapseAccounts .fld_picture span");
		accountsAvatar.hover();
		accountsAvatar.assertAttribute("data-original-title", sugar().accounts.moduleNameSingular, true);
		accountsAvatar.assertEquals(sugar().accounts.moduleNameSingular.substring(0, 2), true);
		new VoodooControl("input", "css", "#collapseAccounts .fld_name input").set(testName);
		new VoodooControl("a", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();

		// Verify tool-tip showing Opportunity and Op is caption name 
		VoodooControl oppAvatar = new VoodooControl("span", "css", "#collapseOpportunities .fld_picture span");
		oppAvatar.hover();
		oppAvatar.assertAttribute("data-original-title", sugar().opportunities.moduleNameSingular, true);
		oppAvatar.assertEquals(sugar().opportunities.moduleNameSingular.substring(0, 2), true);
		new VoodooControl("a", "css", ".convert-headerpane.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}