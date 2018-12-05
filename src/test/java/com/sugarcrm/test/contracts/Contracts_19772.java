package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contracts_19772 extends SugarTest {
	DataSource documentsData = new DataSource();
	FieldSet systemSettingsData = new FieldSet(), subpanelPageSize = new FieldSet();

	public void setup() throws Exception {
		documentsData = testData.get(testName+"_documents");
		subpanelPageSize = testData.get(testName).get(0);

		// Contracts and documents data record created via API
		sugar().contracts.api.create();
		sugar().documents.api.create(documentsData);

		sugar().login();

		// Set Subpanel size to "2" from admin system settings
		systemSettingsData.put("maxEntriesPerSubPanel", subpanelPageSize.get("customMaxEntriesPerSubPanel"));
		sugar().admin.setSystemSettings(systemSettingsData);

		// Enable contracts
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
	}

	/**
	 * Contracts Detail - Documents sub-panel - Select_Corresponding page number is displayed on "DOCUMNETS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19772_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Contracts record
		sugar().contracts.navToListView();
		sugar().contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Document Subpanel, link all document records
		// TODO: VOOD-1713
		VoodooControl actionDropdown = new VoodooControl("span", "css", "#list_subpanel_contracts_documents table tr.pagination td table tr td:nth-child(1) ul li span");
		VoodooControl selectBtn = new VoodooControl("a", "id", "contracts_documents_select_button");
		VoodooControl searchInput = new VoodooControl("input", "css", "input[name=document_name]");
		VoodooControl searchBtn = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl row = new VoodooControl("a", "css", "tr.oddListRowS1 td a");

		for (int i = 0; i < documentsData.size(); i++) {
			actionDropdown.click();
			selectBtn.click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusWindow(1);
			searchInput.set(documentsData.get(i).get("documentName"));
			searchBtn.click();
			row.click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");
		}

		// Verify document records with pagination
		VoodooControl oddDocRow = new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(2) a");
		VoodooControl evenDocRow = new VoodooControl("a", "css", "tr.evenListRowS1 td:nth-of-type(2) a");
		VoodooControl nextButton = new VoodooControl("button", "css", "#list_subpanel_contracts_documents button[name=listViewNextButton]");
		VoodooControl oddRow = new VoodooControl("tr", "css", "#list_subpanel_contracts_documents tr.oddListRowS1");
		VoodooControl evenRow = new VoodooControl("tr", "css", "#list_subpanel_contracts_documents tr.evenListRowS1");
		// #list_subpanel_contracts_documents tr[class*='ListRowS1'] - not returning exact count with countAll, countWithClass methods

		// default only 2 records appear
		int countRows = oddRow.countWithClass() + evenRow.countWithClass();
		Assert.assertTrue("Records are not equal two", countRows == 2);
		oddDocRow.assertEquals(documentsData.get(4).get("documentName"), true);
		evenDocRow.assertEquals(documentsData.get(3).get("documentName"), true);

		// click on next button, 2 records appear 
		nextButton.click();
		countRows = oddRow.countWithClass() + evenRow.countWithClass();
		Assert.assertTrue("Records are not equal two", countRows == 2);
		oddDocRow.assertEquals(documentsData.get(2).get("documentName"), true);
		evenDocRow.assertEquals(documentsData.get(1).get("documentName"), true);

		// click on next button, 1 record appear only
		nextButton.click();
		evenDocRow.assertVisible(false);
		countRows = oddRow.countWithClass();
		Assert.assertTrue("Record are not equal one", countRows == 1);
		oddDocRow.assertEquals(documentsData.get(0).get("documentName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}