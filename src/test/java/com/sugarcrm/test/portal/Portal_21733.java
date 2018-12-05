package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21733 extends PortalTest {
	DataSource bugDS = new DataSource();

	public void setup() throws Exception {
		bugDS = testData.get(testName);
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);

		// Create an Account
		AccountRecord accountRecord = (AccountRecord) sugar().accounts.api.create();

		// Create contact for portal access
		ContactRecord myCon = (ContactRecord)sugar().contacts.api.create(portalContactData);

		// Setup Portal Access
		sugar().login();

		// Link Contact with Accounts
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(accountRecord.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		// Enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable Portal
		sugar().admin.portalSetup.enablePortal();

		// Logout from admin user
		sugar().logout();

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);
	}

	/**
	 * Verify that Portal user is able to search bug by the Subject
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21733_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Report Bug link
		// TODO: VOOD-1116
		new VoodooControl("a", "css", "a[title='Report Bug']").click();

		// Report Bugs
		for(FieldSet bugData : bugDS) {
			portal().bugs.recordView.getEditField("name").set(bugData.get("name"));
			portal().bugs.recordView.getEditField("description").set(bugData.get("description"));
			portal().bugs.recordView.save();
			VoodooUtils.waitForReady();

			// Report next Bug
			portal().bugs.listView.create();
		}

		// Goto Portal home
		new VoodooControl("a", "css", "a[aria-label='Home']").click();
		new VoodooControl("a", "css", ".dashboard .thumbnail .headerpane h1 a").waitForVisible();
		new VoodooControl("a", "css", "div.thumbnail.layout_Bugs a.btn.btn-invisible.search.active").waitForVisible();

		// Make Search textbox visible if not visible
		if (!(new VoodooControl("input", "css", "div.thumbnail.layout_Bugs div.dataTables_filter").queryVisible()))
			new VoodooControl("a", "css", "div.thumbnail.layout_Bugs a.btn.btn-invisible.search.active").click();

		// Check Expected Results
		for(FieldSet bugDataR : bugDS) {
			// Find Bug by name
			new VoodooControl("input", "css", "div.thumbnail.layout_Bugs div.dataTables_filter input").set(bugDataR.get("name"));
			VoodooUtils.waitForReady();
			new VoodooControl("tr", "xpath", "//div[@class='thumbnail layout_Bugs']/div[@class='list-view']/table/tbody/tr[contains(.,'"+bugDataR.get("name")+"')]").assertExists(true);
			new VoodooControl("tr", "xpath", "//div[@class='thumbnail layout_Bugs']/div[@class='list-view']/table/tbody/tr[contains(.,'"+bugDataR.get("not_to_be_found1")+"')]").assertExists(false);
			new VoodooControl("tr", "xpath", "//div[@class='thumbnail layout_Bugs']/div[@class='list-view']/table/tbody/tr[contains(.,'"+bugDataR.get("not_to_be_found2")+"')]").assertExists(false);
		}

		// Should not find Bug by a dummy name
		new VoodooControl("input", "css", "div.thumbnail.layout_Bugs div.dataTables_filter input").set("Garbage");
		VoodooUtils.waitForReady();
		new VoodooControl("tr", "xpath", "//div[@class='thumbnail layout_Bugs']/div[@class='list-view']/table/tbody/tr").assertExists(false);;

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}
