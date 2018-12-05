package com.sugarcrm.test.targetlists;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;

public class TargetLists_18061 extends SugarTest{
	DataSource twoLeads;
	String targetListName;
	TargetListRecord myTargetList;
	
	public void setup() throws Exception {
		twoLeads = testData.get("TargetLists_18061_Leads");
		sugar.login();		
		// Create a target list
		
		myTargetList = (TargetListRecord)sugar.targetlists.api.create();
		targetListName = myTargetList.getRecordIdentifier();
	}

	/**
	 * Search for more targetlist when add records to target lists
	 * @author Eric Yang <eyang@sugarcrm.com>
	 */
	@Test
	public void TargetLists_18061_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create two lead record from a DataSource.
		List<Record> leadRecords = sugar.leads.api.create(twoLeads);
		
		// Nav to leads listview and add two leads to target list
		sugar.leads.navToListView();
		sugar.leads.listView.setSearchString("John");
		sugar.leads.listView.toggleSelectAll();
		sugar.leads.listView.openActionDropdown();
		new VoodooControl("div", "css", ".fld_addtolist_button a").click();
		new VoodooControl("span", "css", ".select2-container.inherit-width a").click();
		new VoodooControl("div", "css", ".select2-result-label").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("span", "css", "#drawers .fld_ProspectLists_select.list input").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("a", "css", "#content .fld_update_button a").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Nav to target list recordview and verify the leads subpanel
		myTargetList.navToRecord();
		sugar.alerts.waitForLoadingExpiration();

		// Assert the related leads in leads sub panel
		StandardSubpanel leadSub = sugar.targetlists.recordView.subpanels.get("Leads");
		leadSub.expandSubpanel();
		leadSub.scrollIntoViewIfNeeded(false);

		for(Record lead : leadRecords)
		{
			// TODO: VOOD-1708 - Need Lib support to verify existence of a Subpanel record with column(s) value per a provided FieldSet
			new VoodooControl("span", "xpath", "//div[contains(@class,'layout_Leads')]//span[contains(@class,'fld_full_name')][contains(.,'"+lead.getRecordIdentifier()+"')]").assertExists(true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}