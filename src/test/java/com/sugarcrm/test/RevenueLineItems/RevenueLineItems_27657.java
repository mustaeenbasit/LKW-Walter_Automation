package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */ 
@Features(revenueLineItem = false)
public class RevenueLineItems_27657 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that RLI module is hidden from the module list and all RLI subpanels are hidden OOB.
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27657_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify no RLI on studio 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		new VoodooControl("tr", "id", "studiolink_"+sugar().revLineItems.moduleNamePlural+"").assertVisible(false);
		VoodooUtils.focusDefault();

		// Verify no RLI in enabled modules section
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("configureTabs").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-466, 828, 1323
		new VoodooControl("tbody", "css", "#enabled_div table tbody.yui-dt-data").assertElementContains(sugar().revLineItems.moduleNamePlural, false);
		new VoodooControl("td", "css", "#enabled_subpanels_div div.yui-dt-bd table tbody.yui-dt-data").assertElementContains(sugar().revLineItems.moduleNamePlural, false);
		VoodooUtils.focusDefault();

		// Verify no RLI in quick create
		sugar().navbar.openQuickCreateMenu();
		sugar().navbar.quickCreate.getControl(sugar().revLineItems.moduleNamePlural).assertVisible(false);

		// Verify no RLI in reports
		sugar().reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		new VoodooControl("option", "css", "select[name='report_module[]'] option[value='"+sugar().revLineItems.moduleNamePlural+"']").assertVisible(false);
		VoodooUtils.focusDefault();		

		// Verify no RLI in Workflow
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("workflowManagement").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// TODO: VOOD-1042
		new VoodooControl("i", "css", "li.active .fa-caret-down").click();
		new VoodooControl("li", "css", "li.active .scroll ul li").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("option", "css", "select[name=base_module] option[value='"+sugar().revLineItems.moduleNamePlural+"']").assertVisible(false);
		new VoodooControl("input", "id", "cancel_workflow").click();
		VoodooUtils.focusDefault();

		// Verify no RLI on Dashlet
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// TODO: VOOD-1376
		new VoodooControl("h4", "css", "li.span8 ul li.row-fluid:nth-of-type(2) div.dashlet-header h4").assertContains(sugar().opportunities.moduleNamePlural, true);

		// Verify no RLI on Forecast worksheet
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.getControl("worksheetSettings").assertContains(testData.get(testName).get(0).get("rli_display_name"), false);
		sugar().forecasts.setup.cancelSettings();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}