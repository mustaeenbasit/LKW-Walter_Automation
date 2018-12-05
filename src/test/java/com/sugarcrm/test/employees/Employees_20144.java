package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20144 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Can't create relationship between Employees and other modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20144_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		// TODO VOOD-542
		new VoodooControl("div", "id", "mblayout").waitForVisible();
		for(int i=0;i<ds.size();i++) {
			// open create relationship window from studio
			new VoodooControl("a", "id", "studiolink_"+ds.get(i).get("module")).click();
			new VoodooControl("td", "id", "relationshipsBtn").waitForVisible();
			new VoodooControl("a", "css", "#relationshipsBtn a.studiolink").click();
			new VoodooControl("div", "id", "relGrid").waitForVisible();
			new VoodooControl("input", "css", "div#mbtabs input[name='addrelbtn']").click();
			new VoodooControl("form", "css", "form[name='relform']").waitForVisible();
			// check no Employees option show in related module list
			new VoodooControl("option", "css", "select#rhs_mod_field option[value='"+ds.get(i).get("employees")+"']").assertExists(false);
			new VoodooControl("input", "css", "input[name='cancelbtn']").click();
			new VoodooControl("a", "css", "div.bodywrapper div a:nth-child(3)").click();
			new VoodooControl("div", "id", "mblayout").waitForVisible();
		}	
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}