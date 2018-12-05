package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Cases_23435 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Sub-panels_Verify that a note can be added to a case
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23435_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases Record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Add note record from subpanels
		StandardSubpanel notesSubpanel = sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.addRecord();
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.defaultData.get("subject"));
		sugar().notes.createDrawer.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().notes.createDrawer.save();

		// Verify success message
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().alerts.getSuccess().closeAlert();

		// Verify note record in Notes subpanel
		// TODO: VOOD-1424, Once resolved need to replace below code with verify method
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.getDetailField(1, "subject").assertEquals(sugar().notes.defaultData.get("subject"), true);

		// TODO: VOOD-1380, VOOD-1443
		new VoodooControl("a", "css", ".fld_assigned_user_name.list a").assertEquals(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
