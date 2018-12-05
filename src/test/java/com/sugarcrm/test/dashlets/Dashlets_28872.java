package com.sugarcrm.test.dashlets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Dashlets_28872 extends SugarTest {
	FieldSet customFS;
	VoodooControl pipelineToolCtrl, editCtrl, inputCtrl, saveBtnCtrl;
	
	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		c.setTime(dt);
		
		// Get second Quarter date
		c.add(Calendar.MONTH, 3);
		dt = c.getTime();
		String closeDate = sdf.format(dt);
		sugar.accounts.api.create();
		sugar.login();
		
		// Navigating to Opportunities page
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.create();
		FieldSet customFS = testData.get(testName).get(0);
		
		// Create opportunity
		sugar.opportunities.createDrawer.getEditField("name").set(testName);
		sugar.opportunities.createDrawer.getEditField("relAccountName").set(sugar.accounts.getDefaultData().get("name"));
		sugar.opportunities.createDrawer.getEditField("likelyCase").set(customFS.get("likelyCase"));
		sugar.opportunities.createDrawer.getEditField("date_closed").set(closeDate);
		sugar.opportunities.createDrawer.save();
	}

	/**
	 * Verify that Pipeline should show the graph based on the dropdown list selection
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28872_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Dashboard
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		
		// TODO: VOOD-1376 - Need library support for Pipeline Dashlet on Home Dashboard
		// Change the name of the pipeline to 'My_Pipeline_Tested'
		pipelineToolCtrl = new VoodooControl("i", "css", ".dashlets.row-fluid li.span8 ul li:nth-child(1) ul.dashlet-cell.rows.row-fluid .fa.fa-cog");
		pipelineToolCtrl.click();
		editCtrl = new VoodooControl("a", "css", ".span8 .dashlet-container [data-dashletaction='editClicked']");
		editCtrl.waitForVisible();
		editCtrl.click();
		VoodooUtils.waitForReady();
		
		// Set New name 'My_Pipeline_Tested'
		inputCtrl = new VoodooControl("input", "css", ".edit.fld_label input");
		inputCtrl.set(customFS.get("new_pipeline"));
		saveBtnCtrl = new VoodooControl("a", "css", ".span8 div div:nth-child(1) span.detail.fld_save_button a");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that pipeline should not show the graph based on the drop down list selection
		VoodooControl pipelineChartCtrl = new VoodooControl("text", "css", "[data-content='chart'] svg g.nv-funnelWrap text.nv-value");
		pipelineChartCtrl.assertExists(false);
		
		// TODO: VOOD-1376 - Need library support for Pipeline Dashlet on Home Dashboard
		VoodooControl dropDownCtrl = new VoodooControl("span", "css", "[data-voodoo-name='forecast-pipeline'] .dashlet-options .select2-arrow");

		// Open the pipeline dashlet's time periods dropdown
		dropDownCtrl.click();
		new VoodooSelect("div", "css", "#select2-drop .select2-results li:nth-child(2) div").click();
		VoodooUtils.waitForReady();
		
		// Verify that pipeline should show the graph based on the drop down list selection
		pipelineChartCtrl.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}