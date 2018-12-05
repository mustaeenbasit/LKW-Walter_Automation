package com.sugarcrm.test.processauthor;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessDefinitionRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessDefinition_update extends SugarTest {
	ProcessDefinitionRecord myDefinition;

	public void setup() throws Exception {
		myDefinition = (ProcessDefinitionRecord)sugar().processDefinitions.api.create();
		sugar().login();
	}

	@Test
	public void ProcessDefinition_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-444 need relationship to update record via UI
		FieldSet newData = new FieldSet();
		newData.put("name", "Notes Process Definition");
		newData.put("targetModule", sugar().notes.moduleNamePlural);

		// Edit the definition using the UI.
		myDefinition.edit(newData);

		// Verify the definition was edited.
		myDefinition.verify(newData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
