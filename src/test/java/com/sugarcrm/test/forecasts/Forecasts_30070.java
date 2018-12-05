package com.sugarcrm.test.forecasts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Forecasts_30070 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify correct text/icons in Forecasts configuration setup page.
	 * @throws Exception
	 */
	
	@Test
	public void Forecasts_30070_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet verificationDataFS = testData.get(testName).get(0);
		
		// Navigate to Forecasts, Commit SalesRep Worksheet
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Verify the text in "Time Periods" section below  fourth point
		// TODO: VOOD-929
		new VoodooControl("div", "css", ".accordion-inner li:nth-child(4) p i").assertContains(verificationDataFS.get("description_Text"), true);
		new VoodooControl("a", "css", "a[id='config-worksheet-columnsTitle'] span").click();
		
		// Verify the pill icon "Forecast" in "Worksheet Columns" section
		new VoodooControl("div", "css", "#config-worksheet-columnsCollapse .record-cell div ul li").assertEquals(sugar().forecasts.moduleNameSingular, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}