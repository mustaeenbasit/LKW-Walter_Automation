package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_25060 extends SugarTest {
	FieldSet forecastData = new FieldSet();

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		FieldSet roleData = testData.get("env_role_setup").get(0);
		forecastData = testData.get(testName).get(0);

		// Login
		sugar().login();

		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();

		// Create a Role
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Setting Best Case permission to 'None' for RLI module
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[contains(@class,'edit')]//tr[contains(.,'Revenue Line Items')]//td/a").click();
		new VoodooControl("div", "id", "best_caselink").click();
		new VoodooControl("select", "id", "flc_guidbest_case").set(roleData.get("roleNone"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role and logout
		AdminModule.assignUserToRole(roleData);

		// Go to RLI-> Create RLI -> Enter required fields with proper values so that it could be seen in Forecast module -> Click Save.
		// TODO: VOOD-444
		FieldSet rliData = new FieldSet();
		rliData.put("salesStage", forecastData.get("salesStage"));
		rliData.put("forecast", forecastData.get("forecast"));
		rliData.put("date_closed", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		rliData.put("relAssignedTo", sugar().users.getQAUser().get("userName"));
		sugar().revLineItems.create(rliData);
		sugar().alerts.getSuccess().closeAlert();
		sugar().logout();
	}

	/**
	 * Verify No access label is displayed for the field in forecasts if field acess is set to none
	 * @throws Exception
	 */
	@Test
	public void Roles_25060_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Go to 'Forecasts' module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Verify the data in Best Case field on forecast worksheet. 
		// TODO: VOOD-1445
		String accessLabel = forecastData.get("accessLabel");
		new VoodooControl("span", "css", ".info.fld_best_case div span").assertEquals(accessLabel, true);

		// Verify the data in Best Case field on Preview pane. 
		sugar().forecasts.worksheet.previewRecord(1);
		sugar().previewPane.showMore();
		new VoodooControl("span", "css", ".preview-data .fld_best_case span").assertEquals(accessLabel, true);
		sugar().previewPane.closePreview();

		// Verify the data in Best Case field on My Dashboard 
		sugar().dashboard.chooseDashboard(forecastData.get("myDashboard"));
		new VoodooControl("span", "css", ".forecast_details_amount span").assertEquals(accessLabel, true);

		// Verify in Forecasts chart dashlet, drop down menu containing best case option not visible.
		// TODO: VOOD-1384
		new VoodooControl("span", "css", ".forecasts-chart-wrapper .fld_dataset .select2-arrow").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}