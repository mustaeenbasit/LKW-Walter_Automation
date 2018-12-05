package com.sugarcrm.test.opportunities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_29029 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that opportunity pipeline chart and its tooltip display same numbers (Likely, Best and Worst figures)
	 * @throws Exception
	 */
	@Test
	public void Opportunities_29029_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String startDate = sdf.format(dt);
		
		// Navigating to Opportunities page
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		FieldSet customFS = testData.get(testName).get(0);
		
		// Set fields, Likely as "40,000", Best as "80,000", Worst as "25,000" and Sales Stage as "Prospecting"
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("likelyCase").set(customFS.get("likelyCase"));
		sugar().opportunities.createDrawer.getEditField("bestCase").set(customFS.get("bestCase"));
		sugar().opportunities.createDrawer.getEditField("worstCase").set(customFS.get("worstCase"));
		sugar().opportunities.createDrawer.getEditField("date_closed").set(startDate);
		sugar().opportunities.createDrawer.save();
		
		// Go to Home dashboard and check Pipeline dashlet
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		
		// TODO: VOOD-1376 & VOOD-1660
		// Verify that The pipeline chart and tooltip should display the same numbers (i.e. the Likely figure "40,000") with the opportunity stage as "Prospecting".
		VoodooControl pipelineChartCtrl = new VoodooControl("text", "css", "[data-content='chart'] svg g.nv-funnelWrap text.nv-value");
		pipelineChartCtrl.assertContains(customFS.get("chartAmount"), true);
		new VoodooControl("text", "css", "div[data-voodoo-name='forecast-pipeline'] .nv-groups text").hover();
		new VoodooControl("div", "css", ".tooltip-inner").assertContains(customFS.get("toltipAmount"), true);
		new VoodooControl("div", "css", ".tooltip-inner").assertContains(customFS.get("toltipStage"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}