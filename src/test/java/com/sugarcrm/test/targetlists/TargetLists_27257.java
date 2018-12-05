package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_27257 extends SugarTest {
	TargetListRecord targetListData;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		targetListData = (TargetListRecord)sugar().targetlists.api.create();
		sugar().accounts.api.create();
		sugar().login();	

		// Create Report based on Accounts Module
		// TODO: VOOD-822 : Need lib support of reports module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "img[name='summationImg']").click();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "css", "#filters_div > table:nth-child(5) tbody tr td input[name='Next >']");
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Accounts_count").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();
		new VoodooControl("input", "id", "save_report_as").set(customData.get("report_name"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}
	/**
	 * Verify that add Account from report in Targetlist
	 */
	@Test
	public void TargetLists_27257_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		targetListData.navToRecord();

		StandardSubpanel accountsSubpanel = (StandardSubpanel) sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		accountsSubpanel.scrollIntoViewIfNeeded(false);
		accountsSubpanel.clickOnSelectFromReport();

		// Verify "Account Report1" is automatically show up in the list. "Accounts' reports" is appearing in Filter header. The filter define is like "Module is any of Accounts".  
		new VoodooControl("tr", "css", ".search-and-select tbody tr").assertVisible(true);
		new VoodooControl("span", "css", ".filter-view.search.layout_Reports span.choice-filter-label").assertEquals(customData.get("filter_name"), true);

		// TODO: VOOD-999
		new VoodooControl("span", "css", "div[data-filter='field'] div a span.select2-chosen").assertEquals(customData.get("field"), true);
		new VoodooControl("span", "css", "div[data-filter='operator'] div a span.select2-chosen").assertEquals(customData.get("operator"), true);
		new VoodooControl("div", "css", "div[data-filter='value'] .fld_module div ul li div").assertEquals(customData.get("value"), true);
		new VoodooControl("input", "css", ".search-and-select tbody tr td:nth-of-type(1) input").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that Account record appears in the sub panel of targetlist > Accounts 
		FieldSet targetsData = new FieldSet();
		targetsData.put("name", sugar().accounts.getDefaultData().get("name"));
		targetsData.put("workPhone", sugar().accounts.getDefaultData().get("workPhone"));
		
		// Expanding Accounts subpanel before verification
		accountsSubpanel.expandSubpanel(); 
		
		// Verify Account record appears in the Account sub panel 
		sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural).verify(1, targetsData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}