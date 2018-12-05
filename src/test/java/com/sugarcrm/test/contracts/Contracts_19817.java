package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19817 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar().contracts.api.create();
		sugar().documents.api.create(customDS);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);

		// Link documents records with contract
		// Go to contracts module
		sugar().contracts.navToListView();
		sugar().contracts.listView.clickRecord(1);
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
	 * Detail Contract View_Verify that Document can be sorted by each column in "Contract" detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Go to contracts module
		sugar().contracts.navToListView();
		sugar().contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-779
		new VoodooControl("span", "css", "#list_subpanel_contracts_documents .pageNumbers").assertContains("(1 - 3 of 3)", true);

		// Click on name title column
		VoodooControl nameColumn = new VoodooControl("a", "css", "#list_subpanel_contracts_documents tr:nth-child(2) th:nth-child(2) span a");
		nameColumn.click();

		// TODO: VOOD-1713
		// Verify that Document sorted correctly by name column.
		for(int i = 1; i <= customDS.size(); i++) {
			new VoodooControl("a", "css", "#list_subpanel_contracts_documents tr:nth-child("+(i+2)+") > td:nth-child(2) a").assertEquals(customDS.get(customDS.size()-i).get("documentName"), true);
		}

		// Reset Sorting order to default state
		nameColumn.click();

		// Click on Category column
		VoodooControl categoryColumn = new VoodooControl("a", "css", "#list_subpanel_contracts_documents  tr:nth-child(2) > th:nth-child(4) span a");
		categoryColumn.click();

		// TODO: VOOD-1713
		// Verify that Document sorted correctly by Category column.
		for(int i = 3; i > customDS.size(); i--){
			new VoodooControl("span", "css", "#list_subpanel_contracts_documents tr:nth-child("+(3-i)+") > td:nth-child(4) span").assertEquals(customDS.get(i).get("categoryID"), true);
		}

		// Reset Sorting order to default state
		categoryColumn.click();

		// Click on status column
		VoodooControl statusColumn = new VoodooControl("a", "css", "#list_subpanel_contracts_documents  tr:nth-child(2) > th:nth-child(6) span a");
		statusColumn.click();

		// TODO: VOOD-1713
		// Verify that Document sorted correctly by Status column.
		for(int i = 3; i > customDS.size(); i--){
			new VoodooControl("span", "css", "#list_subpanel_contracts_documents tr:nth-child("+(3-i)+") > td:nth-child(6) span").assertEquals(customDS.get(i).get("categoryID"), true);
		}

		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}