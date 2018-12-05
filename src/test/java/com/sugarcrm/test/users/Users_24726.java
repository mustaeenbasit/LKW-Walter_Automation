package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24726 extends SugarTest {
	UserRecord chrisUser;
	FieldSet tableData = new FieldSet();

	public void setup() throws Exception {
		// Initialize Test Data
		tableData = testData.get(testName).get(0);
		
		// Create a new user
		chrisUser = (UserRecord) sugar().users.api.create();

		// Login as admin
		sugar().login();

	}

	/**
	 * User-Role_Verify that the default access privilege for a new user is enabled for all modules.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24726_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open chrisUsers record view
		sugar().users.navToListView();
		sugar().users.listView.basicSearch(chrisUser.get("userName"));
		sugar().users.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click Access Tab
		// TODO: VOOD-563
		new VoodooControl("a", "id", "tab3").click();

		// Verify Access for All Modules = Enabled, Normal, All, All ...
		// Access Table Rows count = 41 (except 36-39 are tracker rows)
		verifyAccessTableRows(0, Integer.parseInt(tableData.get("trCount")));
		verifyAccessTableRows(Integer.parseInt(tableData.get("trStartAfterTrackers")), Integer.parseInt(tableData.get("trEndAfterTrackers")));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	private void verifyAccessTableRows(int startTr, int endTr) throws Exception {
		for (int tr = startTr; tr < endTr; tr++) {
			// TODO: VOOD-563
			// First Column data = "Enabled"  // Skip first 2 rows are headers
			new VoodooControl("b", "css", "#user_detailview_tabs tr:nth-child("+ (tr+2)+ ") td:nth-child(2) .aclEnabled b").assertEquals(tableData.get("enabled"), true);
			// Second Column data = "Normal"
			new VoodooControl("b", "css", "#user_detailview_tabs tr:nth-child("+ (tr+2)+ ") td:nth-child(3) .aclNormal b").assertEquals(tableData.get("normal"), true);
			// Remaining 7 Columns data = "All" // Skip first 4 Columns are headers
			for (int td = 0; td < Integer.parseInt(tableData.get("tdCount")); td++)
				new VoodooControl("b", "css", "#user_detailview_tabs tr:nth-child("+ (tr+2)+ ") td:nth-child("+ (td+4)+ ") .aclAll b").assertEquals(tableData.get("all"), true);
		}
	}
	
	public void cleanup() throws Exception {}
}