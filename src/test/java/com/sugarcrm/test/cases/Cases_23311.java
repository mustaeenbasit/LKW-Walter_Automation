package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23311 extends SugarTest {
	FieldSet bugFields = new FieldSet();
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().login();

		// Create Cases record and required Account record
		sugar().accounts.api.create();
		myCase = (CaseRecord)sugar().cases.api.create();

		// Make Bugs subpanel visible as it is hidden by default
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/**
	 * Verify that a Bug for a Case is not created when the mandatory
	 * field is left empty when creating a new Bug in the Bugs subpanel.
	 */
	@Test
	public void Cases_23311_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Cases Detailview
		myCase.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Create a new record under the Bugs subpanel
		bugFields.put("name","");
		sugar().cases.recordView.subpanels.get("Bugs").create(bugFields);

		// Assert the empty required subject field throws an error
		new VoodooControl("i", "css", "span.fld_name.edit.error i.fa-exclamation-circle").assertExists(true);

		// Cancel record creation
		sugar().bugs.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
