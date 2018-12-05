package com.sugarcrm.test.targetlists;

import java.util.List;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class TargetLists_18060 extends SugarTest {
	DataSource twoTargets;
	String targetListName;
	TargetListRecord myTargetList;

	public void setup() throws Exception {
		twoTargets = testData.get(testName);
		sugar.login();	
		
		myTargetList = (TargetListRecord) sugar.targetlists.api.create();
		targetListName = myTargetList.getRecordIdentifier();
	}

	/**
	 * Test Case 18060: Be able to add Targets records to targetlist from listview
	 * 
	 */
	@Test
	public void TargetLists_18060_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create two target records from a DataSource.
		List<Record> targetRecords = sugar.targets.api.create(twoTargets);

		// Nav to contacts listview and add two targets to target list
		sugar.targets.navToListView();		
		sugar.targets.listView.toggleSelectAll();
		sugar.targets.listView.openActionDropdown();
		// TODO: VOOD-528
		new VoodooControl("div", "css", ".fld_addtolist_button a").click();
		new VoodooSelect("a","css", ".fld_prospect_lists_name div a").set(targetListName);		
		
		new VoodooControl("a", "css", ".fld_update_button a").click();
		sugar.alerts.waitForLoadingExpiration();

		// Nav to Target List recordview and verify the contacts subpanel
		myTargetList.navToRecord();
				
		// Assert the related targets in Targets subpanel
		// TODO: VOOD-477
		StandardSubpanel targetsSubpanel = sugar.targetlists.recordView.subpanels.get("Prospects");
		targetsSubpanel.expandSubpanel();
		targetsSubpanel.scrollIntoViewIfNeeded(false);

		for (Record target : targetRecords) {
			// TODO: VOOD-1708 - Need Lib support to verify existence of a Subpanel record with column(s) value per a provided FieldSet
			new VoodooControl("span", "xpath", "//div[contains(@class,'layout_Prospects')]//span[contains(@class,'fld_full_name')][contains(.,'"+target.getRecordIdentifier()+"')]").assertExists(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}