package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20145 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Shouldn't show Employees in related module list when creating a related field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20145_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		// TODO VOOD-542
		new VoodooControl("div", "id", "mblayout").waitForVisible();
		for(int i=0;i<ds.size();i++) {
			// open create field window from studio
			new VoodooControl("a", "id", "studiolink_"+ds.get(i).get("module")).click();
			new VoodooControl("td", "id", "fieldsBtn").waitForVisible();
			new VoodooControl("a", "css", "#fieldsBtn a.studiolink").click();
			new VoodooControl("div", "id", "field_table").waitForVisible();
			new VoodooControl("input", "css", "div#mbtabs input[name='addfieldbtn']").click();
			new VoodooControl("input", "id", "field_name_id").waitForVisible();
			new VoodooControl("select", "id", "type").set(ds.get(i).get("type"));
			// check no Employees option show in related module list
			new VoodooControl("option", "css", "select#ext2 option[value='"+ds.get(i).get("employees")+"']").assertExists(false);
			new VoodooControl("input", "css", "input[name='cancelbtn']").click();
			new VoodooControl("a", "css", "div.bodywrapper div a:nth-child(3)").click();
			new VoodooControl("div", "id", "mblayout").waitForVisible();
		}	
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}