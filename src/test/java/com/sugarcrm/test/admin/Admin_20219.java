package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20219 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Check Time Format field in user settings page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20219_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		int i;
		for(i=0;i<ds.size();i++) {
			sugar().users.userPref.getControl("advanced_timeFormat").assertElementContains(ds.get(i).get("timeformat"), true);
		}
		// TODO VOOD-922
		new VoodooControl("option", "css", "select[name='timeformat'] option:nth-of-type("+ i+1 +")").assertExists(false);
						
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}