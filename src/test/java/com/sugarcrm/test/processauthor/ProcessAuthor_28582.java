package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class ProcessAuthor_28582 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the receive message event in the process author
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_28582_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource caseData = testData.get(testName);

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a Case
		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().cases.createDrawer.getEditField("name").set(sugar().cases.getDefaultData().get("name"));
		sugar().cases.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().cases.createDrawer.save();

		// Verify the status field is changed to Assigned
		sugar().cases.listView.getDetailField(1,"status").assertEquals(caseData.get(0).get("status"), true);

		// Navigate to Record View and Edit the status field to closed
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.edit();
		sugar().cases.recordView.getEditField("status").set(caseData.get(1).get("status"));
		sugar().cases.recordView.save();

		// TODO: VOOD-1743
		// Remove line #59 and #60 once VOOD-1743 gets resolved
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Verify the Note Record in Notes Subpanel
		StandardSubpanel notesSubpanel =sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.expandSubpanel();
		Assert.assertTrue("No. of Rows is equals to Zero", notesSubpanel.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}