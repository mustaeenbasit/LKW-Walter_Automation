package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20185 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that label in export setting is "Default Character Set for Import and Export"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20185_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-820
		new VoodooControl("a", "css" ,"#locale").click();
		new VoodooControl("td", "css" ,"form#ConfigureSettings table:nth-of-type(4) tr:nth-of-type(2) td:nth-of-type(3)").assertEquals(ds.get(0).get("label"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}