package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_27743 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that opportunity with linked RLI is created successfully after "More" button is clicked in Opportunity create drawer
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27743_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to opportunities module
		sugar().opportunities.navToListView();

		//  Click Create button  
		sugar().opportunities.listView.create();
		// Click "More" button 
		sugar().opportunities.createDrawer.showMore();

		//  Fill out RLI and Opportunity required fields 
		FieldSet oppData = sugar().opportunities.defaultData;
		sugar().opportunities.createDrawer.getEditField("name").set(oppData.get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(oppData.get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(oppData.get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(oppData.get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(oppData.get("rli_likely"));

		// Click Save
		sugar().opportunities.createDrawer.save();

		sugar().opportunities.listView.setSearchString(oppData.get("name"));
		sugar().opportunities.listView.clickRecord(1);

		// Verify that The opportunity is created properly with RLI linked to it
		sugar().opportunities.recordView.getDetailField("name").assertEquals(oppData.get("name"), true);
		sugar().opportunities.recordView.getDetailField("relAccountName").assertEquals(oppData.get("relAccountName"), true);
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();
		// TODO: VOOD-1587
		new VoodooControl("div", "css", ".layout_RevenueLineItems .fld_name div").assertEquals(oppData.get("rli_name"), true);

		// Collapsing the subpanel since the default state of subpanel is "Collapsed"
		rliSubpanel.collapseSubpanel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}