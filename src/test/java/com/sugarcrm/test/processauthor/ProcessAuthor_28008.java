package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_28008 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user is able to execute Assign User action
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_28008_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet leadsData = testData.get(testName).get(0);

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();

		// Enable Process Definition
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create Lead with Description = "Assign User"
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(sugar().leads.getDefaultData().get("lastName"));
		sugar().leads.createDrawer.showMore();
		sugar().leads.createDrawer.getEditField("description").set(leadsData.get("description"));
		sugar().leads.createDrawer.save();

		// Verify that Process is getting shown.
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);
		Assert.assertEquals("Total no. of rows is not 1",1, sugar().processes.listView.countRows());

		// Approve the Process through Show Process
		// TODO: VOOD-1706
		sugar().processes.listView.openActionDropdown();
		new VoodooControl("a", "css", ".fld_edit_button a").click();
		VoodooControl approveButtonCtrl = new VoodooControl("a", "css", ".fld_approve_button a");
		approveButtonCtrl.click();
		sugar().alerts.getWarning().confirmAlert();

		// Logout as admin user & Login as qaUser
		sugar().logout();
		sugar().login(sugar().users.qaUser);

		// Create Dashboard having Processes Dashlet.
		// TODO: VOOD-960, VOOD-670
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.getControl("title").set(testName);
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(1, 1);
		new VoodooControl("a", "css", "[data-original-title='Processes'] a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		sugar().home.dashboard.save();

		// Click the Process shown in the dashlet
		new VoodooControl("a", "css", ".dashlet-unordered-list .tab-pane.active a").click();

		// Verify Approve & Reject Button are shown.
		// TODO: VOOD-1706
		approveButtonCtrl.assertVisible(true);
		new VoodooControl("a", "css", ".rowaction.btn-danger").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}