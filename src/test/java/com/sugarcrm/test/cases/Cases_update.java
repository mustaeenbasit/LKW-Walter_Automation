package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_update extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		// TODO: VOOD-444 Once resolved, case record creation via API
		myCase = (CaseRecord)sugar().cases.create();
	}

	@Test
	public void Cases_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Doh! Edited");
		newData.put("description", "Edited description.");
		newData.put("check_portal_viewable", "true");

		// Edit the case using the UI.
		myCase.edit(newData);

		// Verify the case was edited.
		myCase.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}