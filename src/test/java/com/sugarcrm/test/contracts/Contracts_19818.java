package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19818 extends SugarTest {
	DataSource customDS;
	
	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar.contracts.api.create();
		sugar.documents.api.create(customDS);
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
		
		// Link documents records with contract
		// Go to contracts module
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1713
		for(int i = 0; i < customDS.size(); i++) {
			new VoodooControl("span", "css", "#list_subpanel_contracts_documents table tr.pagination td table tr td:nth-child(1) ul li span").click();
			new VoodooControl("a", "id", "contracts_documents_select_button").click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "css", "[name='document_name']").set(customDS.get(i).get("documentName"));
			new VoodooControl("input", "id", "search_form_submit").click();
	
			// Select Document record
			new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(1) a").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");
		}
		VoodooUtils.focusDefault();
	}

	/**
	 * Detail Contract View_Verify that the sort order of "Document" list can be reversed after clicking the highlighted column.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19818_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
			
		// Go to contracts module
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click on name title column
		VoodooControl columnTitle = new VoodooControl("a", "css", "#list_subpanel_contracts_documents tr:nth-child(2) th:nth-child(2) span a");
		columnTitle.click();
		
		// Verify that the sort order is reversed correctly by clicked column.
		int j = 0;
		for(int i = customDS.size()-1; i >= 0; i--) {
			new VoodooControl("a", "css", "#list_subpanel_contracts_documents tr:nth-child("+(j+3)+") > td:nth-child(2) a").assertEquals(customDS.get(i).get("documentName"), true);
			j++;
		}
		
		// Reset Sorting order to default state
		columnTitle.click();
		
		// TODO: VOOD-1713
		// Verify that record listed before clicking on column title
		for(int i = 0; i < customDS.size(); i++) 
			new VoodooControl("a", "css", "#list_subpanel_contracts_documents tr:nth-child("+(i+3)+") > td:nth-child(2) a").assertEquals(customDS.get(i).get("documentName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}