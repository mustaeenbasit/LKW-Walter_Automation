package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class Quotes_23940 extends SugarTest {
	FieldSet multiPurposeData, multiPurposeFS;

	public void setup() throws Exception {

		multiPurposeFS = new FieldSet();
		multiPurposeData = testData.get(testName).get(0);
		sugar.quotes.api.create();

		// Create five Documents
		for(int i = 0; i < 5; i++) {
			multiPurposeFS.put("documentName", multiPurposeData.get("documentName") + "_" + i);
			sugar.documents.api.create(multiPurposeFS);
			multiPurposeFS.clear();
		}

		// Login to sugar
		sugar.login();

		// Navigate to Admin -> System Settings -> Sub panels Items per page "2".
		multiPurposeFS.put("maxEntriesPerSubPanel", multiPurposeData.get("maxEntriesPerSubPanel"));
		sugar.admin.setSystemSettings(multiPurposeFS);
		multiPurposeFS.clear();

		// Navigate to Quotes detail view
		sugar.quotes.navToListView();
		sugar.quotes.listView.clickRecord(1);

		// Controls
		VoodooControl createTasks = new VoodooControl("a", "id", "Activities_createtask_button_create_");
		VoodooControl selectDocuments = new VoodooControl("a", "id", "documents_quotes_select_button");
		VoodooControl searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl selectRecord = new VoodooControl("a", "css", ".oddListRowS1 a");

		// Create 5 Tasks in the Activities Subpanel of Quotes
		for(int i = 0; i < 5; i++ ) {
			VoodooUtils.focusFrame("bwc-frame");
			createTasks.click();
			VoodooUtils.focusDefault();
			sugar.tasks.createDrawer.getEditField("subject").set(testName + "_" + i);
			sugar.tasks.createDrawer.save();
		}

		// Link 5 Documents records in the Documents subpanel
		VoodooControl searchInput = new VoodooControl("input", "css", "input[name='document_name']");
		BWCSubpanel documentsSubpanel = sugar.quotes.detailView.subpanels.get(sugar.documents.moduleNamePlural);
		for(int i = 0; i < 5; i++) {
			documentsSubpanel.expandSubpanelActionsMenu();
			VoodooUtils.focusFrame("bwc-frame");
			selectDocuments.click();
			VoodooUtils.focusWindow(1);
			searchInput.set( multiPurposeData.get("documentName") + "_" + i);
			searchBtnCtrl.click();
			selectRecord.click();
			VoodooUtils.focusWindow(0);
		}
	}

	/**
	 *Paginate Leads_Verify that corresponding lead records' list view is displayed after clicking the pagination control link on "LEADS" sub-panel of a contact record detail view.
	 * @throws Exception
	 */
	@Test
	public void Quotes_23940_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Quotes detail view
		sugar.quotes.navToListView();
		sugar.quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Documents and Activities subpanel controls
		VoodooControl activitiesCtrl = new VoodooControl("div", "css", "#list_subpanel_activities");
		VoodooControl documentsCtrl = new VoodooControl("div", "css", "#subpanel_documents");
		VoodooControl activitiesPageNumberCtrl = new VoodooControl("span", "css", activitiesCtrl.getHookString()+ " .pageNumbers");
		VoodooControl documentsPageNumberCtrl = new VoodooControl("span", "css", documentsCtrl.getHookString()+ " .pageNumbers");

		// Activity subpanel Next, Previous, End and Start button controls
		VoodooControl activitiesNextBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewNextButton'] img");
		VoodooControl activitiesPreviousBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewPrevButton'] img");
		VoodooControl activitiesEndBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewEndButton'] img");
		VoodooControl activitiesStartBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewStartButton'] img");

		// Documents subpanel Next, Previous, End and Start button controls
		VoodooControl documentsNextBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewNextButton'] img");
		VoodooControl documentsPreviousBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewPrevButton'] img");
		VoodooControl documentsEndBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewEndButton'] img");
		VoodooControl documentsStartBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewStartButton'] img");

		// Click "Next" button on the top right corner of "Documents"/Activities sub-panel.
		activitiesNextBtnCtrl.click();
		documentsNextBtnCtrl.click();

		// Verify the next Documents/Activities records list view comparing to current one is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("nextPageValue"), true);
		documentsPageNumberCtrl.assertContains(multiPurposeData.get("nextPageValue"), true);

		//  Click "Previous" button on the top right corner of "Documents"/Activities sub-panel.
		activitiesPreviousBtnCtrl.click();
		documentsPreviousBtnCtrl.click();

		// Verify the previous Documents/Activities records list view comparing to current one is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("previousPageValue"), true);
		documentsPageNumberCtrl.assertContains(multiPurposeData.get("previousPageValue"), true);

		// Click "End" button on the top right corner of "Documents"/Activities sub-panel.
		activitiesEndBtnCtrl.click();
		documentsEndBtnCtrl.click();

		// Verify the last Documents/Activities records list view is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("endPageValue"), true);
		documentsPageNumberCtrl.assertContains(multiPurposeData.get("endPageValue"), true);

		// Click "Start" button on the top right corner of "Documents"/Activities sub-panel.
		activitiesStartBtnCtrl.click();
		documentsStartBtnCtrl.click();

		// Verify the first Documents/Activities records list view is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		activitiesPageNumberCtrl.assertContains(multiPurposeData.get("previousPageValue"), true);
		documentsPageNumberCtrl.assertContains(multiPurposeData.get("previousPageValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}