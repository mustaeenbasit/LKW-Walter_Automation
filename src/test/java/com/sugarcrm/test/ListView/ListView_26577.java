package com.sugarcrm.test.ListView;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class ListView_26577 extends SugarTest {
	DataSource leadsData;
	AccountRecord myAccount;
	int leadSize;
	ArrayList<Record> leadsRecord;

	public void setup() throws Exception {
		leadsData = testData.get(testName);
		leadSize = leadsData.size();		
		myAccount = (AccountRecord)sugar.accounts.api.create();
		leadsRecord = sugar.leads.api.create(leadsData);
		sugar.login();
	}

	/**
	 * Related-to widget select value inline edit mode.
	 * @throws Exception 
	 */
	@Test
	public void ListView_26577_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the account created in setup
		myAccount.navToRecord();

		sugar.accounts.recordView.subpanels.get(sugar.documents.moduleNamePlural).hover();
		// Linking the Leads records in the sub-panel of Account.
		StandardSubpanel leadSubpanel = sugar.accounts.recordView.subpanels.get(sugar.leads.moduleNamePlural);
		leadSubpanel.linkExistingRecords(leadsRecord);

		sugar.accounts.recordView.subpanels.get(sugar.documents.moduleNamePlural).hover();
		// Clicking on the more leads... link
		leadSubpanel.showMore();

		// Inline edit on the last record in the subpanel
		leadSubpanel.editRecord(leadSize);

		// Verify cancel button and save button are shown
		leadSubpanel.getControl(String.format("cancelActionRow%02d", leadSize)).assertExists(true);
		leadSubpanel.getControl(String.format("saveActionRow%02d", leadSize)).assertExists(true);

		// TODO: VOOD-503
		new VoodooControl("input", "css", "[name='last_name']").set(testName);
		leadSubpanel.saveAction(leadSize);

		// Asserting the edited data after save
		FieldSet fs = new FieldSet();
		fs.put("lastName", testName);
		leadSubpanel.verify(leadSize,fs,true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}