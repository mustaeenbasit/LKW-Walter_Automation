package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_30618 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Reports: Modules should appear in ascending order for Report wizard.
	 * @throws Exception
	 */
	@Test
	public void Reports_30618_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource modulesNames = testData.get(testName);
		
		// Navigate to report module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Create summation report
		// TODO: VOOD-822
		new VoodooControl("name", "css", "img[name='summationWithDetailsImg']").click();
		
		// Verifying Modules are appearing in ascending order
		for (int i = 0, row = 1, coloumn = 1; i < modulesNames.size(); i++) {
			new VoodooControl("table", "css", "#buttons_table tr:nth-child(" + row + ") td:nth-child(" + coloumn + ") .wizardButton").assertEquals(modulesNames.get(i).get("Modules"), true);
			coloumn++;
			if(coloumn > 6)
			{
				// Switching to next row in layout
				row++;
				coloumn = 1;
			}	
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}