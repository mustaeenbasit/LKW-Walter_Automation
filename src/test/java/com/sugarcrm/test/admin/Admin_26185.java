package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26185 extends SugarTest {	
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Only default module is available.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_26185_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// click on Accounts in studio panel 
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		
		// Add a many-many relationship with Leads.
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#rhs_mod_field option[value='Leads']").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		new VoodooControl("select", "id", "lhs_subpanel").click();
		
		// Verify, in drop down of 'Subpanel from Accounts:', there are 2 values including default.
		new VoodooControl("option", "css", "#lhs_subpanel option:nth-child(1)").assertContains(customData.get("dropdown1"), true);
		new VoodooControl("option", "css", "#lhs_subpanel option:nth-child(2)").assertContains(customData.get("dropdown2"), true);
		
		// TODO: VOOD-938
		new VoodooControl("select", "id", "rhs_subpanel").click();
		
		// Verify, in drop down of 'Subpanel from Leads:', there is 1 value which is default.
		new VoodooControl("option", "css", "#rhs_subpanel option").assertContains(customData.get("dropdown1"), true);
		
		new VoodooControl("input", "css", "[name='cancelbtn']").click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
