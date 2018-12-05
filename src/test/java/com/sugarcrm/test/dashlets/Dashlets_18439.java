package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_18439 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
		
		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();
		
		// Go to admin page(//Hack: Direct navigation to Home module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();
	}

	/**
	 * Verify that the proper message is displayed in the dashlet if closed won or/and closed lost sales stage is deleted by the admin
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_18439_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960
		// Go to Home -> My Dashboard -> Edit
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.edit();
		
		// Add a Dashlet
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4, 1);
		FieldSet customFS = testData.get(testName).get(0);
		
		// Add 'In Forecast' dashlet to the admin page
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Save Dashlet
		new VoodooControl("a", "css", "[data-voodoo-name='dashletconfiguration-headerpane'] .detail.fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Save 'My Dashboard' with the new dashlet
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Go to Admin > Studio > dropdown editor
		sugar().admin.navToAdminPanelLink("dropdownEditor");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-781
		// Used xPath here to select specific drop down 'sales_stage_dom'
		new VoodooControl("a", "xpath", "//a[@class='mbLBLL' and text()='sales_stage_dom']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".listContainer li:nth-child(9) table tbody tr td:nth-child(2) a:nth-child(2)").click();
		VoodooUtils.acceptDialog();
		new VoodooControl("a", "css", ".listContainer li:nth-child(10) table tbody tr td:nth-child(2) a:nth-child(2)").click();
		VoodooUtils.acceptDialog();
		new VoodooControl("input", "id", "saveBtn").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		// Go back to home page
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		
		// Verify the following message is displayed inside the dashlet: 
		// "The Forecasts module has been improperly configured and is no longer available. Sales Stage Won and Sales Stage Lost are missing from the available Sales Stages values. Please contact your Administrator."
		new VoodooControl("div", "css", "[data-voodoo-name='forecastdetails'] div").assertContains(customFS.get("verifyMessage"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}