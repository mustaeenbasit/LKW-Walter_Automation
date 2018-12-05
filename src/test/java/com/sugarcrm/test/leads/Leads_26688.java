package com.sugarcrm.test.leads;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Leads_26688 extends SugarTest {
	LeadRecord testLead1;
	LeadRecord testLead2;

	public void setup() throws Exception {
		DataSource allLeads = testData.get(testName);
		ArrayList<Record> myLeads = sugar().leads.api.create(allLeads);
		testLead1 = (LeadRecord) myLeads.get(0);
		testLead2 = (LeadRecord) myLeads.get(1);
		sugar().login();
	}

	/**
	 * Lead Convert should complete correctly when user doesn't save Opportunity info during Lead Convert
	 * @throws Exception
	 */
	@Test
	public void Leads_26688_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource leadsInfo = testData.get(testName + "_convert");
		testLead1.navToRecord();
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads		
		VoodooControl convertButton = new VoodooControl("a", "css", ".fld_lead_convert_button.detail a");
		VoodooControl accountName = new VoodooControl("input", "css","div[data-module='Accounts'] "
				+ ".fld_name.edit input");
		VoodooControl associateAccountBtn = new VoodooControl("a", "css","div[data-module='Accounts'] "
				+ ".fld_associate_button.convert-panel-header a");
		VoodooControl opportunityName = new VoodooControl("input", "css","div[data-module='Opportunities']"
				+ " .fld_name.edit input");
		VoodooControl saveButton = new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a");

		// Convert the Lead
		convertButton.click();
		VoodooUtils.waitForReady();
		accountName.set(leadsInfo.get(0).get("accountName"));
		associateAccountBtn.click();
		VoodooUtils.waitForReady();
		opportunityName.set(leadsInfo.get(0).get("opportunityName"));
		VoodooUtils.waitForReady();
		saveButton.click();
		VoodooUtils.waitForReady();

		// Verify the Leads record is converted in record view
		VoodooControl ConvertedBadge = new VoodooControl("span", "css", ".detail.fld_converted span");
		ConvertedBadge.assertEquals("Converted", true);

		// Verify Contacts and Accounts are created, but no Opportunity record is created
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", testLead1.get("lastName"));
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", leadsInfo.get(0).get("accountName"));
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.assertIsEmpty();

		// Go to another unconverted Lead and Convert it
		testLead2.navToRecord();
		sugar().leads.recordView.openPrimaryButtonDropdown();
		convertButton.click();
		VoodooUtils.waitForReady();		
		accountName.set(leadsInfo.get(1).get("accountName"));		
		associateAccountBtn.click();
		VoodooUtils.waitForReady();		
		opportunityName.set(leadsInfo.get(1).get("opportunityName"));
		VoodooUtils.waitForReady();		
		new VoodooControl("a", "css", "#collapseOpportunities button[data-moreless='more']").click();
		new VoodooSelect("a", "css", ".fld_opportunity_type.edit div a").set(leadsInfo.get(0).get("type"));
		VoodooUtils.waitForReady();		
		saveButton.click();
		VoodooUtils.waitForReady();

		// Verify the Leads record is converted in record view
		ConvertedBadge.assertEquals("Converted", true);		

		// Verify Contacts and Accounts are created, but no Opportunity record is created
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", testLead2.get("lastName"));
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", leadsInfo.get(1).get("accountName"));
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}