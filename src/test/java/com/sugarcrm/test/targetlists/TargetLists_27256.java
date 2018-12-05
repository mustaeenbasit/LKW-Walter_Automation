package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_27256 extends SugarTest {	
	FieldSet reportData = new FieldSet();

	public void setup() throws Exception {
		reportData = testData.get(testName).get(0);
		sugar().targetlists.api.create();
		sugar().login();

		// TODO: VOOD-822
		// Create Custom(Summation) Report for Users module
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='summationImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "id", "saveButton");
		VoodooControl userCount = new VoodooControl("tr", "id", "Users_count");

		// Go to Reports -> Create reports -> summation report -> Users
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Users").click();

		// Click Next -> Next
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		userCount.click();
		nextBtnCtrl.click();	

		// Click next to by pass the chart option step
		// TODO: VOOD-822
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(1) input#nextButton").click();

		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.focusDefault();
	}
	/**
	 * Verify that add Users from report in Targetlist
	 * @throws Exception
	 */
	@Test
	public void TargetLists_27256_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Targetlists and open the record.
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);

		// In Users sub panel.Click on action drop down, select "Select from Reports".
		StandardSubpanel usersSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		usersSubpanel.scrollIntoViewIfNeeded(false);
		usersSubpanel.clickOnSelectFromReport();

		// Verify that the created report is automatically show up in the list.
		// TODO: VOOD-1162
		new VoodooControl("tr", "css", ".search-and-select span[data-voodoo-name='name'] div").assertContains(testName,true );

		// Verify that the "Users' reports" is appearing in Filter header.
		// TODO: VOOD-1162
		new VoodooControl("span", "css", ".choice-filter.choice-filter-clickable span").assertContains(reportData.get("filterHeader"), true);

		// Verify that the filter define is like "Module is any of Users"
		// TODO: VOOD-1162
		new VoodooControl("span", "css", ".fld_filter_row_name .select2-chosen").assertContains(reportData.get("module"), true);
		new VoodooControl("span", "css", ".fld_filter_row_operator .select2-chosen").assertContains(reportData.get("isAnyOf"), true);
		new VoodooControl("div", "css", ".select2-search-choice div").assertContains(sugar().users.moduleNamePlural, true);

		// Search for "User Report1" and select it.
		sugar().reports.searchSelect.search(testName);
		sugar().reports.searchSelect.selectRecord(1);

		// Expanding Users subpanel before verification
		usersSubpanel.expandSubpanel(); 
		
		// Verify that the TargetList record view, the User record appears in the sub panel of "Users".
		FieldSet verificationFS =  new FieldSet();
		for(int i = 1; i <= 4; i++) {
			verificationFS.put("userName",reportData.get("record_" + i));
			usersSubpanel.verify(i, verificationFS , true);
			verificationFS.clear();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}