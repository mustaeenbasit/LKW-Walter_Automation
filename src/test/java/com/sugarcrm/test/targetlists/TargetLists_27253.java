package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_27253 extends SugarTest {
	TargetListRecord myTL;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myTL = (TargetListRecord)sugar().targetlists.api.create();
		sugar().targets.api.create();
		sugar().login();	

		// Create Report based on Targets Module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "img[name='summationImg']").click();
		new VoodooControl("table", "id", "Targets").click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Prospects_count").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();
		new VoodooControl("input", "id", "save_report_as").set(customData.get("report_name"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}
	/**
	 * Verify that add Target from report in Targetlist
	 */
	@Test
	public void TargetLists_27253_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myTL.navToRecord();

		// TODO: VOOD-1147
		new VoodooControl("a", "css", ".layout_Prospects .btn.dropdown-toggle").click();
		new VoodooControl("li", "css", ".layout_Prospects span.actions.btn-group.pull-right ul "
				+ "li:nth-of-type(2)").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify "Target Report1" is automatically show up in the list. "Targets' reports" is appearing
		// in Filter header.  The filter define is like "Module is any of Targets".  
		new VoodooControl("tr", "css", ".search-and-select tbody tr").assertVisible(true);
		new VoodooControl("span", "css", ".filter-view.search.layout_Reports span.choice-filter-label")
			.assertEquals(customData.get("filter_name"), true);

		// TODO: VOOD-999
		new VoodooControl("span", "css", "div[data-filter='field'] div a span.select2-chosen").
			assertEquals(customData.get("field"), true);
		new VoodooControl("span", "css", "div[data-filter='operator'] div a span.select2-chosen").
			assertEquals(customData.get("operator"), true);
		new VoodooControl("div", "css", "div[data-filter='value'] .fld_module div ul li div").
			assertEquals(customData.get("value"), true);
		new VoodooControl("input", "css", ".search-and-select tbody tr td:nth-of-type(1) input").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify Target record appears in the sub panel of "Targets" 
		FieldSet targetsData = new FieldSet();
		targetsData.put("firstName", sugar().targets.getDefaultData().get("firstName"));
		targetsData.put("lastName", sugar().targets.getDefaultData().get("lastName"));
		targetsData.put("title", sugar().targets.getDefaultData().get("title"));
		targetsData.put("phoneWork", sugar().targets.getDefaultData().get("phoneWork"));
		
		// Expanding Targets subpanel before verification
		StandardSubpanel targetsSubpanel = sugar().targetlists.recordView.subpanels.get("Prospects");
		targetsSubpanel.expandSubpanel(); 
		sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural).
			verify(1, targetsData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}