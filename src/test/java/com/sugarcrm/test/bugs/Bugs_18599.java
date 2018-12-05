package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18599 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		// create two bug records with different name
		sugar.bugs.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar.bugs.api.create(fs);
		sugar.login();

		// Enable bugs module
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);

		// Assign one bugs record to Qauser
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		sugar.bugs.recordView.edit();
		sugar.bugs.recordView.getEditField("relAssignedTo").set(sugar.users.qaUser.get("userName"));
		sugar.bugs.recordView.save();
	}

	/**
	 * search bug with advanced_search
	 * @throws Exception
	 */
	@Test
	public void Bugs_18599_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();
		sugar.bugs.listView.openFilterDropdown();
		sugar.bugs.listView.selectFilterAssignedToMe();
		sugar.bugs.listView.setSearchString(sugar.bugs.getDefaultData().get("name"));
		
		// Verify that the related records are displayed
		sugar.bugs.listView.verifyField(1, "name", sugar.bugs.getDefaultData().get("name"));
		sugar.bugs.listView.clearSearch();
		
		// Logout as Admin and Login as Qauser
		sugar.logout();
		sugar.login(sugar.users.getQAUser());
		
		sugar.bugs.navToListView();
		sugar.bugs.listView.openFilterDropdown();
		sugar.bugs.listView.selectFilterAssignedToMe();
		sugar.bugs.listView.setSearchString(testName);
		
		// Verify that the related records are displayed
		sugar.bugs.listView.verifyField(1, "name", testName);
		sugar.bugs.listView.clearSearch();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}