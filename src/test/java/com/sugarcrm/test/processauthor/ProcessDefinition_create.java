package com.sugarcrm.test.processauthor;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessDefinitionRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessDefinition_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void ProcessDefinition_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ProcessDefinitionRecord myDefinition = (ProcessDefinitionRecord)sugar().processDefinitions.create();
		myDefinition.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
