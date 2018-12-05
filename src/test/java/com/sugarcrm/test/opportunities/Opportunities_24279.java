package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.SortFailedException;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24279 extends SugarTest {
	DataSource oppData;
	
	public void setup() throws Exception {
		oppData = testData.get(testName);
		sugar().accounts.api.create();
		
		sugar().login();
		sugar().opportunities.create(oppData);
	}
	
	/**
	 * TODO: VOOD-1452
	 * Need to add this method and use as replacement of existing sortBy. 
	 * Reason is, existing sortBy() is failing to get columns in view before sorting.
	 * 
	 * NOTE: Once existing sortBy() method will be fixed, remove this method.
	 */
	private void customSortBy(String header, boolean ascending) throws Exception {
		VoodooControl columnHeader = sugar().opportunities.listView.getControl(header);
		String headerClass = columnHeader.getAttribute("class");
		VoodooUtils.voodoo.log.info(header + " has class value of: " + headerClass);
		String direction = (ascending ? " ascending" : " descending");

		// If the sort is already correct, do nothing.
		if (ascending && headerClass.contains("sorting_asc") || !ascending && headerClass.contains("sorting_desc")) {
			VoodooUtils.voodoo.log.info("Column is already sorted " + direction + ".  Skipping click.");
		} else { // sort is not already correct, so sort it.
			// Max 2 attempts -- first sort may not be the desired direction,
			// but the second should be for sure.
			for (int i = 0; i < 2; i++) {
				columnHeader.scrollIntoViewIfNeeded(true);
				sugar().alerts.waitForLoadingExpiration();
				headerClass = columnHeader.getAttribute("class");

				// if the sort was successful, we're done
				if (ascending && headerClass.contains("sorting_asc") || !ascending && headerClass.contains("sorting_desc")) {
					VoodooUtils.voodoo.log.info("Sorted column " + header + " " + direction + ".");
					return;
				}
			}
			// If we haven't returned by now, the sort failed.
			VoodooUtils.voodoo.log.severe("Could not sort header \"" + header + "\" not defined in this view!");
			throw (new SortFailedException(header));
		}
	}

	/**
	 * Sort Opportunities_Verify that opportunities can be sorted by column in "Opportunities" list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24279_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities listview and verify the sorting feature for fields: "User" and "Date Created"
		sugar().opportunities.navToListView();
		
		// TODO: VOOD-1452 -Once VOOD-1452 resolved, replace customSortBy() by sortBy() method
		// Sort 'User' field in ascending order and verify
		// sugar().opportunities.listView.sortBy("headerAssignedusername", true);
		customSortBy("headerAssignedusername", true);
		VoodooUtils.waitForReady();
		sugar().opportunities.listView.verifyField(1, "name", oppData.get(0).get("name"));
		sugar().opportunities.listView.verifyField(2, "name", oppData.get(1).get("name"));
		sugar().opportunities.listView.verifyField(3, "name", oppData.get(2).get("name"));
		
		// Sort 'User' field in descending order and verify
		// sugar().opportunities.listView.sortBy("headerAssignedusername", false);
		customSortBy("headerAssignedusername", false);
		VoodooUtils.waitForReady();
		sugar().opportunities.listView.verifyField(1, "name", oppData.get(2).get("name"));
		sugar().opportunities.listView.verifyField(2, "name", oppData.get(1).get("name"));
		sugar().opportunities.listView.verifyField(3, "name", oppData.get(0).get("name"));
		
		// Sort 'Date Created' field in ascending order and verify
		// sugar().opportunities.listView.sortBy("headerDateentered", true);
		customSortBy("headerDateentered", true);
		VoodooUtils.waitForReady();
		sugar().opportunities.listView.verifyField(1, "name", oppData.get(0).get("name"));
		sugar().opportunities.listView.verifyField(2, "name", oppData.get(1).get("name"));
		sugar().opportunities.listView.verifyField(3, "name", oppData.get(2).get("name"));
		
		// Sort 'Date Created' field in descending order and verify
		// sugar().opportunities.listView.sortBy("headerDateentered", false);
		customSortBy("headerDateentered", false);
		VoodooUtils.waitForReady();
		sugar().opportunities.listView.verifyField(1, "name", oppData.get(2).get("name"));
		sugar().opportunities.listView.verifyField(2, "name", oppData.get(1).get("name"));
		sugar().opportunities.listView.verifyField(3, "name", oppData.get(0).get("name"));
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}