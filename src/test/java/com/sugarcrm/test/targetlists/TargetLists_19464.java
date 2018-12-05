package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19464 extends SugarTest {
	FieldSet targetList = new FieldSet(); 

	public void setup() throws Exception {

		// Create two TargetList records
		// Create first record
		sugar.targetlists.api.create();		

		// Create second record
		targetList.put("targetlistName", testName);
		sugar.targetlists.api.create(targetList);

		targetList.clear();
		targetList = testData.get(testName).get(0);

		sugar.login();

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.toggleSelectAll();
		sugar.targetlists.listView.openActionDropdown();

		// Changing the listType value to "Default" because the Type field remains blank when creating record by API
		// TODO: VOOD-1397 - Need library support for Record View, List View, Preview and mass update option for the Target List module. 
		sugar.targetlists.listView.massUpdate();
		new VoodooSelect("span", "css", ".filter-body:nth-child(2) .mu_attribute .select2-chosen").set(targetList.get("massUpdateListType"));
		new VoodooSelect("span", "css", ".filter-body:nth-child(2) .filter-value .fld_list_type .select2-chosen").set(targetList.get("massUpdateValueDefault"));
		new VoodooControl("a", "css", ".filter-header .pull-right .fld_update_button a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify mass update work correctly in Target Lists list view
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19464_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Performing Mass Update to change the listType to Seed and Assigned User to qaUser
		sugar.targetlists.listView.toggleSelectAll();
		sugar.targetlists.listView.openActionDropdown();
		sugar.targetlists.listView.massUpdate();

		// TODO: VOOD-1397 - Need library support for Record View, List View, Preview and mass update option for the Target List module.
		new VoodooSelect("span", "css", ".filter-body:nth-child(2) .mu_attribute .select2-chosen").set(targetList.get("massUpdateListType"));
		new VoodooSelect("span", "css", ".filter-body:nth-child(2) .filter-value .fld_list_type .select2-chosen").set(targetList.get("massUpdateValueSeed"));
		new VoodooControl("i", "css", ".filter-actions .fa-plus").click();
		new VoodooSelect("span", "css", ".filter-body:nth-child(3) .mu_attribute .select2-chosen").set(targetList.get("massUpdateAssignedTo"));
		new VoodooSelect("span", "css", ".filter-body:nth-child(3) .filter-value .fld_assigned_user_name .select2-chosen").set(sugar.users.getQAUser().get("userName").toString());
		new VoodooControl("a", "css", ".filter-header .pull-right .fld_update_button a").click();
		VoodooUtils.waitForReady();

		// Assert that the listType = "Seed" and Assigned User = "qaUser" after Mass Update has been performed
		for(int i=1;i<=sugar.targetlists.listView.countRows();i++){
			Assert.assertTrue("Not found Seed as listType when it should!!", new VoodooControl("td", "css", ".layout_ProspectLists .flex-list-view-content .single:nth-child("+i+") td:nth-child(3)").getText().trim().equals(targetList.get("massUpdateValueSeed")));
			Assert.assertTrue("Not found Seed as listType when it should!!",new VoodooControl("td", "css", ".layout_ProspectLists .flex-list-view-content .single:nth-child("+i+") td:nth-child(5)").getText().trim().equals(sugar.users.getQAUser().get("firstName").toString()));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}