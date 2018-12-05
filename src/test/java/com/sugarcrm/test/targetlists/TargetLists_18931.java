package com.sugarcrm.test.targetlists;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TargetListRecord;

public class TargetLists_18931 extends SugarTest{
	DataSource twoTargetLists = new DataSource();
	TargetListRecord testlist1, testlist2;
	FieldSet filterData = new FieldSet();
	
	public void setup() throws Exception {
		filterData = testData.get(testName + "_filter").get(0);
		twoTargetLists = testData.get(testName);
		
		ArrayList<Record> myTargetlists = sugar.targetlists.api.create(twoTargetLists);
		testlist1 = (TargetListRecord) myTargetlists.get(0);
		testlist2 = (TargetListRecord) myTargetlists.get(1);
		sugar.login();	
		
		// TODO: VOOD-997 - Value isn't set in Type field when sugar.targetlists.api.create(DataSource)
		// Have to manually setup Type fields
		testlist1.navToRecord();		
		sugar.targetlists.recordView.edit();
		VoodooSelect listType = new VoodooSelect("a", "css", ".fld_list_type a");
		listType.set(testlist1.get("listType"));
		sugar.targetlists.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
		
		testlist2.navToRecord();
		sugar.targetlists.recordView.edit();
		listType.set(testlist2.get("listType"));
		sugar.targetlists.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
	}
	
	/**
	 * Verify that the correct filter is generated when create a new filter in Targetlist
	 * 
	 */
	@Test
	public void TargetLists_18931_execute() throws Exception {
		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open filter and create a new filter for Type is any of "Default"
		sugar.targetlists.navToListView();		
		sugar.targetlists.listView.openFilterDropdown();
		sugar.targetlists.listView.selectFilterCreateNew();
		sugar().targetlists.listView.filterCreate.setFilterFields("listType",filterData.get("filterFor"),filterData.get("operator"),testlist1.get("listType"),1);
		sugar().targetlists.listView.filterCreate.getControl("filterName").set(filterData.get("filterName"));
		VoodooUtils.waitForReady();
		sugar().targetlists.listView.filterCreate.save();

		// Verify the result list contains correct TargetList record that has Type = Default
		sugar.targetlists.listView.assertContains(testlist1.get("listType"), true);
		sugar.targetlists.listView.assertContains(testlist1.get("targetlistName"), true);
		sugar.targetlists.listView.assertContains(testlist2.get("targetlistName"), false);

		// TODO: VOOD-1580
		new VoodooControl("span", "css", "[data-voodoo-name='ProspectLists'] .choice-filter-label").click();
		VoodooUtils.waitForReady();
		sugar().targetlists.listView.filterCreate.delete();
		VoodooUtils.waitForReady();
		sugar.alerts.confirmAllAlerts();
		
		// Verify the filter is deleted
		// TODO: VOOD-1580
		sugar.targetlists.listView.openFilterDropdown();
		new VoodooControl("li", "css", "div.search-filter-dropdown ul.select2-results[role='listbox'] li").assertContains(filterData.get("filterName"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}