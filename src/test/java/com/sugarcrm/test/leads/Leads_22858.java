package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22858 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * 22858 Verify that error when not giving value in the required field when create a new lead record
	 * @throws Exception
	 */
	@Test
	public void Leads_22858_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet errorMessage = testData.get(testName).get(0);

		// Navigate to Leads module and click on create button
		sugar().leads.navToListView();
		sugar().leads.listView.create();

		// Do not fill up any field,click on "Save" button
		sugar().leads.createDrawer.save();

		// TODO VOOD-664 native methods should be used here
		sugar().alerts.getError().assertContains(errorMessage.get("validation_message"), true);
		new VoodooControl("span", "css", "span.error[data-name='last_name']").assertVisible(true);
		new VoodooControl("span", "css", "span.error[data-name='last_name'] span.error-tooltip").assertVisible(true);
		sugar().leads.createDrawer.getEditField("lastName").assertAttribute("class", "required");

		// Close error message
		sugar().alerts.getError().closeAlert();

		// Cancel Lead Creation
		sugar().leads.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}