package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20146 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Shouldn't show Employees in related module list on the convert lead layout
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20146_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		// TODO VOOD-542
		new VoodooControl("div", "id", "mblayout").waitForVisible();
		// open convert lead layout edit window from studio
		new VoodooControl("a", "id", "studiolink_Leads").click();
		new VoodooControl("td", "id", "layoutsBtn").waitForVisible();
		new VoodooControl("a", "css", "#layoutsBtn a.studiolink").click();
		new VoodooControl("td", "id", "viewBtnrecordview").waitForVisible();
		new VoodooControl("a", "css", "td#layoutsBtn a.studiolink").click();
		new VoodooControl("div", "id", "relGrid").waitForVisible();
		// check no Employees option show in related module list
		new VoodooControl("option", "css", "select#convertSelectNewModule option[value='"+ds.get(0).get("employees")+"']").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}