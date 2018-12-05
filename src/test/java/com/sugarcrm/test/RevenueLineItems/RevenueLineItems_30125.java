package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_30125 extends SugarTest {
	public void setup() throws Exception {
		// RLI, Task, Note record created via API
		sugar().revLineItems.api.create();
		FieldSet customData = new FieldSet();
		customData.put("status", testData.get(testName).get(0).get("status"));
		TaskRecord myTask = (TaskRecord)sugar().tasks.api.create(customData);
		NoteRecord myNote = (NoteRecord)sugar().notes.api.create();
		sugar().login();

		// Task and note record associated with RLI record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		StandardSubpanel taskSubpanel = sugar().revLineItems.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubpanel.linkExistingRecord(myTask);

		StandardSubpanel noteSubpanel = sugar().revLineItems.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		noteSubpanel.linkExistingRecord(myNote);
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.closeAllSuccess();
	}

	/**
	 * Verify Preview should not be broken on the historical summary view.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30125_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.recordView.openPrimaryButtonDropdown();

		// Open Historical Summary
		// TODO: VOOD-965 - Support for Historical Summary page 
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify Preview Panel should open on clicking preview button
		new VoodooControl("a", "css", ".layout_RevenueLineItems.drawer.active .fa.fa-eye").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".block.preview-data .fld_name.detail div").assertEquals(sugar().notes.getDefaultData().get("subject"), true);

		// TODO: VOOD-1887 - Once resolved commented line will work
		//sugar().previewPane.assertVisible(true);
		// TODO: VOOD-1349 - Once resolved commented lines will work
		//sugar().previewPane.setModule(sugar().notes);
		//sugar().previewPane.getPreviewPaneField("subject").assertEquals(sugar().notes.getDefaultData().get("subject"), true);
		new VoodooControl("a", "css", ".layout_RevenueLineItems.drawer.active tr.single:nth-of-type(2) .fa.fa-eye").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1349 - Once resolved commented lines will work
		//sugar().previewPane.setModule(sugar().tasks);
		//sugar().previewPane.getPreviewPaneField("subject").assertEquals(sugar().tasks.getDefaultData().get("subject"), true);
		new VoodooControl("div", "css", ".block.preview-data .fld_name.detail div").assertEquals(sugar().tasks.getDefaultData().get("subject"), true);

		// Close historical summary
		new VoodooControl("a", "css", ".history-summary-headerpane.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}