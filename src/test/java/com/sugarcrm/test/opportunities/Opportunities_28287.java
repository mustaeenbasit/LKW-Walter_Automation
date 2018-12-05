package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28287 extends SugarTest {
	
	// TODO: VOOD-1359 
	String forecast = "commit_stage";

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().revLineItems.api.create();
		sugar().login();

		// setup forecast
		FieldSet fsData = new FieldSet();
		fsData = testData.get(testName).get(0);
		sugar().forecasts.setupForecasts(fsData);
	}

	/**
	 * Verify that Forecast field is removed from opportunity record/list view when 
	 * switch projection from Opportunities to Opportunities + RLIs
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28287_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// 1. Forecast field is present in RLI record and list view, when mode is OPP + RLI
		// TODO: VOOD-1359 Need to modify Lib support for controls on Opportunity/RLI Both Views
		sugar().revLineItems.listView.addHeader(forecast);
		VoodooControl forecastHeaderRLI = sugar().revLineItems.listView
				.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(forecast)));
		sugar().revLineItems.recordView.addControl(forecast, "div", "css", ".fld_commit_stage");
		
		// Verify forecast column header is present for RLI, OPP + RLI mode
		// TODO: VOOD-1772 NavToModule fails with Page not ready after setting up Forecast
		sugar().navbar.showAllModules();
		new VoodooControl("li", "css", "ul.nav.megamenu div[role='menu'].dropdown-menu li[data-module='" 
				+ sugar().revLineItems.moduleNamePlural + "']").click();

		// TODO: VOOD-1772 NavToModule fails with Page not ready after setting up Forecast
		VoodooUtils.refresh();
		sugar().alerts.waitForLoadingExpiration();
		forecastHeaderRLI.assertExists(true);

		// Verify forecast field is present for RLI record, OPP + RLI mode
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.getControl(forecast).assertExists(true);

		// 2. Forecast field is not present in opportunity record and list view, mode is OPP + RLI
		// TODO: VOOD-1359 Need to modify Lib support for controls on Opportunity/RLI Both Views
		sugar().opportunities.listView.addHeader(forecast);
		VoodooControl forecastHeaderOpp = sugar().opportunities.listView
				.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(forecast)));
		sugar().opportunities.recordView.addControl(forecast, "div", "css", ".fld_commit_stage");
		
		// Verify forecast column header is not present for OPP, when mode is OPP + RLI
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		forecastHeaderOpp.assertExists(false);
		
		// Verify forecast field is not present for OPP record, when mode is OPP + RLI
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.getControl(forecast).assertExists(false);

		// Switch from Opp+RLI mode to OPP only mode
		sugar().admin.api.switchToOpportunitiesView();

		// 3. Forecast field is present on opportunity record and list view
		// Verify forecast column header is present for OPP, when mode is OPP only
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		forecastHeaderOpp.assertExists(true);

		// Verify forecast field is not present for OPP record, when mode is OPP only
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.getControl(forecast).assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}


	public void cleanup() throws Exception {}
}