package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23287 extends SugarTest {
	FieldSet caseData, customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		caseData = sugar().cases.defaultData.deepClone();
		caseData.put("relAccountName", null);

		sugar().login();
	}

	/**
	 * Create Case: A case cannot be created when an invalid account is specified.
	 */
	@Test
	public void Cases_23287_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		// Create case
		sugar().cases.listView.create();
		sugar().cases.createDrawer.showMore();
		// Fill in required fields
		sugar().cases.createDrawer.setFields(caseData);

		// Add non-existing account
		sugar().cases.createDrawer.getEditField("relAccountName").click();
		// TODO: VOOD-704 CreateDrawer: select related record with input: no way to close it if there is no result found
		new VoodooControl("input", "css", "div#select2-drop div input").set(customData.get("invalid_acc_name"));
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("li", "css", "#select2-drop ul:nth-child(2) li").assertEquals(customData.get("no_match_found"), true);

		// Click "Search for more"
		new VoodooControl("div", "css", "#select2-drop ul:nth-child(3) li div").click();
		sugar().alerts.waitForLoadingExpiration();
		// Click "Cancel"
		new VoodooControl("a", "css", ".fld_close a").click();
		sugar().cases.createDrawer.save();

		// Verify & close error message when try to save case record with invalid account
		sugar().alerts.getError().assertEquals(customData.get("error_msg"), true);
		sugar().alerts.getAlert().closeAlert();
		sugar().cases.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
