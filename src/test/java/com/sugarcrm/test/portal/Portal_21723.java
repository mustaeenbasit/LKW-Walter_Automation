package com.sugarcrm.test.portal;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_21723 extends PortalTest {
	ContactRecord myCon;

	public void setup() throws Exception {
		DataSource ds = testData.get(testName);
		FieldSet portalContactData = testData.get("env_portal_contact_setup").get(0);
		FieldSet fs = testData.get(testName + "_data").get(0);

		// Create an Account record, a Contact record and a Case record
		AccountRecord accountRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().cases.api.create(ds);
		myCon = (ContactRecord) sugar().contacts.api.create(portalContactData);

		// Login
		sugar().login();

		// Enable portal
		sugar().admin.portalSetup.enablePortal();

		// Link Contact with Accounts
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(accountRecord.getRecordIdentifier());
		sugar().alerts.getAlert().cancelAlert();
		sugar().contacts.recordView.save();

		// Mass Update all Cases records
		sugar().cases.navToListView();
		sugar().cases.listView.toggleSelectAll();
		sugar().cases.listView.clickSelectAllRecordsLink();
		sugar().cases.listView.openActionDropdown();
		sugar().cases.listView.massUpdate();

		// TODO: VOOD-1126
		// need to update cases record to show in portal 
		new VoodooSelect("div","css", "div[data-voodoo-name='Cases'] .filter-body.clearfix:nth-of-type(2) .controls.filter-field").set(fs.get("show_in_portal"));
		new VoodooSelect("div","css", "div[data-voodoo-name='Cases'] .filter-body.clearfix:nth-of-type(2) .controls.filter-value").set(fs.get("confirm_add"));
		new VoodooControl("button", "css", "#content .layout_Cases div.filter-actions.btn-group button").click();
		new VoodooSelect("div","css", "div[data-voodoo-name='Cases'] .filter-body.clearfix:nth-of-type(3) .controls.filter-field").set(fs.get("account_name"));
		new VoodooSelect("div","css", "div[data-voodoo-name='Cases'] .filter-body.clearfix:nth-of-type(3) .controls.filter-value").set(accountRecord.getRecordIdentifier());
		sugar().cases.massUpdate.update();
		VoodooUtils.waitForReady(120000); // Mass update taking too much time to update 100 records. 
		sugar().logout();
	}

	/**
	 *  Verify Portal user is able to search case by the "Num".
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21723_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigate to case module 
		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1096
		String firstCaseNumber = new VoodooControl("div", "css", "#content .list-view  tr:nth-of-type(1) td:nth-of-type(1) div").getAttribute("data-original-title");
		String firstCaseSubject = new VoodooControl("div", "css", "#content .list-view  tr:nth-of-type(1) td:nth-of-type(2) div").getAttribute("data-original-title");

		String secondCaseNumber = new VoodooControl("div", "css", "#content .list-view  tr:nth-of-type(2) td:nth-of-type(1) div").getAttribute("data-original-title");
		String secondCaseSubject = new VoodooControl("div", "css", "#content .list-view  tr:nth-of-type(2) td:nth-of-type(2) div").getAttribute("data-original-title");

		String thirdCaseNumber = new VoodooControl("div", "css", "#content .list-view  tr:nth-of-type(3) td:nth-of-type(1) div").getAttribute("data-original-title");
		String thirdCaseSubject = new VoodooControl("div", "css", "#content .list-view  tr:nth-of-type(3) td:nth-of-type(2) div").getAttribute("data-original-title");

		// TODO: VOOD-1096 - Portal Module Listview support 
		VoodooControl searchCtrl= new VoodooControl("input", "css", "#content [data-voodoo-name='filter'] input");

		// search number in list
		searchCtrl.set(firstCaseNumber);
		VoodooUtils.waitForReady();

		// assert number (i.e. 2 ) in listview 
		VoodooControl caseNameCtrl= new VoodooControl("span", "css", "#content  div.list-view [data-voodoo-name='name']");
		caseNameCtrl.assertContains(firstCaseSubject, true);

		VoodooControl subjectCtrl =  new VoodooControl("a", "css", "#content .list-view  td:nth-child(2)  a");
		// click number in listview to navigate to case details view 
		subjectCtrl.click();
		VoodooUtils.waitForReady();

		VoodooControl caseNumberCtrl = new VoodooControl("span", "css", "#content div.record [data-voodoo-name='case_number']");

		// assert number (i.e. 2 ) on  record view  
		caseNumberCtrl.assertEquals(firstCaseNumber, true);

		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		VoodooUtils.waitForReady();

		// search number in list
		searchCtrl.set(secondCaseNumber);
		VoodooUtils.waitForReady();

		// assert number (i.e. 10 ) in listview 
		caseNameCtrl.assertContains(secondCaseSubject, true);

		// click number in listview to navigate to case details view 
		subjectCtrl.click();
		VoodooUtils.waitForReady();

		// assert number (i.e. 10 ) on  record view 
		caseNumberCtrl.assertEquals(secondCaseNumber, true);

		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		VoodooUtils.waitForReady();

		// search number in list
		searchCtrl.set(thirdCaseNumber);
		VoodooUtils.waitForReady();

		// assert number (i.e. 100 ) in listview 
		caseNameCtrl.assertContains(thirdCaseSubject, true);

		// click number in listview to navigate to case details view 
		subjectCtrl.click();
		VoodooUtils.waitForReady();

		// assert number (i.e. 100 ) on  record view 
		caseNumberCtrl.assertEquals(thirdCaseNumber, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}