package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_28966 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		// Create 10 records
		customDS = testData.get(testName);
		sugar().tasks.api.create(customDS);
		sugar().login();
	}

	/**
	 * Upcoming task should be listed as per default settings in active task dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28966_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().dashboard.edit();
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(4, 1);

		FieldSet dashletSetup = testData.get("env_dashlets_setup").get(0);

		// TODO: VOOD-1004 -Library support need to create dashlet
		new VoodooControl("input", "css", ".span4.search").set( dashletSetup.get("activeDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".span8 div div span.detail.fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify it should list 10 task in dashlet.
		String numberOfRecord = String.format("%s", (customDS.size()));
		new VoodooControl("span", "css", ".layout_Home.span4 div.dashlet-tabs.tab3 div div:nth-child(2) a span").assertContains(numberOfRecord, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}