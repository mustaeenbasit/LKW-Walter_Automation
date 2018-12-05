package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_18212 extends SugarTest {
	String rliModuleNamePlural = "";

	public void setup() throws Exception {
		rliModuleNamePlural = testData.get(testName).get(0).get("rli_module_plural_name");
		sugar().accounts.api.create();
		sugar().login();

		// TODO: VOOD-828, VOOD-1355
		// Move RLI subpanel to hidden, Xpath used to find subpanels by name
		sugar().admin.navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='" + rliModuleNamePlural + "']").dragNDrop(sugar().admin.configureTabs.getControl("hiddenSubpanels"));
		new VoodooControl("div", "xpath", "//*[@id='disabled_subpanels_div']//div[.='" + rliModuleNamePlural + "']").waitForVisible();
		sugar().admin.configureTabs.getControl("save").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify that the opportunity & account names are populated in new RLI when
	 *  click "Create a Revenue Line Item" in warning message after a new opp is saved 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18212_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create opportunity record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Click on Create RLI warning link
		sugar().alerts.getWarning().clickLink(0);
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);

		// Verify account name & Opportunity name are pre-populated
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		// TODO: VOOD-1359
		new VoodooControl("span", "css", ".detail.fld_account_name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}