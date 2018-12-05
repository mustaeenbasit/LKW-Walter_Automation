package com.sugarcrm.test.processauthor;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessEmailTemplatesRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessEmailTemplates_delete extends SugarTest {
	ProcessEmailTemplatesRecord myEmailTemplate;

	public void setup() throws Exception {
		myEmailTemplate = (ProcessEmailTemplatesRecord)sugar().processEmailTemplates.api.create();
		sugar().login();
	}

	@Test
	public void ProcessEmailTemplates_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the email template using the UI.
		myEmailTemplate.delete();

		// Verify the email template was deleted
		assertEquals(VoodooUtils.contains(myEmailTemplate.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
