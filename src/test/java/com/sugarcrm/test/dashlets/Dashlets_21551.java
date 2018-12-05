package com.sugarcrm.test.dashlets;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21551 extends SugarTest {
	DataSource opportunityReccord = new DataSource();
	FieldSet dashletData = new FieldSet();
	UserRecord newUser;

	public void setup() throws Exception {
		opportunityReccord = testData.get(testName);
		dashletData = testData.get(testName+"_dashletData").get(0);
		sugar.opportunities.api.create(opportunityReccord);
		sugar.login();

		// Create custom user
		newUser = (UserRecord) sugar.users.create();
		VoodooUtils.waitForReady(); // Needed extra wait

		// Logout from the admin user and login as custom user
		sugar.logout();
		newUser.login();

		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");
		VoodooSelect selectReportCtrl = new VoodooSelect("div", "css", ".fld_saved_report_id .select2-container");

		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		// Add a Dashlet
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);

		// Add a Dashlet -> Select "Saved Reports Chart Dashlet" tab in toggle drawer
		dashletSearchCtrl.set(dashletData.get("dashlet"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady(); // Extra wait needed 
		selectReportCtrl.set(dashletData.get("selectReport"));
		sugar.alerts.waitForLoadingExpiration();

		// Save Dashlet
		saveBtnCtrl.click();
		sugar.alerts.waitForLoadingExpiration();

		// Save the home page Dashboard
		// TODO: VOOD-1645
		new VoodooControl("a", "css", ".fld_save_button a").click();
		sugar.alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify that chart data can be included/excluded by clicking on chart legend
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21551_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960
		VoodooControl circleColdCallCtrl = new VoodooControl("g", "css", "[data-voodoo-name='saved-reports-chart'] g.nv-legendWrap g g g g:nth-child(1)");
		VoodooControl barChartColdCallCtrl = new VoodooControl("rect", "css", "[data-voodoo-name='saved-reports-chart'] .nv-series-0 g:nth-child(1) rect");
		VoodooControl circlePartnerCtrl = new VoodooControl("g", "css", "[data-voodoo-name='saved-reports-chart'] g.nv-legendWrap g g g g:nth-child(2)");
		VoodooControl barChartPartnerCtrl = new VoodooControl("rect", "css", "[data-voodoo-name='saved-reports-chart'] .nv-series-1 g:nth-child(2) rect");

		// Verify default state of circle and bar chart
		Assert.assertFalse("Circle is not solid", circleColdCallCtrl.isDisabled());
		barChartColdCallCtrl.assertVisible(true);
		Assert.assertFalse("Circle is not solid", circlePartnerCtrl.isDisabled());
		barChartPartnerCtrl.assertVisible(true);

		// Click the circle next to 'Cold Call' type in the legend at the top of the dashlet
		circleColdCallCtrl.click();

		// Verify, when clicked the circle next to 'Cold Call' lead source type changes from solid to a white center
		Assert.assertTrue("Circle does not change from solid to a white center", circleColdCallCtrl.isDisabled());

		// Verify bar chart for that value is removed from the chart
		barChartColdCallCtrl.assertVisible(false);

		// Verify that clicking again will become solid and will add the value back to chart
		circleColdCallCtrl.click();
		Assert.assertFalse("Circle does not become solid", circleColdCallCtrl.isDisabled());
		barChartColdCallCtrl.assertVisible(true);

		// Click the circle next to 'Partner' type in the legend at the top of the dashlet
		circlePartnerCtrl.click();

		// Verify, when clicked the circle next to 'Partner' lead source type changes from solid to a white center
		Assert.assertTrue("Circle does not change from solid to a white center", circlePartnerCtrl.isDisabled());

		// Verify bar chart for that value is removed from the chart
		barChartPartnerCtrl.assertVisible(false);

		// Verify that clicking again will become solid and will add the value back to chart
		circlePartnerCtrl.click();
		Assert.assertFalse("Circle does not become solid", circlePartnerCtrl.isDisabled());
		barChartPartnerCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}