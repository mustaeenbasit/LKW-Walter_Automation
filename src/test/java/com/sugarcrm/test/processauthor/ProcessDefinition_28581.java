package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessDefinition_28581 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify List of Modules Available for Process Author Definitions
	 * @throws Exception
	 */
	@Test
	public void ProcessDefinition_28581_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource targetModule = testData.get(testName);
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.create();
		sugar().processDefinitions.createDrawer.getEditField("targetModule").click();

		// The list of modules that can generate triggering events, listed in alphabetical order
		// TODO: VOOD-629 - Add support for accessing and manipulating individual components of a VoodooSelect
		for(int i=0; i<targetModule.size(); i++) {

			// Assert that the Module names are displayed in Alphabetical Order
			new VoodooControl("div", "css",".select2-drop-active .select2-result-selectable:nth-child("
					+ (i+1) + ") .select2-result-label").assertEquals(
							targetModule.get(i).get("moduleName"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
