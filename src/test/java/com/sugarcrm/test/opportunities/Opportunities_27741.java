package com.sugarcrm.test.opportunities;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Opportunities_27741 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that no RLI subpanel appears in Opportunity create mode if RLI sub-panel is hidden by the admin.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27741_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> Display Modules and Subpanels and hide RLI subpanel
		// TODO: VOOD-1355
		//sugar().admin.disableSubpanelDisplayViaJs(sugar().revLineItems);
		sugar().navbar.navToAdminTools();
		new VoodooControl("iframe", "css", "#bwc-frame").waitForVisible(20000);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("configureTabs").click();
		new VoodooControl("div", "xpath", "//*[contains(@class,'add_subpanels')]//div[.='" + "Revenue Line Items" + "']")
		.dragNDrop(sugar().admin.configureTabs.getControl("hiddenSubpanels"));
		new VoodooControl("div", "xpath", "//*[@id='disabled_subpanels_div']//div[.='" + "Revenue Line Items" + "']")
		.waitForVisible();
		sugar().admin.configureTabs.getControl("save").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration(30000);

		//  Go to Opportunities module
		sugar().opportunities.navToListView();
		// Click Create button
		sugar().opportunities.listView.create();

		// Verify that  No RLI subpanel appears in opportunity create mode
		sugar().opportunities.createDrawer.getEditField("rli_name").assertExists(false);
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.defaultData.get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.defaultData.get("relAccountName"));
		sugar().opportunities.createDrawer.save();

		// verify that a message appears to create a RLI .
		sugar().alerts.getWarning().assertContains(fs.get("warning_message"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}