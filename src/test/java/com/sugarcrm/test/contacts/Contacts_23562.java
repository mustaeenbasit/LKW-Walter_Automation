package com.sugarcrm.test.contacts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23562 extends SugarTest {
	ContactRecord myContact;
	StandardSubpanel leadsSubpanel;
	DataSource leadData;
	ArrayList<Record> leadRecords;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		leadData = testData.get(testName);
		leadRecords = sugar().leads.api.create(leadData);
		sugar().login();

		// Setting records in Lead Subpanel of Contacts
		myContact.navToRecord();
		leadsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecords(leadRecords);
	}

	/** Sort Leads_Verify that leads related to the contact can be sorted  by column titles in "LEADS" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23562_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		leadsSubpanel.scrollIntoView();
		// TODO: VOOD-609
		// Click "name" column in Leads Subpanel
		new VoodooControl("th", "css", ".tabs-left.layout_Leads th.sorting.orderByfull_name").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify Lead records are sorted according to the name column
		for (int i = 1 ; i <= leadData.size() ; i++){
			leadsSubpanel.verify(i, leadData.get(leadData.size()-i), true);
		}

		// Click "Office Phone" column in Leads Subpanel
		new VoodooControl("th", "css", ".tabs-left.layout_Leads th.sorting.orderByphone_work").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify Lead records are sorted according to the "office phone" column
		for (int i = 1 ; i <= leadData.size() ; i++){
			leadsSubpanel.verify(i, leadData.get(leadData.size()-i), true);
		}
		// Collapse Subpanel
		leadsSubpanel.collapseSubpanel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
