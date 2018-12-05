package com.sugarcrm.test.processauthor;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class ProcessAuthor_30158 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Import a Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * Verify "Likely/Best/Worst" fields are not missing in Change/Add related fields of RLI
	 *
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30158_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		// Navigate to Imported Process Definition design page
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("design01").click();
		VoodooUtils.waitForReady();

		// Right click -> Action type -> Setting
		// TODO: VOOD-1369 and VOOD-1539
		new VoodooControl("div", "css", "#jcore_designer .adam-activity-task").rightClick();
		new VoodooControl("a", "css", "#jcore_designer .adam-menu .adam-list li:nth-child(1) a").click();
		VoodooUtils.waitForReady();
		// Go to settings of "Add related record", and select Revenue Line Items.
		new VoodooControl("select", "id", "act_field_module").set(fs.get("moduleName"));
		VoodooUtils.waitForReady();

		// Verify that Likely/Best/Worst field should be displayed in the RLI module
		new VoodooControl("label", "css", "div.adam-panel-body > div:nth-child(2) > div > div:nth-child(3) > label").assertContains(fs.get("bestField"), true);
		new VoodooControl("label", "css", "div.adam-panel-body > div:nth-child(2) > div > div:nth-child(11) > label").assertContains(fs.get("likelyField"), true);
		new VoodooControl("label", "css", "div.adam-panel-body > div:nth-child(2) > div > div:nth-child(37) > label").assertContains(fs.get("worstField"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}