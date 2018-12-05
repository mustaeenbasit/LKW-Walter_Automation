package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21553 extends SugarTest {
	DataSource oppDS = new DataSource();

	public void setup() throws Exception {
		// Initialize app and data
		sugar().admin.api.switchToOpportunitiesView();
		sugar().accounts.api.create();
		oppDS = testData.get(testName);

		sugar().opportunities.api.create(oppDS);
		sugar().login();
		
		// Nav to opportunities module to mass update all opportunities
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.showMore();
		sugar().opportunities.listView.toggleSelectAll();
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.massUpdate();
		
		// Update Related Account
		// TODO: VOOD-2162 - Initialize mass update where applicable  sugar().opportunities.massupdate does not work
		// TODO: VOOD-1003 - Lib support needed for mass update controls on list view
		new VoodooSelect("div", "css", ".filter-field .select2-container").set(oppDS.get(1).get("relAccountName"));
		new VoodooSelect("div", "css", ".filter-value .select2-container").set(oppDS.get(0).get("relAccountName"));
		
		// Add another mass update row to update "Likely" field
		new VoodooSelect("img", "css", "[data-original-title='Add Field'] .fa.fa-plus").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("div", "css", ".extend.list .filter-body:nth-child(3) .filter-field .select2-container").set(oppDS.get(1).get("likelyCase"));
		new VoodooControl("input", "css", ".extend.list .filter-body:nth-child(3) .filter-value input").set(oppDS.get(0).get("likelyCase"));
		
		// Update all opportunities
		new VoodooControl("a", "css", ".massupdate.fld_update_button a").click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 *  Display basic record: Verify that the cumulative opportunity amounts for the basic chart is consistent with 
	 *  cumulative of actual opportunity records
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21553_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Modify "Opp by lead source" report to display "Opp Amount" column
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Use basic search and search for report
		sugar().reports.listView.getControl("basicSearchLink").click();
		String reportName = oppDS.get(3).get("likelyCase");
		// TODO: VOOD-822 - need lib support of reports module
		new VoodooControl("input", "css", "input[name='name_basic']").set(reportName);
		sugar().reports.listView.getControl("searchButton").click();
		VoodooUtils.waitForReady();
		
		// Open Report in Edit mode
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1408 - sugar.reports.listView.clickRecord(1); not working
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(3) a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		
		// Report edit view > Press Next twice to nav to Display Summaries page
		// TODO: VOOD-822 - need lib support of reports module
		VoodooControl nextButton = new VoodooControl("input", "id", "nextBtn");
		nextButton.click();
		nextButton.click();
		new VoodooControl("a", "css", "#display_summaries_row_2 td:nth-child(4) img").click();
		// Add summation Amount field
		new VoodooControl("tr", "css", "#module_fields_panel .yui-dt-data tr:nth-child(21)").scrollIntoView();
		new VoodooControl("tr", "css", "#module_fields_panel .yui-dt-data tr:nth-child(21)").click();
		// Save the report
		new VoodooControl("input", "css", "input[name='Save']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Go to Home -> My Dashboard -> Edit
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.edit();
		
		// Add a Dashlet
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4, 1);
		
		// Add a Dashlet -> Select "Saved Reports Chart Dashlet" tab in toggle drawer
		// TODO: VOOD-960
		String dashletName = oppDS.get(4).get("likelyCase");
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashletName);
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady(); 
		new VoodooSelect("div", "css", ".fld_saved_report_id .select2-container").set(reportName);
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Verify tooltip value of each bar in bar chart dashlet 
		String expectedRoundedSum = oppDS.get(2).get("likelyCase");
		new VoodooControl("input", "css",".dashlet-row.ui-sortable li:nth-child(4)").scrollIntoViewIfNeeded(false);
		for (int i = 0; i < oppDS.size()/2; i++) {
			// Hover over bar to trigger tooltip
			new VoodooControl("input", "css",".dashlet-row.ui-sortable li:nth-child(4) .nv-series-" + i + " g:nth-child(" + (i + 1) + ")").hover();
			VoodooUtils.waitForReady();
			
			// Verify Sum value
			new VoodooControl("div", "css", ".tooltip-inner").assertEquals(oppDS.get(i).get("leadSource") + "\n" + expectedRoundedSum, true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}