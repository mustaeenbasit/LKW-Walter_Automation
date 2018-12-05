package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_19089 extends SugarTest {

	public void setup() throws Exception {
		// Creating Calls record
		sugar().calls.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().calls.api.create(fs);
		fs.clear();
		fs.put("name", testName+testName);
		sugar().calls.api.create(fs);
		sugar().login();  
	}

	/**
	 * Mass update Call: Verify that calls are displayed in "My Calls" dashlet on a user's Home page when the calls are assigned to that user.
	 * @throws Exception
	 */
	@Test
	public void Calls_19089_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// TODO: VOOD-1200 - Authentication failed on calling Users default data
		// Once this VOOD resolved, CSV can be removed and user can be created from API
		// Create user
		FieldSet userData = testData.get(testName+"_userData").get(0);
		UserRecord myUserRecord = (UserRecord) sugar().users.create(userData);

		// Calls => mass update => assignedTo->new user
		sugar().calls.navToListView();
		sugar().calls.listView.checkRecord(2);
		sugar().calls.listView.checkRecord(3);
		sugar().calls.listView.openActionDropdown();
		sugar().calls.listView.massUpdate();
		FieldSet customData = testData.get(testName).get(0);
		sugar().calls.massUpdate.getControl("massUpdateField02").set(customData.get("assignedTo"));
		sugar().calls.massUpdate.getControl("massUpdateValue02").set(myUserRecord.get("lastName"));
		sugar().calls.massUpdate.update();

		// Logout as Admin
		sugar().logout();

		// Login as new user
		sugar().login(myUserRecord);

		// TODO: VOOD-592 - Add dashlet support to home screen model.
		// TODO: VOOD-670 - More Dashlet Support
		// Default listview dashlet (contacts) changed to "Calls" and save
		VoodooControl dashletCtrl = new VoodooControl("div", "css", ".dashboard .dashlet-row .row-fluid:nth-of-type(3) .dashlet-header div.btn-group");
		dashletCtrl.scrollIntoViewIfNeeded(false);
		dashletCtrl.click();
		new VoodooControl("a", "css", "div.btn-group.open ul li a[data-dashletaction='editClicked']").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("div", "css", ".edit.fld_module div.select2").set(sugar().calls.moduleNamePlural);
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".layout_Home.drawer.active .detail.fld_save_button").click();
		VoodooUtils.waitForReady();

		// Verify displayed only My calls (i.e 2 records) on home dashboard page under My calls dashlet
		new VoodooControl("span", "css", ".sorting.orderByname span").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".dataTable tr:nth-child(2) a").assertEquals(sugar().calls.getDefaultData().get("name"), true);
		new VoodooControl("a", "css", ".dataTable tr a").assertEquals(testName, true);
		new VoodooControl("a", "css", ".dataTable tr:nth-child(3) a").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}