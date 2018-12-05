package com.sugarcrm.test.targetlists;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;


public class TargetLists_18057 extends SugarTest{
	DataSource twoAccounts;
	String targetListName;
	TargetListRecord myTargetList;
	
	public void setup() throws Exception {

		twoAccounts = testData.get("TargetLists_18057_Accounts");
		sugar.login();
		// Create a target list
		
		myTargetList = (TargetListRecord)sugar.targetlists.api.create();
		targetListName = myTargetList.getRecordIdentifier();
	}

	/**
	 * Be able to add Account records to targetlist from lisview
	 * @author Eric Yang <eyang@sugarcrm.com>
	 */
	@Test
	public void TargetLists_18057_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create two account records from a DataSource.
		List<Record> accountRecords = sugar.accounts.api.create(twoAccounts);
				
		// Nav to accounts listview and add two accounts to target list
		sugar.accounts.navToListView();
		sugar.accounts.listView.setSearchString("TList_18057");
		sugar.accounts.listView.toggleSelectAll();
		sugar.accounts.listView.openActionDropdown();
		// TODO: VOOD-528
		new VoodooControl("div", "css", ".fld_addtolist_button a").click();
		new VoodooSelect("a","css", ".fld_prospect_lists_name div a").set(targetListName);
		new VoodooControl("a", "css", ".fld_update_button a").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Nav to target list recordview and verify the accounts subpanel
		myTargetList.navToRecord();
		sugar.alerts.waitForLoadingExpiration();

		// Assert the related accounts in Accounts sub panel
		StandardSubpanel accSub = sugar.targetlists.recordView.subpanels.get("Accounts");
		accSub.expandSubpanel();
		accSub.scrollIntoViewIfNeeded(false);

		for(Record account : accountRecords)
		{
			// TODO: VOOD-1708 - Need Lib support to verify existence of a Subpanel record with column(s) value per a provided FieldSet
			new VoodooControl("span", "xpath", "//div[contains(@class,'layout_Accounts')]//span[contains(@class,'fld_name')][contains(.,'"+account.getRecordIdentifier()+"')]").assertExists(true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}
