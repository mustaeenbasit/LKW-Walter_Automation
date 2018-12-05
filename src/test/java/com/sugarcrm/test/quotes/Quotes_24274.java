package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class Quotes_24274 extends SugarTest {
	FieldSet customFS, systemSettingFS;
	
	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().quotes.api.create();

		// Create two Documents with different name
		FieldSet fs = new FieldSet();
		for(int i = 0; i < 5; i++) {
			fs.put("documentName", testName+"_"+i);
			sugar().documents.api.create(fs);
		}
		
		// Login to sugar
		sugar().login();

		// Go to Admin -> System Settings -> Sub panels and set Items per page "1".
		systemSettingFS = new FieldSet();
		systemSettingFS.put("maxEntriesPerSubPanel", customFS.get("maxEntriesPerSubPanel"));
		sugar().admin.setSystemSettings(systemSettingFS);

		// Go to Quotes detail view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);

		// TODO: VOOD-1000
		// Controls
		VoodooControl createTasks = new VoodooControl("a", "id", "Activities_createtask_button_create_");
		VoodooControl createNotes = new VoodooControl("a", "css", "#formHistory a");
		VoodooControl selectDocuments = new VoodooControl("a", "id", "documents_quotes_select_button");
		VoodooControl searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl selectRecord = new VoodooControl("a", "css", ".oddListRowS1 a");
		VoodooControl searchInput = new VoodooControl("input", "css", "input[name='document_name']");
		BWCSubpanel documentsSubpanel = sugar().quotes.detailView.subpanels.get(sugar().documents.moduleNamePlural);

		// Create 5 Tasks & Notes in the Activities Sub-panel of Quotes detail view
		for(int i = 0; i < 5; i++ ) {
			VoodooUtils.focusFrame("bwc-frame");
			createTasks.click();
			VoodooUtils.focusDefault();
			sugar().tasks.createDrawer.getEditField("subject").set(testName + "_" + i);
			sugar().tasks.createDrawer.save();
			
			// Notes create
			VoodooUtils.focusFrame("bwc-frame");
			createNotes.click();
			VoodooUtils.focusDefault();
			sugar().notes.createDrawer.getEditField("subject").set(testName + "_" + i);
			sugar().notes.createDrawer.save();
			
			// Link 5 Document records in the Documents sub-panel
			documentsSubpanel.expandSubpanelActionsMenu();
			VoodooUtils.focusFrame("bwc-frame");
			selectDocuments.click();
			VoodooUtils.focusWindow(1);
			searchInput.set(testName+"_"+i);
			searchBtnCtrl.click();
			selectRecord.click();
			VoodooUtils.focusWindow(0);
		}
	}

	/**
	 * Verify that the list in the sub-panels of Quotes detail view can be paginated.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_24274_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Quotes detail view and verify sub-panels pagination
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1000
		// Documents, History and Activities sub-panel controls
		VoodooControl activitiesCtrl = new VoodooControl("div", "css", "#list_subpanel_activities");
		VoodooControl historyCtrl = new VoodooControl("div", "css", "#list_subpanel_history");
		VoodooControl documentsCtrl = new VoodooControl("div", "css", "#subpanel_documents");
		VoodooControl activitiesPageNumberCtrl = new VoodooControl("span", "css", activitiesCtrl.getHookString()+ " .pageNumbers");
		VoodooControl historyPageNumberCtrl = new VoodooControl("span", "css", historyCtrl.getHookString()+ " .pageNumbers");
		VoodooControl documentsPageNumberCtrl = new VoodooControl("span", "css", documentsCtrl.getHookString()+ " .pageNumbers");

		// Activity sub-panel Next or Previous button controls
		VoodooControl activitiesNextBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewNextButton'] img");
		VoodooControl activitiesPreviousBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewPrevButton'] img");
		VoodooControl activitiesEndBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewEndButton'] img");
		VoodooControl activitiesStartBtnCtrl = new VoodooControl("img", "css", activitiesCtrl.getHookString()+ " button[name='listViewStartButton'] img");

		// History sub-panel Next or Previous button controls
		VoodooControl historyNextBtnCtrl = new VoodooControl("img", "css", historyCtrl.getHookString()+ " button[name='listViewNextButton'] img");
		VoodooControl historyPreviousBtnCtrl = new VoodooControl("img", "css", historyCtrl.getHookString()+ " button[name='listViewPrevButton'] img");
		VoodooControl historyEndBtnCtrl = new VoodooControl("img", "css", historyCtrl.getHookString()+ " button[name='listViewEndButton'] img");
		VoodooControl historyStartBtnCtrl = new VoodooControl("img", "css", historyCtrl.getHookString()+ " button[name='listViewStartButton'] img");

		// Documents sub-panel Next or Previous button controls
		VoodooControl documentsNextBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewNextButton'] img");
		VoodooControl documentsPreviousBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewPrevButton'] img");
		VoodooControl documentsEndBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewEndButton'] img");
		VoodooControl documentsStartBtnCtrl = new VoodooControl("img", "css", documentsCtrl.getHookString()+ " button[name='listViewStartButton'] img");

		// Click "Next" button on the top right corner of "Documents"/Activities sub-panel.
		activitiesNextBtnCtrl.click();
		historyNextBtnCtrl.click();
		documentsNextBtnCtrl.click();
		
		// Verify the next Activities/History/Documents records list view.
		activitiesPageNumberCtrl.assertContains(customFS.get("nextPageValue"), true);
		historyPageNumberCtrl.assertContains(customFS.get("nextPageValue"), true);
		documentsPageNumberCtrl.assertContains(customFS.get("nextPageValue"), true);

		//  Click "Previous" button on the top right corner of "Activities/History/Documents" sub-panel.
		activitiesPreviousBtnCtrl.click();
		historyPreviousBtnCtrl.click();
		documentsPreviousBtnCtrl.click();

		// Verify the previous "Activities/History/Documents" records list view.
		activitiesPageNumberCtrl.assertContains(customFS.get("previousPageValue"), true);
		historyPageNumberCtrl.assertContains(customFS.get("previousPageValue"), true);
		documentsPageNumberCtrl.assertContains(customFS.get("previousPageValue"), true);
		
		// Click "End" button on the top right corner of "Documents"/Activities sub-panel.
		activitiesEndBtnCtrl.click();
		historyEndBtnCtrl.click();
		documentsEndBtnCtrl.click();

		// Verify the previous "Activities/History/Documents" records list view.
		activitiesPageNumberCtrl.assertContains(customFS.get("endPageValue"), true);
		historyPageNumberCtrl.assertContains(customFS.get("endPageValue"), true);
		documentsPageNumberCtrl.assertContains(customFS.get("endPageValue"), true);

		// Click "Start" button on the top right corner of "Documents"/Activities sub-panel.
		activitiesStartBtnCtrl.click();
		historyStartBtnCtrl.click();
		documentsStartBtnCtrl.click();

		// Verify the previous "Activities/History/Documents" records list view.
		activitiesPageNumberCtrl.assertContains(customFS.get("previousPageValue"), true);
		historyPageNumberCtrl.assertContains(customFS.get("previousPageValue"), true);
		documentsPageNumberCtrl.assertContains(customFS.get("previousPageValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}