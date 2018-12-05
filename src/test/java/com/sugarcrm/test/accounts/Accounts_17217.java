package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17217 extends SugarTest {
	FieldSet leadsData = new FieldSet();
	StandardSubpanel leadSubpanel, contactSubpanel;

	public void setup() throws Exception {
		// Pre-Condition : Account Records exist with relate records
		sugar().accounts.api.create();
		LeadRecord leadRecord = (LeadRecord)sugar().leads.api.create();
		NoteRecord noteRecord = (NoteRecord)sugar().notes.api.create();
		leadsData = sugar().leads.getDefaultData();
		FieldSet fs = new FieldSet();
		fs.put("lastName", leadsData.get("lastName"));
		ContactRecord conRecord = (ContactRecord)sugar().contacts.api.create(fs);
		sugar().login();

		// Relate leads, Contacts, Notes data with account record.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();
		leadSubpanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSubpanel.linkExistingRecord(leadRecord);
		StandardSubpanel notesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.linkExistingRecord(noteRecord);
		contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(conRecord);
	}

	/**
	 * User can search record from all subpanels by name field (first name and last name for person type module )
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17217_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Input data in search box
		sugar().accounts.recordView.setSearchString(leadsData.get("lastName"));

		// Verify records from all subpanels are searched out.
		// Verifying record in leads Subpanel
		leadSubpanel.expandSubpanel();
		// TODO: VOOD-1443
		new VoodooControl("div", "css", ".layout_Leads [data-voodoo-name='full_name'] div").assertContains(leadsData.get("fullName"), true);

		// Verifying record in contact subpanel
		contactSubpanel.expandSubpanel();
		new VoodooControl("div", "css", ".layout_Contacts [data-voodoo-name='full_name'] div").assertContains(leadsData.get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}