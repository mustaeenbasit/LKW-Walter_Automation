package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 *@author Ashish Raina <araina@sugarcrm.com>
 **/
public class Cases_23291 extends SugarTest{
	public void setup() throws Exception {
		DataSource casesDS = testData.get(testName);
		sugar().cases.api.create(casesDS);
		sugar().login();
	}

	/**
	 * Verify that case records can be edited when using "Update" function in "Mass Update" panel.
	 * @throws Exception
	 **/
	@Test
	public void Cases_23291_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select case records and open Mass update
		sugar().cases.navToListView();
		sugar().cases.listView.getControl("selectAllCheckbox").click();
		sugar().cases.listView.openActionDropdown();
		sugar().cases.listView.massUpdate();

		// Mass Update 'status' field for the selected cases
		FieldSet massUpdFS = new FieldSet();
		DataSource casesDSUpd = testData.get(testName + "_1");
		massUpdFS.put(casesDSUpd.get(0).get("upd_field"), casesDSUpd.get(0).get("upd_value"));

		// Press update button and commit the update
		sugar().cases.massUpdate.performMassUpdate(massUpdFS);
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify that the records are correctly updated in List view
		sugar().cases.listView.verifyField(1,"status", casesDSUpd.get(0).get("upd_value"));
		sugar().cases.listView.verifyField(2,"status", casesDSUpd.get(0).get("upd_value"));

		// Verify that the records are correctly updated in Record View
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.getDetailField("status").assertEquals(casesDSUpd.get(0).get("upd_value"), true);
		sugar().cases.recordView.gotoNextRecord();
		sugar().cases.recordView.getDetailField("status").assertEquals(casesDSUpd.get(0).get("upd_value"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
