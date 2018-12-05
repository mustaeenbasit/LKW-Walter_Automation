package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
@Features(revenueLineItem = false)
public class Opportunities_17018 extends SugarTest {
	DataSource salesStageDS;
	AccountRecord myAcc;
	VoodooSelect salesStageEditList;

	public void setup() throws Exception {
		salesStageDS = testData.get(testName);

		// TODO: VOOD-1359
		salesStageEditList = new VoodooSelect("span", "css", ".fld_sales_stage.edit");
		// 1 Account and 2 Opportunities
		myAcc = (AccountRecord)sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// Link same account with above Opportunity, and sales stage 
		sugar().opportunities.navToListView();
		for (int i = 0; i < salesStageDS.size(); i++) {
			sugar().opportunities.listView.editRecord(i+1);
			sugar().opportunities.listView.getEditField(i+1, "name").set(salesStageDS.get(i).get("name"));
			sugar().opportunities.listView.getEditField(i+1, "relAccountName").set(myAcc.getRecordIdentifier());
			salesStageEditList.set(salesStageDS.get(i).get("sales_stage"));
			sugar().opportunities.listView.saveRecord(i+1);
		}
	}

	/**
	 * Verify auto duplicate check during the creation of an Opportunity
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17018_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl name = sugar().opportunities.createDrawer.getEditField("name");
		sugar().opportunities.listView.create();
		name.set(salesStageDS.get(1).get("name").substring(0, 1));

		sugar().opportunities.createDrawer.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
		sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.save();

		// Verify duplicate panel
		sugar().opportunities.createDrawer.getControl("duplicateCount").assertEquals(testData.get("env_dupe_assertion").get(0).get("dupe_check"), true);
		sugar().opportunities.createDrawer.getControl("duplicateHeaderRow").assertExists(true);

		// Verify record either by click on select or preview
		// Verify match with the "Starts with" letters AND Sales status/Sales Stage NOT equal to "Closed"
		sugar().opportunities.createDrawer.selectAndEditDuplicate(1);
		name.assertEquals(salesStageDS.get(1).get("name"), true);
		new VoodooSelect("span", "css", ".fld_sales_stage.edit a").assertEquals(salesStageDS.get(1).get("sales_stage"), true);
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}