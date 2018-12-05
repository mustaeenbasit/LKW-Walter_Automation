package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contracts_19770 extends SugarTest {
	VoodooControl studio, studioContracts, subpanelsBtn, documentSubpanel, latestRevisionField, saveAndDeploy;
	DataSource documentsData = new DataSource();

	public void setup() throws Exception {
		documentsData = testData.get(testName);
		sugar.contracts.api.create();
		studio = sugar.admin.adminTools.getControl("studio");

		// TODO: VOOD-542, VOOD-1511
		studioContracts = new VoodooControl("a", "id", "studiolink_Contracts");
		subpanelsBtn = new VoodooControl("td", "id", "subpanelsBtn");
		documentSubpanel = new VoodooControl("td", "css", "#Buttons tr td");
		latestRevisionField = new VoodooControl("li", "css", "li[data-name=latest_revision_name]");
		saveAndDeploy = new VoodooControl("input", "id", "savebtn");
		sugar.login();
		
		// TODO: VOOD-444
		// Document records created via UI
		VoodooControl docName = sugar.documents.editView.getEditField("documentName");
		VoodooControl fileName = sugar.documents.editView.getEditField("fileNameFile");
		VoodooFileField fileFieldName = new VoodooFileField(fileName.getTag(), fileName.getStrategyName(), fileName.getHookString()); 				
		for (int i = 0; i < documentsData.size(); i++) {
			sugar.navbar.selectMenuItem(sugar.documents, "createDocument");
			VoodooUtils.focusFrame("bwc-frame");
			docName.set(documentsData.get(i).get("documentName"));
			fileFieldName.set(documentsData.get(i).get("fileNameFile"));
			VoodooUtils.focusDefault();
			sugar.documents.editView.save();
			// Special case for last record: to change revision number for doc (needed while sorting of records)
			if(i==documentsData.size()-1){
				VoodooUtils.focusFrame("bwc-frame");
				new VoodooControl("a", "id", "document_revisions_create_button").click();
				VoodooUtils.waitForReady();
				fileFieldName.set(documentsData.get(i).get("fileNameFile"));
				new VoodooControl("input", "id", "SAVE_FOOTER").click();
				VoodooUtils.focusDefault();	
			}
		}

		// Enable contracts
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		// Studio -> Contracts -> Subpanels -> Documents -> Add latest revision name field to Default
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studio.click();
		VoodooUtils.waitForReady();
		studioContracts.click();
		VoodooUtils.waitForReady();
		subpanelsBtn.click();
		VoodooUtils.waitForReady();
		documentSubpanel.click();
		VoodooUtils.waitForReady();
		latestRevisionField.dragNDropViaJS(new VoodooControl("td", "css", "#Default #ul0"));
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Contracts Detail - Documents sub-panel - Sort_Verify that no SQL Error is displayed when sorting documents list in contracts detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19770_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Contracts record
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Document Subpanel
		// TODO: VOOD-1713
		VoodooControl actionDropdown = new VoodooControl("span", "css", "#list_subpanel_contracts_documents table tr.pagination td table tr td:nth-child(1) ul li span");
		VoodooControl selectBtn = new VoodooControl("a", "id", "contracts_documents_select_button");
		VoodooControl oddRow = new VoodooControl("a", "css", "tr.oddListRowS1 td a");
		VoodooControl evenRow = new VoodooControl("a", "css", "tr.evenListRowS1 td a");

		actionDropdown.click();
		selectBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Verify documents are sorted according to document name
		oddRow.assertEquals(documentsData.get(2).get("documentName"), true);
		evenRow.assertEquals(documentsData.get(1).get("documentName"), true);
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(5) td a").assertEquals(documentsData.get(0).get("documentName"), true);
		
		// Select 2 records in document subpanel (i.e Link records)
		oddRow.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		actionDropdown.click();
		selectBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		evenRow.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1534
		VoodooControl latestRevisionHeader = new VoodooControl("a", "css", "#list_subpanel_contracts_documents th:nth-of-type(10) a");
		VoodooControl oddDocRecord = new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(2) a");
		VoodooControl evenDocRecord = new VoodooControl("a", "css", "tr.evenListRowS1 td:nth-of-type(2) a");

		// Verify latest revision sort records in descending order without any type of error
		latestRevisionHeader.click();
		oddDocRecord.assertEquals(documentsData.get(2).get("documentName"), true);
		evenDocRecord.assertEquals(documentsData.get(1).get("documentName"), true);

		// Verify latest revision sort records in ascending order without any type of error
		latestRevisionHeader.click();
		oddDocRecord.assertEquals(documentsData.get(1).get("documentName"), true);
		evenDocRecord.assertEquals(documentsData.get(2).get("documentName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}