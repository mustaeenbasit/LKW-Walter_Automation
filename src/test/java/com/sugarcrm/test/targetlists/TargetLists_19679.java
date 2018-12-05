package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class TargetLists_19679 extends SugarTest{
	DataSource leads;
	TargetListRecord targetList;
	
	public void setup() throws Exception {
		sugar.targetlists.api.deleteAll();
		sugar.leads.api.deleteAll();
		leads = testData.get(testName);
		sugar.leads.api.create(leads);
		targetList = (TargetListRecord) sugar.targetlists.api.create();
		
		sugar.login();
	}

	/**
	 * Targetlist-add leads to a target list by click Add To Target List button on the leads list view page
	 */
	@Test
	public void TargetLists_19679_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-657: Listview needs to provide access to all drop down menus
		VoodooControl addToTargetList = new VoodooControl("a", "css", ".dropdown-menu .list.fld_addtolist_button a");
		
		// TODO: VOOD-528: Adding lib support for the "Add To Target List" menu item and pane
		VoodooSelect targetListDropdown = new VoodooSelect("div", "css", ".fld_prospect_lists_name .required");
		VoodooControl updateButton = new VoodooControl("a", "css", "[name='update_button']");
		sugar.leads.navToListView();
		
		// Selecting all leads and adding all to target list
		sugar.leads.listView.toggleSelectAll();
		sugar.leads.listView.openActionDropdown();
		addToTargetList.click();
		VoodooUtils.waitForReady();
		targetListDropdown.set(targetList.getRecordIdentifier());
		updateButton.click();
		VoodooUtils.waitForReady();
		int size = leads.size();
		
		// Verifying that the lead records have been added to target list
		sugar.alerts.getSuccess().assertEquals("Success "+size+" records were added successfully.", true);
		sugar.alerts.getSuccess().closeAlert(); 
		
		// Navigating to Target Lists module
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		StandardSubpanel leadsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.leads.moduleNamePlural);

		leadsSubpanel.scrollIntoViewIfNeeded(false);
		leadsSubpanel.expandSubpanel();

		// Sorting the leads in ascending order in subpanel
		leadsSubpanel.sortBy("headerFullname", true);
		
		// Verifying that leads are visible in Leads subpanel
		for (int i = 0 ; i < size ; i++){
			String fullName = "Mr. " +leads.get(i).get("firstName") +" "+leads.get(i).get("lastName");
			// TODO: VOOD-1424: Make StandardSubpanel.verify() verify specified value is in correct column.
			leadsSubpanel.getDetailField(i+1, "fullName").assertEquals(fullName, true);
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}