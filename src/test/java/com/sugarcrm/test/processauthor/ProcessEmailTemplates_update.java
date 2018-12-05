package com.sugarcrm.test.processauthor;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessEmailTemplatesRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessEmailTemplates_update extends SugarTest {
	ProcessEmailTemplatesRecord myEmailTemplate;

	public void setup() throws Exception {
		myEmailTemplate = (ProcessEmailTemplatesRecord)sugar().processEmailTemplates.api.create();
		sugar().login();
	}

	@Test
	public void ProcessEmailTemplates_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-444 need relationship to update record via UI
		FieldSet newData = new FieldSet();
		newData.put("name", "Process Email Template 2");
		newData.put("targetModule", sugar().contacts.moduleNamePlural);

		// Edit the email template using the UI.
		myEmailTemplate.edit(newData);

		// Verify the email template was edited.
		myEmailTemplate.verify(newData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
