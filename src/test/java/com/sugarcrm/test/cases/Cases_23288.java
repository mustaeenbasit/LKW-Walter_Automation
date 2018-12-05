package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23288 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that case is not created when several mandatory fields are left empty
	 * @throws Exception
	 **/
	@Test
	public void Cases_23288_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet caseData = new FieldSet();
		caseData = sugar().cases.getDefaultData();
		caseData.put("name", null);
		caseData.put("relAccountName", null);
		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().cases.createDrawer.showMore();
		sugar().cases.createDrawer.setFields(caseData);
		sugar().cases.createDrawer.save();

		// Verify & close error message when try to save case record without required fields
		sugar().alerts.getError().assertEquals(testData.get(testName).get(0).get("error_msg"), true);
		sugar().alerts.getAlert().closeAlert();
		sugar().cases.createDrawer.cancel();

		// verify case record is not created
		sugar().cases.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
