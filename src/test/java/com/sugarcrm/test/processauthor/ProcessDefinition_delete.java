package com.sugarcrm.test.processauthor;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessDefinitionRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessDefinition_delete extends SugarTest {
	ProcessDefinitionRecord myDefinition;

	public void setup() throws Exception {
		myDefinition = (ProcessDefinitionRecord)sugar().processDefinitions.api.create();
		sugar().login();
	}

	@Test
	public void ProcessDefinition_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the definition using the UI.
		myDefinition.delete();

		// Verify the definition was deleted
		assertEquals(VoodooUtils.contains(myDefinition.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
