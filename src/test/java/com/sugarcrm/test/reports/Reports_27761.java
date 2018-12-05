package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_27761 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Report on Knowledge Base module displays related Cases module twice
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_27761_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to report module and click on Create Report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// click onRows and Columns Report
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();

		// click on custom module i.e. Knowledge Base
		new VoodooControl("table", "css", "[id='Knowledge Base']").click();
		VoodooUtils.waitForReady();
		DataSource ds = testData.get(testName);

		// temp variable 
		int count = 0;
		// loop through all the modules listed in left hand column to Confirm the Cases module is only listed once.
		for (int i = 1; i < ds.size()-1; i++) {
			//String moduleName = new VoodooControl("a", "css", "#module_tree div  div  div  div:nth-child("+i+") > table").getText();

			if(new VoodooControl("a", "css", "#module_tree div  div  div  div:nth-child("+i+") > table").queryContains(sugar().cases.moduleNamePlural, true)){
				if(count == 0) {
					// Verify Cases module labels are correct in related module folder.
					new VoodooControl("a", "css", "#module_tree div  div  div  div:nth-child("+i+") > table").assertContains(ds.get(i-3).get("module_plural_name"), true);
					count++;
				}else{
					// script will fail here if Case module is listed more then once
					new VoodooControl("a", "css", "#module_tree div  div  div  div:nth-child("+i+") > table").assertContains(ds.get(i-3).get("module_plural_name"), false);
				}
			}
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
