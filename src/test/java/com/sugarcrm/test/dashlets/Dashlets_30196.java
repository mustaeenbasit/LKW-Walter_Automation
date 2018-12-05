package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_30196 extends SugarTest {

	public void setup() throws Exception {
		// Login as admin
		sugar().login();
	}

	/**
	 * Verify that Selected Report and Report title should display as selected in "Saved reports chart" dashlet.
	 * @throws Exception
	 */
	@Test
	public void Dashlets_30196_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Create a 'Saved Reports Chart Dashlet' in My Dashboard by selecting a report "Module used by My direct Reports (Last 30 Days)".
		sugar().home.dashboard.edit();

		// Clicking 'Add row' on Second column
		// TODO: VOOD-591 Dashlets support
		new VoodooControl("a", "css", ".dashlets li:nth-child(2) [data-value='1']").click();
		sugar().home.dashboard.addDashlet(3,1);

		// Scrolling and clicking 'Saved Reports Chart Dashlet' link
		new VoodooControl("a", "css", ".table-striped tr:nth-child(19) a").scrollIntoViewIfNeeded(true);
		VoodooUtils.waitForReady();

		// Set 'Modules Used By My Direct Reports (Last 30 Days)'
		VoodooSelect reportNameDropDown = new VoodooSelect("span", "css", ".edit.fld_saved_report_id span");
		reportNameDropDown.set(customData.get("reportName"));
		VoodooUtils.waitForReady();

		// Clicking on Save button
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save 'My Dashboard' with the new dashlet
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		String requiredDashletCtrl = ".layout_Home:nth-child(2) .sortable:nth-child(3) ";
		
		// Verify that the name of dashlet and report title as selected is same
		new VoodooControl("h4", "css", requiredDashletCtrl + ".dashlet-header h4").assertEquals(customData.get("reportName"), true);
		
		// Clicking the Cog (Configure) Icon
		new VoodooControl("a", "css", requiredDashletCtrl + ".dropdown-toggle").click();
		
		// Clicking 'Edit' option
		new VoodooControl("span", "css", requiredDashletCtrl + ".dropdown-menu .dashlet-toolbar").click();
		VoodooUtils.waitForReady();
		
		// Verify Edit view displays Report name in dashlet title as selected before
		new VoodooControl("span", "css", ".active .edit.fld_label input").assertEquals(customData.get("reportName"), true);
		
		// Verify Edit view displays Report name in Reports drop-down as selected before 
		reportNameDropDown.assertEquals(customData.get("reportName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}