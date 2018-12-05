package com.sugarcrm.test.targetlists;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class TargetLists_18058 extends SugarTest {
	DataSource twoContacts;
	String targetListName;
	TargetListRecord myTargetList;

	public void setup() throws Exception {
		twoContacts = testData.get("TargetLists_18058_Contacts");
		sugar.login();
		
		myTargetList = (TargetListRecord) sugar.targetlists.api.create();
		targetListName = myTargetList.getRecordIdentifier();
	}

	/**
	 * Be able to add Contact records to targetlist from listview
	 * 
	 * @author Yuri Zverev <yzverev@sugarcrm.com>
	 */
	@Test
	public void TargetLists_18058_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create two contact records from a DataSource.
		List<Record> contactRecords = sugar.contacts.api.create(twoContacts);

		// Nav to contacts listview and add two contacts to target list
		sugar.contacts.navToListView();		
		sugar.contacts.listView.toggleSelectAll();
		sugar.contacts.listView.openActionDropdown();
		// TODO: VOOD-528
		new VoodooControl("div", "css", ".fld_addtolist_button a").click();
		new VoodooSelect("a","css", ".fld_prospect_lists_name div a").set(targetListName);
		new VoodooControl("a", "css", ".fld_update_button a").click();
		sugar.alerts.waitForLoadingExpiration();

		// Nav to Target List recordview and verify the contacts subpanel
		myTargetList.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
				
		// Assert the related contacts in Contacts subpanel
		sugar.targetlists.recordView.subpanels.get("Contacts").expandSubpanel();
		sugar.targetlists.recordView.subpanels.get("Contacts").scrollIntoViewIfNeeded(false);
		// TODO: VOOD-477

		for (Record contact : contactRecords) {
			// TODO: VOOD-1708 - Need Lib support to verify existence of a Subpanel record with column(s) value per a provided FieldSet
			new VoodooControl("span", "xpath", "//div[contains(@class,'layout_Contacts')]//span[contains(@class,'fld_full_name')][contains(.,'"+contact.getRecordIdentifier()+"')]").assertExists(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}