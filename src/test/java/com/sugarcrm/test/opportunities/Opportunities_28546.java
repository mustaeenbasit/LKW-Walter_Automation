package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28546 extends SugarTest {
	DataSource oppData = new DataSource();

	public void setup() throws Exception {
		oppData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		FieldSet oppRliData = new FieldSet();
		// Create two Opportunity records with RLI records (one with "Closed Won" & other with "Closed Lost") 
		for(int i = 0; i < oppData.size(); i++){
			oppRliData.put("name", oppData.get(i).get("name"));
			oppRliData.put("rli_name", oppData.get(i).get("rli_name"));
			oppRliData.put("rli_stage", oppData.get(i).get("salesStage"));
			sugar().opportunities.create(oppRliData);
			oppRliData.clear();
		}
	}

	/**
	 * Verify that user is not able to Delete Closed Won and Closed Lost Opportunities from the list view after removing "disabled" attribute
	 * @throws Exception
	 */
	@Ignore("VOOD-1780")
	@Test
	public void Opportunities_28546_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		// Sort the opp records to identify & assert records appropriately
		sugar().opportunities.listView.sortBy("headerName", true);
		sugar().opportunities.listView.openRowActionDropdown(1);

		// Verify "Delete" button should be disabled for "Closed Won"
		VoodooControl closedWondeleteCtrl = sugar().opportunities.listView.getControl("delete01");
		Assert.assertTrue(closedWondeleteCtrl.isDisabled());

		// Inspect the Delete button from Developer tools and Modify the class from 
		// {{rowaction disabled}} to {{rowaction}}. (i.e. remove {{disabled}} attribute )
		// TODO: VOOD-1545
		String jsCodeSnippet = "jQuery(\"a[name='delete_button']\").removeClass('disabled')";
		VoodooUtils.executeJS(jsCodeSnippet);

		sugar().opportunities.listView.openRowActionDropdown(1);
		// Verify the disbaled class is removed
		closedWondeleteCtrl.assertAttribute("class", oppData.get(0).get("class"), false);

		// Verify that delete button should not actually delete the record 
		// if disabled attribute is removed using browser developer tool.
		sugar().opportunities.listView.deleteRecord(1);
		int rowCount = sugar().opportunities.listView.countRows();
		Assert.assertTrue("total rows in list view when record gets deleted is not equal to 2", rowCount == 2);
		sugar().opportunities.listView.getDetailField(1, "name").assertEquals(oppData.get(0).get("name"), true);

		// Click to disappear tooltip
		sugar().opportunities.listView.checkRecord(2);
		sugar().opportunities.listView.openRowActionDropdown(2);

		// Verify "Delete" button should be disabled for "Closed Lost"
		VoodooControl closedLostdeleteCtrl = sugar().opportunities.listView.getControl("delete02");
		Assert.assertTrue(closedLostdeleteCtrl.isDisabled());

		// Inspect the Delete button from Developer tools and Modify the class from 
		// {{rowaction disabled}} to {{rowaction}}. (i.e. remove {{disabled}} attribute )
		// TODO: VOOD-1545
		VoodooUtils.executeJS(jsCodeSnippet);

		sugar().opportunities.listView.openRowActionDropdown(2);

		// Verify the disbaled class is removed
		closedLostdeleteCtrl.assertAttribute("class", oppData.get(0).get("class"), false);

		// Verify that delete button should not actually delete the record 
		// if disabled attribute is removed using browser developer tool.
		sugar().opportunities.listView.deleteRecord(2);
		Assert.assertTrue("total rows in list view when record gets deleted is not equal to 2", rowCount == 2);
		sugar().opportunities.listView.getDetailField(2, "name").assertEquals(oppData.get(1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}