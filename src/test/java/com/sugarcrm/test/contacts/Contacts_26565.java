package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_26565 extends SugarTest {
	AccountRecord myAccount;
	ContactRecord myContact;
	StandardSubpanel directReports;
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		sugar().login();

		ds = testData.get(testName);

		FieldSet newData = new FieldSet();

		newData.put("name", ds.get(0).get("firstName"));
		newData.put("workPhone", ds.get(0).get("firstName"));
		myAccount = (AccountRecord) sugar().accounts.api.create(newData);
		myContact = (ContactRecord) sugar().contacts.api.create(ds.get(0));

		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.confirmAllAlerts();
		sugar().contacts.recordView.save();
		 sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify that Account name is populated when Direct Report is created from the direct report subpanel of
	 * Contact record view
	 * 04/18/2014, Alex N: Created test case from SFA-2509 (https://sugarcrm.atlassian.net/browse/SFA-2509)
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_26565_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();

		directReports = sugar().contacts.recordView.subpanels.get("Contacts");
		directReports.addRecord();

		sugar().contacts.createDrawer.getEditField("relAccountName").assertElementContains(ds.get(0).get("firstName"), true);
		sugar().contacts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
