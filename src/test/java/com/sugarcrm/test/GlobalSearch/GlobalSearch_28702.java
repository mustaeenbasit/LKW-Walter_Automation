package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28702 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify Dashboard Framework for Facets
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28702_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName);
		
		// Enter any value in search bar & hit Enter.
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		sugar().navbar.getControl("globalSearch").set(sugar().accounts.getDefaultData().get("workPhone") + '\uE007');
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1848 - Need library support to check enable/disable property of preview button and RHS pane support on Global search results page
		// Verifying Dashboard for the Facet dashlets is in place on the right hand pane.
		VoodooControl facetDashboardCtrl = new VoodooControl("div", "css", ".side.sidebar-content");
		facetDashboardCtrl.assertCssAttribute(customData.get(0).get("facetDashletAttribute"), customData.get(0).get("facetDashletAttributeValue"), true);
		
		// Verifying all available options in Facet dashlet
		for (int i = 0; i < customData.size(); i++) {
			facetDashboardCtrl.assertContains(customData.get(i).get("facetDashletAvailableOptions"), true);
		}		
		
		// Controls for switching dashboard between facet and help dashboard
		VoodooControl dashboardDropDownCtrl = new VoodooControl("i", "css", ".side.sidebar-content .dropdown i");
		VoodooControl switchDashboardCtrl = new VoodooControl("li", "css", ".dropdown.open li");
		VoodooControl facetDashletCtl = new VoodooControl("div", "css", ".facets-dashboard");
		VoodooControl helpDashletCtrl = new VoodooControl("div", "css", ".help-dashlet-content");
		
		// Switch to help DashBoard
		dashboardDropDownCtrl.click();
		switchDashboardCtrl.click();
		
		// Verifying help dashboard is appearing and facet dashlet is not appearing
		helpDashletCtrl.assertVisible(true);
		facetDashletCtl.assertVisible(false);
		
		// Switch to facet dashboard
		dashboardDropDownCtrl.click();
		switchDashboardCtrl.click();
		
		// Verifying facet dashboard is appearing and help dashboard is not appearing
		facetDashletCtl.assertVisible(true);
		helpDashletCtrl.assertVisible(false);
		
		// Click on Close dashboard ">>"
		new VoodooControl("button", "css", "[aria-label='Close Dashboard']").click();
		
		// Search account name after closing dashboard
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		sugar().navbar.getControl("globalSearch").set(sugar().accounts.getDefaultData().get("name") + '\uE007');
		
		// Verifying facet dashboard is still closed
		facetDashboardCtrl.assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}