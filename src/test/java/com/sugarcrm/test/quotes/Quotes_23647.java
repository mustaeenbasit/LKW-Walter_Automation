package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class Quotes_23647 extends SugarTest {
	FieldSet documentRecord = new FieldSet();
	FieldSet dataRecord = new FieldSet();

	public void setup() throws Exception {

		documentRecord = testData.get(testName).get(0);
		sugar().quotes.api.create();

		// Create three Documents
		for(int i = 1; i <= 3; i++) {
			dataRecord.put("documentName", documentRecord.get("documentName") + "_" + i);
			sugar().documents.api.create(dataRecord);
			dataRecord.clear();
		}

		// Login to sugar
		sugar().login();

		// Navigate to Admin -> System Settings -> Sub panels Items per page "1".
		dataRecord.put("maxEntriesPerSubPanel", documentRecord.get("maxEntriesPerSubPanel"));
		sugar().admin.setSystemSettings(dataRecord);
		dataRecord.clear();

		// Navigate to Quotes detail view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);

		// TODO: VOOD-826(Lib support for document subpanel)
		// Controls for adding document in Quotes record view
		VoodooControl selectDocuments = new VoodooControl("a", "id", "documents_quotes_select_button");
		VoodooControl searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl selectRecord = new VoodooControl("a", "css", ".oddListRowS1 a");

		// Link 3 Documents records in the Documents subpanel
		VoodooControl searchInput = new VoodooControl("input", "css", "input[name='document_name']");
		BWCSubpanel documentsSubpanel = sugar().quotes.detailView.subpanels.get(sugar().documents.moduleNamePlural);
		for(int i = 1; i <= 3; i++) {
			documentsSubpanel.expandSubpanelActionsMenu();
			VoodooUtils.focusFrame("bwc-frame");
			selectDocuments.click();
			VoodooUtils.focusWindow(1);
			searchInput.set( documentRecord.get("documentName") + "_" + i);
			searchBtnCtrl.click();
			selectRecord.click();
			VoodooUtils.focusWindow(0);
		}
	}

	/**
	 *Paginate Quotes_Verify that corresponding documents records' list view is displayed after clicking the pagination control link on "Documents" sub-panel of a quote record detail view.
	 * @throws Exception
	 */
	@Test
	public void Quotes_23647_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Quotes detail view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Documents subpanel controls
		VoodooControl documentsPageNumberCtrl = new VoodooControl("span", "css", "#subpanel_documents .pageNumbers");

		// Documents subpanel Next, Previous, End and Start button controls
		VoodooControl documentsNextBtnCtrl = new VoodooControl("img", "css", "#subpanel_documents button[name='listViewNextButton'] img");
		VoodooControl documentsPreviousBtnCtrl = new VoodooControl("img", "css", "#subpanel_documents button[name='listViewPrevButton'] img");
		VoodooControl documentsEndBtnCtrl = new VoodooControl("img", "css", "#subpanel_documents button[name='listViewEndButton'] img");
		VoodooControl documentsStartBtnCtrl = new VoodooControl("img", "css", "#subpanel_documents button[name='listViewStartButton'] img");

		// Click "Next" button on the top right corner of "Documents" sub-panel.
		documentsNextBtnCtrl.click();

		// Verify the next Documents records list view comparing to current one is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		documentsPageNumberCtrl.assertContains(documentRecord.get("nextPageValue"), true);

		//  Click "Previous" button on the top right corner of "Documents" sub-panel.
		documentsPreviousBtnCtrl.click();

		// Verify the previous Documents records list view comparing to current one is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		documentsPageNumberCtrl.assertContains(documentRecord.get("previousPageValue"), true);

		// Click "End" button on the top right corner of "Documents" sub-panel.
		documentsEndBtnCtrl.click();

		// Verify the last Documents records list view is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		documentsPageNumberCtrl.assertContains(documentRecord.get("endPageValue"), true);

		// Click "Start" button on the top right corner of "Documents" sub-panel.
		documentsStartBtnCtrl.click();

		// Verify the first Documents records list view is displayed.
		// Verify the page number of the subpanels(Order of records is not fix so assert only pagination)
		documentsPageNumberCtrl.assertContains(documentRecord.get("previousPageValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}