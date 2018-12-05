package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_27255 extends SugarTest {
	TargetListRecord myTargetList;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myTargetList = (TargetListRecord)sugar().targetlists.api.create();
		sugar().leads.api.create();
		sugar().login();	

		// Create Summation Report based on Leads Module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "img[name='summationImg']").click();
		new VoodooControl("table", "id", "Leads").click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "css", "#filters_div > table:nth-child(5) tbody tr td input[name='Next >']");
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Leads_count").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();
		new VoodooControl("input", "id", "save_report_as").set(customData.get("report_name"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that add Lead from report in Targetlist
	 */
	@Test
	public void TargetLists_27255_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myTargetList.navToRecord();

		// TODO: VOOD-1147
		new VoodooControl("span", "css", "div.filtered.tabbable.tabs-left.layout_Leads a span").click();
		new VoodooControl("span", "css", "div.filtered.tabbable.tabs-left.layout_Leads div.subpanel-header li:nth-child(2) span a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verifying "Lead Report1" is automatically show up in the list. "Leads' reports" is appearing in Filter header.The filter define is like "Module is any of Leads". 
		new VoodooControl("div", "css", "tr:nth-child(1) td:nth-child(2) span div").assertEquals(customData.get("report_name"), true);
		new VoodooControl("span", "css", ".filter-view.search.layout_Reports span.choice-filter-label").assertEquals(customData.get("filter_name"), true);

		// TODO: VOOD-999
		new VoodooControl("span", "css", "div[data-filter='field'] div a span.select2-chosen").assertEquals(customData.get("field"), true);
		new VoodooControl("span", "css", "div[data-filter='operator'] div a span.select2-chosen").assertEquals(customData.get("operator"), true);
		new VoodooControl("div", "css", "div[data-filter='value'] .fld_module div ul li div").assertEquals(customData.get("value"), true);
		new VoodooControl("input", "css", ".search-and-select tbody tr td:nth-of-type(1) input").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify the Lead record appears in the sub panel of "Leads". 
		FieldSet leadsData = new FieldSet();
		leadsData.put("firstName", sugar().leads.getDefaultData().get("firstName"));
		leadsData.put("lastName", sugar().leads.getDefaultData().get("lastName"));
		leadsData.put("phoneWork", sugar().leads.getDefaultData().get("phoneWork"));
		leadsData.put("leadSource", sugar().leads.getDefaultData().get("leadSource"));
		
		// Expanding Leads subpanel before verification
		StandardSubpanel leadsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.expandSubpanel(); 
		sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural).verify(1, leadsData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}