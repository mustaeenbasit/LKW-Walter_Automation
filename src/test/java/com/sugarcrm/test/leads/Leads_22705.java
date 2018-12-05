package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22705 extends SugarTest {
	DataSource leadData = new DataSource();
	VoodooControl studioLinkCtrl,leadsStudioCtrl,layoutCtrl;

	public void setup() throws Exception {
		leadData = testData.get(testName);

		// TODO: VOOD-1509,1510
		studioLinkCtrl = sugar().admin.adminTools.getControl("studio");
		leadsStudioCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		layoutCtrl= new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl searchViewCtrl=new VoodooControl("td", "id", "searchBtn");
		VoodooControl searchCtrl= new VoodooControl("a", "css", ".studiolink");
		VoodooControl saveAndDeploy = new VoodooControl("input", "css", ".list-editor #savebtn");
		sugar().leads.api.create(leadData);
		sugar().login();

		// Admin Tools -> Studio -> Leads -> Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioLinkCtrl.click();
		VoodooUtils.waitForReady();
		leadsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		searchViewCtrl.click();
		VoodooUtils.waitForReady();
		searchCtrl.click();
		VoodooUtils.waitForReady();

		// Adding "Referred By" field from Hidden to Default List in Search View
		// TODO: VOOD-1509,1510
		VoodooControl addToDefaultList = new VoodooControl("td", "css", "#editor-content #Default");
		VoodooControl referredByFieldCtrl = new VoodooControl("li", "css", "[data-name='refered_by']");
		referredByFieldCtrl.dragNDrop(addToDefaultList);
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to Leads RecordView Layout
		// TODO: VOOD-1506
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		leadsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Inserting a new row in the Record View layout
		VoodooControl insertNewRow = new VoodooControl("div", "css", "#toolbox .le_row");
		VoodooControl toPanel = new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel");
		insertNewRow.dragNDrop(toPanel);

		// Moving ReferredBy field to the RecordView layout
		VoodooControl referredByField = new VoodooControl("div", "css", "#toolbox div[data-name='refered_by']");
		VoodooControl filler = toPanel.getChildElement("div", "css", ".le_row div:nth-child(1).le_field.special");
		referredByField.dragNDrop(filler);
		new VoodooControl("input", "css", "#layoutEditorButtons #publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to Leads and Set the Value in ReferredBy Field
		// TODO: VOOD-1489
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		VoodooControl referredBy = new VoodooControl("input", "css", ".fld_refered_by.edit input");
		for(int i = 1; i >= 0; i--) {
			sugar().leads.recordView.edit();
			referredBy.set(leadData.get(i).get("lastName"));
			sugar().leads.recordView.save();
			if(i == 1)
				sugar().leads.recordView.gotoNextRecord();
		}
	}

	/**
	 * Search Leads_Verify that Referred By field can be searched by creating new filter under leads module.
	 * @throws Exception
	 */
	@Test 
	public void Leads_22705_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filterData = testData.get(testName+"_filterData").get(0);

		// Create custom filter
		// TODO: VOOD-1462
		sugar().leads.navToListView();
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();
		new VoodooSelect("span", "css", ".search-filter .filter-definition-container  span.select2-chosen").set(filterData.get("filterField"));
		new VoodooSelect("div", "css",".fld_filter_row_operator div").set(filterData.get("operator"));
		new VoodooControl("input", "css", ".fld_refered_by input").set(leadData.get(0).get("lastName"));
		sugar().leads.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().leads.listView.filterCreate.save();

		// Verify that only one lead having referredBy as set in filter is displayed.
		// TODO: VOOD-1489
		Assert.assertTrue("Total no. of Rows not equal to 1", sugar().leads.listView.countRows() == 1);
		sugar().leads.listView.clickRecord(1);
		new VoodooControl("span", "css", ".fld_refered_by.detail").assertEquals(leadData.get(0).get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}