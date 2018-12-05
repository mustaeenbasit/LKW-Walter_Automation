package com.sugarcrm.test.roles;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_31218 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().opportunities.api.create();

		// Login as Admin
		sugar().login();

		// Setup forecast
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		sugar().logout();
	}

	/**
	 * Verify that data is appearing in dashlets when opportunity access is set to diasbled
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_31218_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String currentDate = DateTime.now().toString("MM/dd/yyyy");
		FieldSet roleRecordData = testData.get("env_role_setup").get(0);
		DataSource customData = testData.get(testName);
		VoodooControl tooltipTextCtrl = new VoodooControl("div", "css", ".tooltip-inner");

		// Login as a qauser
		sugar().login(sugar().users.getQAUser());

		// Creating RLI record with current date
		FieldSet rliData = new FieldSet();
		rliData.put("date_closed", currentDate);
		sugar().revLineItems.create(rliData);

		// Edit My dashboard in home page
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.edit();
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4, 1);

		// Adding Forecast Bar Chart dashlet
		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("a", "css", "[data-original-title='Forecast Bar Chart'] a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();

		// Save the home page dashboard
		// TODO: VOOD-1645 - Need to update method(s) in 'Dashboard.java' for Edit page.
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Logout from qauser
		sugar().logout();

		// Login as Admin
		sugar().login();

		// Creating Role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Set Opportunity Access=Disabled
		// TODO: VOOD-580 - Create a Roles (ACL) Module LIB, VOOD-856 - Lib is needed for Roles Management
		new VoodooControl("div", "css", "#ACLEditView_Access_Opportunities_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Opportunities_access div select").set(customData.get(0).get("access"));

		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign role to "QAUser"
		AdminModule.assignUserToRole(roleRecordData);

		// Logout
		sugar().logout();

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// TODO: VOOD-1376 - Need library support for Pipeline Dashlet on Home Dashboard
		// Verifying All dashlets are appearing properly in home dashboard
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH); /* 0 through 11 */
		int quarter = (month / 3) + 1; 
		for (int i = 1; i < customData.size()-1; i++) {
			VoodooControl dashLetNameCtrl = new VoodooControl("h4", "css", ".dashlets li .row-fluid.sortable:nth-of-type(" + i + ") .dashlet-header h4");
			if(i == 4)
				dashLetNameCtrl.assertEquals(customData.get(i-1).get("dashletsName") + " " + cal.get(Calendar.YEAR) + " Q" + quarter, true);
			else 
				dashLetNameCtrl.assertEquals(customData.get(i-1).get("dashletsName"), true);
		}

		// Verifying dashlets in R.H.S (Pipeline/Top 10 sales rli items)
		new VoodooControl("h4", "css", ".dashlets li:nth-child(2) .row-fluid.sortable .dashlet-header h4").assertEquals(customData.get(4).get("dashletsName"), true);
		new VoodooControl("h4", "css", ".dashlets li:nth-child(2) .row-fluid.sortable:nth-of-type(2) .dashlet-header h4").assertEquals(customData.get(5).get("dashletsName"), true);

		// Verifying PipeLine dashlet Data
		new VoodooControl("g", "css", "[data-voodoo-name='forecast-pipeline'] .nv-label-value").hover();
		VoodooUtils.waitForReady();
		for (int i = 0; i < 3; i++) {
			tooltipTextCtrl.assertContains(customData.get(i).get("pipeLineDashletData"), true);
		}

		// Verifying Top 10 sales RLI Data
		new VoodooControl("g", "css", ".dashlets li:nth-child(2) .row-fluid.sortable:nth-of-type(2) .nv-groups").hover();
		VoodooUtils.waitForReady();
		tooltipTextCtrl.assertContains(customData.get(0).get("top10RLIDashletData"), true);
		tooltipTextCtrl.assertContains(customData.get(1).get("top10RLIDashletData"), true);
		tooltipTextCtrl.assertContains(customData.get(2).get("top10RLIDashletData") + currentDate, true);

		// Verifying Forecast Bar Chart Dashlet
		new VoodooControl("text", "css", "div.nv-chart.nv-pareto svg g.nv-y.nv-axis g:nth-child(14) text").assertContains(customData.get(0).get("forecastBarCharDashletData"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}