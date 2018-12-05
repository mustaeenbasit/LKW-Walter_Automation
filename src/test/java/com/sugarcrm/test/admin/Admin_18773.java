package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_18773 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify elastic_boost_option is removed from dropdown editor.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_18773_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-781
		new VoodooControl("a", "css" ,"#dropdowneditor").click();
		new VoodooControl("a", "xpath" ,"//div[@id='dropdowns']//a[text()='"+ds.get(0).get("label")+"']").assertExists(false);
		new VoodooControl("a", "xpath" ,"//div[@id='mbTree']//a[text()='"+ds.get(0).get("label")+"']").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
