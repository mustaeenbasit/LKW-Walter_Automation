package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class Cases_17087 extends SugarTest {
	FieldSet customCaseData;

	public void setup() throws Exception {
		customCaseData = testData.get(testName).get(0);
		sugar().cases.api.create();
		sugar().cases.api.create(customCaseData);
		sugar().login();
	}

	/**
	 * Verify user can delete a record from record level action drop down on list view
	 * @throws Exception
	 */
	@Test
	public void Cases_17087_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();

		// Delete 1st record (i.e custom case record not API record)
		sugar().cases.listView.deleteRecord(1);
		sugar().alerts.getWarning().confirmAlert();
		sugar().alerts.waitForLoadingExpiration();

		// Verify only 1 record is in list (i.e API record)
		sugar().cases.listView.assertEquals(customCaseData.get("name"), false);
		sugar().cases.listView.verifyField(1, "name", sugar().cases.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
