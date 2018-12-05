package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class RecordsModuleTests extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void apiCreateFromFieldSetTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreateFromFieldSetTest()...");

		FieldSet recordData = new FieldSet();
		recordData.put("name", testName + " test account");

		AccountRecord myAccount = (AccountRecord)sugar().accounts.api.create(recordData);
		myAccount.verify();
		assertFalse(myAccount.getGuid().isEmpty());

		VoodooUtils.voodoo.log.info("apiCreateFromFieldSetTest() complete.");
	}

	@Test
	public void apiCreateFromDataSourceTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreateFromDataSourceTest()...");

		DataSource recordData = new DataSource();
		FieldSet singleRecord = new FieldSet();
		singleRecord.put("name", testName + " test account 1");
		recordData.add(singleRecord);
		singleRecord.put("name", testName + " test account 2");
		recordData.add(singleRecord);
		assertTrue("recordData size = " + recordData.size() + ", expected 2.", recordData.size() == 2);

		ArrayList<Record> myAccounts = sugar().accounts.api.create(recordData);
		assertTrue("myAccounts size = " + myAccounts.size() + ", expected 2.", myAccounts.size() == 2);
		for(Record record : myAccounts) {
			record.verify();
			assertFalse(record.getGuid().isEmpty());
		}

		VoodooUtils.voodoo.log.info("apiCreateFromDataSourceTest() complete.");
	}

	@Test
	public void createKBRecordBadStatus() throws Exception {
		VoodooUtils.voodoo.log.info("Running createKBRecordBadStatus()...");

		FieldSet kbRecord1 = new FieldSet();
		kbRecord1.put("name", "KnowledgeBase Draft");
		kbRecord1.put("status", "Draft");
		sugar().knowledgeBase.api.create(kbRecord1);

		FieldSet kbRecord2 = new FieldSet();
		kbRecord2.put("name", "KnowledgeBase In Review");
		kbRecord2.put("status", "In review");
		sugar().knowledgeBase.api.create(kbRecord2);

		// TODO: VOOD-1965 - createKBRecordBadStatus self test - Failed due to HTTP error code: 422 - Once resolved commented code lines will work and verification of records update accordingly (use this backgroundcolor rgba(85, 85, 85, 1) for bad status)
		//FieldSet kbRecord3 = new FieldSet();
		//kbRecord3.put("name", "KnowledgeBase XyZ");
		//kbRecord3.put("status", "XyZ");
		//sugar().knowledgeBase.api.create(kbRecord3);

		FieldSet kbRecord4 = new FieldSet();
		kbRecord4.put("name", "KnowledgeBase expired");
		kbRecord4.put("status", "expired");
		sugar().knowledgeBase.api.create(kbRecord4);

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertCssAttribute("background-color", "rgba(85, 85, 85, 1)", true);
		sugar().knowledgeBase.listView.getDetailField(2, "status").assertCssAttribute("background-color", "rgba(253, 248, 238, 1)", true);
		sugar().knowledgeBase.listView.getDetailField(3, "status").assertCssAttribute("background-color", "rgba(0, 0, 0, 0)", true);

		VoodooUtils.voodoo.log.info("createKBRecordBadStatus() complete.");
	}

	public void cleanup() throws Exception {}
}