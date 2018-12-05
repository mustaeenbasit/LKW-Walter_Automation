package com.sugarcrm.test.targetlists;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_28880 extends SugarTest {
	StandardSubpanel targetsSubpanel, leadsSubpanel, contactsSubpanel;
	ArrayList<Record> targetRecords, leadsRecords, contactsRecords;
	FieldSet myData = new FieldSet();
	
	public void setup() throws Exception {
		targetRecords = new ArrayList<Record>();
		leadsRecords = new ArrayList<Record>();
		contactsRecords = new ArrayList<Record>();
		
		// Create 2 records of Targets, Leads and Contacts
		for (int i = 0; i < 2; i++) {
			myData.put("firstName", testName+"_"+i);
			targetRecords.add(sugar.targets.api.create(myData));
			leadsRecords.add(sugar.leads.api.create(myData));
			contactsRecords.add(sugar.contacts.api.create(myData));
		}
		
		// Create TargetList
		sugar.targetlists.api.create();
		sugar.login();
	}

	/**
	 * Total Entries count should auto-update in target list
	 *
	 *@throws Exception
	 */
	@Test
	public void TargetLists_28880_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to record view of 'TargetList' module
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		
		// Targets sub panel
		// Click on action drop-down of Targets sub panel & select Link Existing 2 target Records
		targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetsSubpanel.linkExistingRecords(targetRecords);
		
		// Verify selected targets should be linked to Target sub panel
		Assert.assertTrue("Record count is not equal to 2", targetsSubpanel.countRows() == 2);
		for (int i = 0; i < 2; i++) {
			myData.put("firstName", testName+"_"+(1-i));
			targetsSubpanel.verify((i+1), myData, true);
			myData.clear();
		}
		
		// Verify that count for 'Total Entries' field in detail view of Target list should get updated automatically
		sugar.targetlists.recordView.getDetailField("entryCount").assertEquals("2", true);
		
		// Leads sub panel
		// Click on action drop-down of Leads sub panel & select Link Existing 2 Leads Records
		leadsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecords(leadsRecords);
		
		// Verify selected leads should be linked to Leads sub panel
		Assert.assertTrue("Record count is not equal to 2", leadsSubpanel.countRows() == 2);
		for (int i = 0; i < 2; i++) {
			myData.put("firstName", testName+"_"+(1-i));
			leadsSubpanel.verify((i+1), myData, true);
			myData.clear();
		}
		
		// Verify that count for 'Total Entries' field in detail view of Target list should get updated automatically
		sugar.targetlists.recordView.getDetailField("entryCount").assertEquals("4", true);
		
		// Contacts sub panel
		// Click on action drop-down of Contacts sub panel & select Link Existing 2 Contacts Records
		contactsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecords(contactsRecords);
		
		// Verify selected contacts should be linked to Contacts sub panel
		Assert.assertTrue("Record count is not equal to 2", contactsSubpanel.countRows() == 2);
		for (int i = 0; i < 2; i++) {
			myData.put("firstName", testName+"_"+(1-i));
			contactsSubpanel.verify((i+1), myData, true);
			myData.clear();
		}
		
		// Verify that count for 'Total Entries' field in detail view of Target list should get updated automatically
		sugar.targetlists.recordView.getDetailField("entryCount").assertEquals("6", true);	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
