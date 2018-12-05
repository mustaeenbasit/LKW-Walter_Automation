package com.sugarcrm.test.RevenueLineItems;

import java.text.DecimalFormat;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17777 extends SugarTest {
	DataSource ds;
	public void setup() throws Exception {
		ds = testData.get(testName);

		// update FieldSet values with current date 
		String strDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		DataSource updatedRecord = new DataSource();
		for(FieldSet data : ds){
			data.put("date_closed", strDate);
			updatedRecord.add(data);
		}
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().revLineItems.api.create(updatedRecord);
		sugar().login();

		// TODO: VOOD-444
		// add relation between account and opp record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
		sugar().opportunities.listView.saveRecord(1);
		VoodooUtils.waitForReady();

		// mass update rli record to add assigned user and opp record
		FieldSet massUpdateRecord = new FieldSet();
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		massUpdateRecord.put("Assigned To", sugar().users.getQAUser().get("userName"));
		massUpdateRecord.put("Opportunity Name", sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.massUpdate.performMassUpdate(massUpdateRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify that Pipeline Chart dashlet is populated with RLIs (not Opps)
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17777_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().login(sugar().users.getQAUser());
		VoodooUtils.waitForReady();

		DecimalFormat formatar = new DecimalFormat("#,###");
		String value = null;

		// Verify that Pipeline is generated from revenue line items data but not from Opportunities data.
		// TODO: VOOD-1376
		for (int i = 0; i < ds.size(); i++) {
			new VoodooControl("text", "css", "g.nv-group.nv-series-"+i+" text.nv-label").assertEquals(ds.get(i).get("salesStage"), true);
			value = formatar.format(Double.parseDouble(ds.get(i).get("likelyCase")));
			new VoodooControl("text", "css", "g.nv-group.nv-series-"+i+" text.nv-value").assertContains(value, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}