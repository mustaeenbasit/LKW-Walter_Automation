package com.sugarcrm.test.processauthor;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessBusinessRulesRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessBusinessRules_update extends SugarTest {
	ProcessBusinessRulesRecord myBusiness;

	public void setup() throws Exception {
		myBusiness = (ProcessBusinessRulesRecord)sugar().processBusinessRules.api.create();
		sugar().login();
	}

	@Test
	public void ProcessBusinessRules_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-444 need relationship to update record via UI
		FieldSet newData = new FieldSet();
		newData.put("name", "Call Process Definition");
		newData.put("targetModule", sugar().calls.moduleNamePlural);
		newData.put("businessRuleType", sugar().processBusinessRules.getDefaultData().get("businessRuleType"));

		// Edit the business rule using the UI.
		myBusiness.edit(newData);

		// Verify the business rule was edited.
		myBusiness.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
