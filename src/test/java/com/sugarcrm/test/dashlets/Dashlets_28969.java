package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_28969 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		// Create 11 records
		customDS = testData.get(testName);
		sugar().tasks.api.create(customDS);
		sugar().login();
	}

	/**
	 * Users Avatar should appear for available task in active task dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28969_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().dashboard.edit();
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(4, 1);

		FieldSet dashletSetup = testData.get("env_dashlets_setup").get(0);

		// TODO: VOOD-960 - Dashlet selection 
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("input", "css", ".span4.search").set( dashletSetup.get("activeDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", ".edit.fld_visibility .select2-container.select2 a .select2-arrow").click();
		new VoodooSelect("li", "css", "#select2-drop ul.select2-results li:nth-child(2)").click();
		new VoodooControl("a", "css", ".span8 div div span.detail.fld_save_button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", ".layout_Home.span4 div.dashlet-tabs.tab3 div div:nth-child(2) a").click();
		new VoodooControl("button", "css", "[data-action='show-more']").click();
		VoodooUtils.waitForReady();

		// Verify that Avatar should appear with assigned user name for the more available tasks
		for(int i = 0; i < customDS.size(); i++) {
			// Xpath required to verify inconsistent behavior of Active tasks > Upcoming > list
			new VoodooControl("a", "xpath", "//*[@data-voodoo-name='active-tasks']/div[2]/div[2]/div/ul/li[contains(.," + customDS.get(i).get("subject") + ")]").assertExists(true);
			new VoodooControl("a", "css", "[data-voodoo-name='active-tasks'] .tab-pane.active ul li:nth-child(" + (i + 1) + ") .pull-left.avatar.avatar-md img").assertExists(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}