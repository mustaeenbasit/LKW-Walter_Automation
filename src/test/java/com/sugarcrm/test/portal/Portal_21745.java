package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21745 extends PortalTest {
	DataSource ds = new DataSource();
	DataSource caseNoDS = new DataSource();
	CaseRecord myCase1, myCase2, myCase3;
	AccountRecord myAcc;
	FieldSet  portalContactData = new FieldSet();
	
	public void setup() throws Exception {
		ds = testData.get(testName);
		portalContactData = testData.get("env_portal_contact_setup").get(0);

		// Setup Portal Access
		sugar().login();
		sugar().admin.portalSetup.enablePortal();

		myAcc = (AccountRecord)sugar().accounts.api.create();

		// Create contact for portal access
		ContactRecord myCon = (ContactRecord)sugar().contacts.api.create(portalContactData);

		// Add Account to Contact
		myCon.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Create several Cases recs linked to this Contact
		FieldSet fs = new FieldSet();
		fs = sugar().cases.getDefaultData();
		fs.put("name", ds.get(0).get("name"));
		fs.put("status", ds.get(0).get("status"));
		myCase1 = (CaseRecord) sugar().cases.api.create(fs);
		
		fs = new FieldSet();
		fs = sugar().cases.getDefaultData();
		fs.put("name", ds.get(1).get("name"));
		fs.put("status", ds.get(1).get("status"));
		myCase2 = (CaseRecord) sugar().cases.api.create(fs);
		
		fs = new FieldSet();
		fs = sugar().cases.getDefaultData();
		fs.put("name", ds.get(2).get("name"));
		fs.put("status", ds.get(2).get("status"));
		myCase3 = (CaseRecord) sugar().cases.api.create(fs);
		
		// Enable all Case recs to be viewable in Portal
		myCase1.navToRecord();
		sugar().cases.recordView.edit();
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set("true");
		new VoodooControl("a", "css", "span.fld_type.edit a").click();
		new VoodooControl("li", "xpath", "//li[contains(.,'"+ds.get(0).get("type")+"')][@role='presentation']").click();		
		sugar().cases.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().cases.recordView.getEditField("priority").set(ds.get(0).get("priority"));
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		myCase2.navToRecord();
		sugar().cases.recordView.edit();
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set("true");
		new VoodooControl("a", "css", "span.fld_type.edit a").click();
		new VoodooControl("li", "xpath", "//li[contains(.,'"+ds.get(1).get("type")+"')][@role='presentation']").click();
		sugar().cases.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().cases.recordView.getEditField("priority").set(ds.get(1).get("priority"));
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		myCase3.navToRecord();
		sugar().cases.recordView.edit();
		new VoodooControl("input", "css", "span.fld_portal_viewable.edit input").set("true");
		new VoodooControl("a", "css", "span.fld_type.edit a").click();
		new VoodooControl("li", "xpath", "//li[contains(.,'"+ds.get(2).get("type")+"')][@role='presentation']").click();
		sugar().cases.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().cases.recordView.getEditField("priority").set(ds.get(2).get("priority"));
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().logout();
		
		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myCon.get("portalName"));
		portalUser.put("password", myCon.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);
		new VoodooControl("div", "css", ".dashboard .thumbnail").assertVisible(true);
	}
	
	/**
	 * Verify that columns can be sorted in order in cases module of portal list view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Portal_21745_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Collect case numbers
		FieldSet caseNo;
		caseNoDS = new DataSource();
		for(FieldSet caseDataN : ds) {
			caseNo = new FieldSet();		
			caseNo.put("name", caseDataN.get("name"));
			caseNo.put("caseNo", new VoodooControl("a","xpath", "//div[@class='thumbnail layout_Cases']/div[@class='list-view']/table/tbody/tr[contains(.,'"+caseDataN.get("name")+"')]/td[1]").getText());
			caseNoDS.add(caseNo);
		}

		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// Sort Desc by Subject
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='name']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(2) > span > div > a").assertContains(ds.get(2).get("name"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(2) > span > div > a").assertContains(ds.get(1).get("name"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(2) > span > div > a").assertContains(ds.get(0).get("name"), true);
		
		// Sort Asc by Subject
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='name']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(2) > span > div > a").assertContains(ds.get(0).get("name"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(2) > span > div > a").assertContains(ds.get(1).get("name"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(2) > span > div > a").assertContains(ds.get(2).get("name"), true);
		
		// Restore original Subject Order
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='name']").click();

		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// Sort Desc by Status
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='status']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(3) > span").assertContains(ds.get(0).get("status"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(3) > span").assertContains(ds.get(1).get("status"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(3) > span").assertContains(ds.get(2).get("status"), true);
		
		// Sort Asc by Status
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='status']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(3) > span").assertContains(ds.get(2).get("status"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(3) > span").assertContains(ds.get(1).get("status"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(3) > span").assertContains(ds.get(0).get("status"), true);

		// Restore original Status Order
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='status']").click();

		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// Sort Desc by Priority
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='priority']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(4) > span").assertContains(ds.get(2).get("priority"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(4) > span").assertContains(ds.get(1).get("priority"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(4) > span").assertContains(ds.get(0).get("priority"), true);
		
		// Sort Asc by Priority
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='priority']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(4) > span").assertContains(ds.get(0).get("priority"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(4) > span").assertContains(ds.get(1).get("priority"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(4) > span").assertContains(ds.get(2).get("priority"), true);

		// Restore original Priority Order
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='priority']").click();

		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// Sort Desc by Type
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='type']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(5) > span").assertContains(ds.get(2).get("type"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(5) > span").assertContains(ds.get(1).get("type"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(5) > span").assertContains(ds.get(0).get("type"), true);
		
		// Sort Asc by Type
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='type']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(5) > span").assertContains(ds.get(0).get("type"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(5) > span").assertContains(ds.get(1).get("type"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(5) > span").assertContains(ds.get(2).get("type"), true);

		// Restore original Type Order
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='type']").click();

		portal().navbar.navToModule(portal().cases.moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();

		// Sort Desc by Number
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='case_number']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(1) > span").assertContains(caseNoDS.get(2).get("caseNo"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(1) > span").assertContains(caseNoDS.get(1).get("caseNo"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(1) > span").assertContains(caseNoDS.get(0).get("caseNo"), true);
		
		// Sort Asc by Number
		new VoodooControl("th", "css", "div.list-view > table > thead > tr > th[data-fieldname='case_number']").click();
		VoodooUtils.waitForReady(); // Allow Sort to complete
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(1) > td:nth-child(1) > span").assertContains(caseNoDS.get(0).get("caseNo"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(2) > td:nth-child(1) > span").assertContains(caseNoDS.get(1).get("caseNo"), true);
		new VoodooControl("a", "css", "div.list-view > table > tbody > tr:nth-child(3) > td:nth-child(1) > span").assertContains(caseNoDS.get(2).get("caseNo"), true);
		
		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}