package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class TargetLists_19459 extends SugarTest{
	DataSource customData;

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar.targetlists.api.create(customData);
		sugar.login();
	}

	/**
	 * Target List - Verify that sorting column headers works correctly in the "Target Lists" list view
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19459_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.sortBy("headerName", true);
		// Set "Type" field in created records
		// TODO: VOOD-997 Value isn't set in Type field when sugar.targetlists.api.create(DataSource)
		for (int i = 0 ; i < customData.size() ; i++){
			new VoodooControl("body", "css", "body").click(); // To move focus away
			sugar.targetlists.listView.editRecord(i+1);
			sugar.targetlists.listView.getEditField(i+1, "listType").set(customData.get(i).get("listType"));
			sugar.targetlists.listView.saveRecord(i+1);
		}

		// Click "Target List" column title in the "Target List" list view. 
		// TODO: VOOD-845
		new VoodooControl("th", "css", ".sorting_asc.orderByname").click();
		sugar.alerts.waitForLoadingExpiration();
		// Verify The target list is sorted by "Target List" as DESC
		for (int i = customData.size() ; i > 0 ; i--){
			sugar.targetlists.listView.getDetailField(4-i, "targetlistName").assertContains(customData.get(i-1).get("targetlistName"), true);
		} 

		// Click Again
		// TODO: VOOD-845
		new VoodooControl("th", "css", ".sorting_desc.orderByname").click();
		sugar.alerts.waitForLoadingExpiration();
		// Verify The target list is sorted by "Target List" as ASC
		for (int i = 0 ; i < customData.size() ; i++){
			sugar.targetlists.listView.getDetailField(i+1, "targetlistName").assertContains(customData.get(i).get("targetlistName"), true);
		}

		// Click "Type" column title in the "Target List" list view
		// TODO: VOOD-845
		new VoodooControl("th", "css", ".sorting.orderBylist_type").click();
		sugar.alerts.waitForLoadingExpiration();
		// Verify target list is sorted by "Type" as DESC,
		for (int i = customData.size() ; i > 0 ; i--){
			sugar.targetlists.listView.getDetailField(4-i, "listType").assertContains(customData.get(i-1).get("listType"), true);
		}

		// Click Type again
		// TODO: VOOD-845
		new VoodooControl("th", "css", ".sorting_desc.orderBylist_type").click();
		sugar.alerts.waitForLoadingExpiration();
		// Verify target list is sorted by "Type" as ASC,
		for (int i = 0 ; i < customData.size() ; i++){
			sugar.targetlists.listView.getDetailField(i+1, "listType").assertContains(customData.get(i).get("listType"), true);
		}

		// Click "Description" column title in the "Target List" list view
		// TODO: VOOD-845
		new VoodooControl("th", "css", ".sorting.orderBydescription").click();
		sugar.alerts.waitForLoadingExpiration();
		// Verify The target list is sorted by "Description" as DESC
		for (int i = customData.size() ; i > 0 ; i--){
			sugar.targetlists.listView.getDetailField(4-i, "description").assertContains(customData.get(i-1).get("description"), true);
		}

		// Click "Description" column title again
		// TODO: VOOD-845
		new VoodooControl("th", "css", ".sorting_desc.orderBydescription").click();
		sugar.alerts.waitForLoadingExpiration();
		// Verify The target list is sorted by "Description" as ASC
		for (int i = 0 ; i < customData.size() ; i++){
			sugar.targetlists.listView.getDetailField(i+1, "description").assertContains(customData.get(i).get("description"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}