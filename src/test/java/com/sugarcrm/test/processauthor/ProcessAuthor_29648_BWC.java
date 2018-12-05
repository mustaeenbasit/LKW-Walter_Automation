package com.sugarcrm.test.processauthor;

import org.apache.maven.shared.utils.StringUtils;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29648_BWC extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Verify Show Process detail view title format is consistent for BWC module
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29648_BWC_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processData = testData.get(testName).get(0);

		// Importing process Definition file
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a Document Record
		sugar().navbar.navToModule(sugar().documents.moduleNamePlural);
		sugar().navbar.selectMenuItem(sugar().documents, "createDocument" );
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooFileField("input", "id", "filename_file").set("src/test/resources/data/" + testName + ".bpm");
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Processes and show Processes
		// TODO: VOOD-1706
		sugar().processes.navToListView();
		sugar().processes.listView.openRowActionDropdown(1);
		new VoodooControl("a", "css", "span[data-voodoo-name='edit_button'] a").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify the title format is as "Process Definition Name | Activity Name"
		new VoodooControl("h2", "id", "showCaseTitle").assertEquals(processData.get("text"), true);

		// Under the title, there has Approve, Reject, Cancel and other Action buttons such as History etc.
		// Used the CSS selectors with "+" implies that Approve,cancel etc buttons are immediate siblings
		// TODO: VOOD-1706
		String css = "#showCaseTitle + br" + StringUtils.repeat("+input", 9) + "+table";

		new VoodooControl("input", "css", css + " #ApproveBtn").assertVisible(true);
		new VoodooControl("input", "css", css + " #RejectBtn").assertVisible(true);
		new VoodooControl("input", "css", css + " [name='Cancel']").assertVisible(true);
		new VoodooControl("input", "css", css + " [title='History']").assertVisible(true);
		new VoodooControl("input", "css", css + " [title='Status']").assertVisible(true);
		new VoodooControl("input", "css", css + " [title='Show Notes']").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
