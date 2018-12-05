package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26701 extends SugarTest {

	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		FieldSet fs =testData.get(testName).get(0);
		sugar().login();
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);

		// Select a Note & Tasks module in subpanel
		StandardSubpanel notesSubpanel = sugar().revLineItems.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		StandardSubpanel tasksSubpanel = sugar().revLineItems.recordView.subpanels.get(sugar().tasks.moduleNamePlural);

		// Click + icon to add notes record.
		notesSubpanel.addRecord();

		// Enter data and click "Save and create new" option
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.getDefaultData().get("subject"));
		sugar().notes.createDrawer.save();

		// Click + icon to add tasks record with status as Completed
		tasksSubpanel.addRecord();

		// Enter data and click "Save and create new" option
		sugar().tasks.createDrawer.getEditField("subject").set(sugar().tasks.getDefaultData().get("subject"));
		sugar().tasks.createDrawer.getEditField("status").set(fs.get("status"));
		sugar().tasks.createDrawer.save();
	}

	/**
	 * Verify that preview icon is available and functional for sidecar module records in Historical Summary page
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26701_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		sugar().revLineItems.recordView.openPrimaryButtonDropdown();

		// Clicking Historical Summary action from the Action dropDrown List
		// TODO: VOOD-738
		new VoodooControl("span", "css", "[name='historical_summary_button']").click();

		// Clicking the Preview Icon of Tasks on Historical Summary View
		// TODO: VOOD-965
		new VoodooControl("a", "css",".layout_RevenueLineItems.drawer.active tr.single:nth-of-type(1) .fieldset-field").click();
		VoodooUtils.waitForReady();
		
		// Verifying that Preview pane of Tasks is Visible
		VoodooControl previewPane = new VoodooControl("div", "css",".layout_RevenueLineItems.drawer.active .preview-headerbar");
		previewPane.assertVisible(true);

		// Verifying the Subject of Tasks in the Preview Pane
		VoodooControl subjectCtrl = new VoodooControl("div", "css", ".layout_RevenueLineItems.drawer.active .preview-data .fld_name");
		subjectCtrl.assertEquals(sugar().tasks.getDefaultData().get("subject"), true);

		// Clicking the preview icon of Notes on Historical Summary View
		new VoodooControl("a", "css",".layout_RevenueLineItems.drawer.active tr.single:nth-of-type(2) .fieldset-field").click();
		VoodooUtils.waitForReady();

		// Verifying that Preview pane of Notes is Visible
		previewPane.assertVisible(true);

		// Verifying the Subject of Notes in the Preview Pane
		subjectCtrl.assertEquals(sugar().notes.getDefaultData().get("subject"), true);

		// Closing the Historical Summary Page
		new VoodooControl("a", "css", ".history-summary-headerpane.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}