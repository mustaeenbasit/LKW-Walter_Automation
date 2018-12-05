package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20220 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Check Time Format field in Locale settings page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20220_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO VOOD-820
		new VoodooControl("a", "css", "#locale").click();
		int i;
		for(i=0;i<ds.size();i++) {
			new VoodooControl("select", "css", "select[name='default_time_format']").assertElementContains(ds.get(i).get("timeformat"), true);
		}
		// TODO VOOD-922
		new VoodooControl("option", "css", "select[name='default_time_format'] option:nth-of-type("+ i+1 +")").assertExists(false);
								
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}