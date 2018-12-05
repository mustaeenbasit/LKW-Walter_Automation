package com.sugarcrm.test.KnowledgeBase;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik  <skaushik@sugarcrm.com>
 */
public class KnowledgeBase_30141 extends SugarTest {
	DataSource kbData = new DataSource();
	int kbRecordsNo = 0;
	ArrayList<Record> kbRecords = new ArrayList<Record>();
	
	public void setup() throws Exception {
		kbData = testData.get(testName);
		kbRecordsNo = kbData.size();

		// Create 3 KB articles with default Author
		kbRecords = sugar().knowledgeBase.api.create(kbData);
		sugar().cases.api.create();

		// Login 
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.sortBy("headerName", false);
		sugar().knowledgeBase.listView.clickRecord(1);

		// Assign KB Documents to specific users
		// TODO: VOOD-444
		for (int i = 1 ; i <= kbRecordsNo ; i++) {
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.showMore();
			sugar().knowledgeBase.recordView.getEditField("relAssignedTo").set(kbData.get(kbRecordsNo - i).get("relAssignedTo"));
			sugar().knowledgeBase.recordView.save();
			sugar().knowledgeBase.recordView.gotoNextRecord();
		}

		// Navigating to admin tool
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1511
		new VoodooControl("a", "id", "studiolink_Cases").click();
		VoodooUtils.waitForReady();

		// Adding "Assigned to" in the subpanels list
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("tr", "css", "#Buttons tr:nth-child(2) td:nth-child(2)").click();

		VoodooControl languageCtrl = new VoodooControl("li", "css", "[data-name='language']");
		VoodooControl assignedUserCtrl = new VoodooControl("li", "css", "[data-name='assigned_user_name']");
		assignedUserCtrl.scrollIntoViewIfNeeded(false);
		assignedUserCtrl.dragNDrop(languageCtrl);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that "Assign to" field has applied for in KB module
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30141_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB module and preview a record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.previewRecord(1);

		// Verify "Assigned To" field is shown on Knowledge Base Preview Panel
		sugar().previewPane.showMore();
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertEquals(kbData.get(kbRecordsNo - 1).get("relAssignedTo"), true);
		sugar().previewPane.closePreview();

		// TODO: VOOD-1517 VOOD-1761 
		sugar().knowledgeBase.listView.getControl("moreColumn").click();
		new VoodooControl("li", "css", ".dropdown-menu.left " + "[data-field-toggle='assigned_user_name']").click();
		sugar().knowledgeBase.listView.getControl("moreColumn").click();

		sugar().knowledgeBase.listView.getDetailField(1, "relAssignedTo").scrollIntoViewIfNeeded(sugar().knowledgeBase.listView.getControl("horizontalScrollBar"), false);
		sugar().knowledgeBase.listView.getControl("horizontalScrollBar").scrollHorizontally(200);

		// Clicking on Assigned User Name header to sort records in Descending order
		new VoodooControl("th", "css", ".orderByassigned_user_name").click();
		VoodooUtils.waitForReady();

		// Verify that records are sorted in descending order w.r.t "Assigned to" column
		for (int i = 1 ; i <= kbRecordsNo ; i++)
			sugar().knowledgeBase.listView.verifyField(i, "relAssignedTo", kbData.get(kbRecordsNo - i).get("relAssignedTo"));

		// Open a Case >> KB subpanel.
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		StandardSubpanel kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);

		// Linking above 3 KB records
		kbSubpanel.linkExistingRecords(kbRecords);
		
		// Click on "Assigned to" column (in order to sort records w.r.t "Assigned to" column)
		kbSubpanel.sortBy("headerAssignedusername", false);
		//new VoodooControl("th", "css", ".orderByassigned_user_name").click();
		VoodooUtils.waitForReady();

		// Verify that records are sorted in descending order w.r.t "Assigned to" column
		for (int i = 1 ; i <= kbRecordsNo ; i++)
			kbSubpanel.getDetailField(i, "relAssignedTo").assertEquals(kbData.get(kbRecordsNo - i).get("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}