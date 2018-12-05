package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
@Features(revenueLineItem = false)
public class Opportunities_27656 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that using Opportunities only (no RLIs) is be the default OOB action
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27656_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		new VoodooControl("tr", "id", "studiolink_"+sugar().opportunities.moduleNamePlural+"").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Verify Worst/Likely/Best + Expected closed Date fields are not calculated. (+/- image is not appear) 
		new VoodooControl("img", "css", "div[data-name=amount]").assertAttribute("class", "right_icon", false);
		new VoodooControl("img", "css", "div[data-name=best_case]").assertAttribute("class", "right_icon", false);
		new VoodooControl("img", "css", "div[data-name=worst_case]").assertAttribute("class", "right_icon", false);		
		new VoodooControl("img", "css", "div[data-name=date_closed]").assertAttribute("class", "right_icon", false);

		// Verify Status Field is not present
		new VoodooControl("div", "css", "div[data-name=sales_status]").assertExists(false);
		VoodooUtils.focusDefault();

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
		sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.getEditField("bestCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.getEditField("worstCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.save();

		// Verify no warning message for RLI is to appear
		sugar().alerts.getWarning().assertVisible(false);

		// Verify opportunity is created
		sugar().opportunities.listView.verifyField(1, "name", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}