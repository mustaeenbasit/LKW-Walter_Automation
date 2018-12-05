package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_18211 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Admin -> Display Modules & Subpanels 
		// Move RLI from displayed subpanels to hidden subpanels 
		sugar().admin.navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-828, VOOD-1355
		new VoodooControl("div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='" + customData.get("rli_module_plural_name") + "']").dragNDrop(sugar().admin.configureTabs.getControl("hiddenSubpanels"));
		new VoodooControl("div", "xpath", "//*[@id='disabled_subpanels_div']//div[.='" + customData.get("rli_module_plural_name") + "']").waitForVisible();
		sugar().admin.configureTabs.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * ENT/ULT: Verify that the message regarding missing RLI is displayed when viewing the opportunity with no RLIs linked to it 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18211_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		// Create Opportunity
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify RLI warning on listview after save opportunity record
		sugar().alerts.getWarning().assertEquals(customData.get("warning_msg"), true);
		sugar().alerts.getWarning().closeAlert();

		// Verify RLI warning on record view itself
		sugar().opportunities.listView.clickRecord(1);
		sugar().alerts.getWarning().assertEquals(customData.get("warning_msg"), true);
		sugar().alerts.getWarning().closeAlert();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}