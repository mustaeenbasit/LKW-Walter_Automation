package com.sugarcrm.test.cases;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23458 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();

		// Login to Sugar
		sugar().login();
	}

	/**
	 * New action dropdown list in case detail view page
	 * @throws Exception
	 * */
	@Test
	public void Cases_23458_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource actionDropdownList = testData.get(testName);

		// Go to a case record view page
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Verify that the action dropdown list is shown above the overview panel
		sugar().cases.recordView.getControl("primaryButtonDropdown").assertVisible(true);

		// Click the down arrow beside Edit action
		sugar().cases.recordView.openPrimaryButtonDropdown();

		// Controls for primary button dropdown list
		// TODO: VOOD-738, 691, 578, 695
		VoodooControl shareCtrl = new VoodooControl("a", "css", ".fld_share.detail a");
		VoodooControl createArticleCtrl =new VoodooControl("a", "css", ".fld_create_button.detail a");
		VoodooControl findDuplicateCtrl =new VoodooControl("a", "css", ".fld_find_duplicates_button.detail a");
		VoodooControl historicalSummaryCtrl =new VoodooControl("a", "css", ".fld_historical_summary_button.detail a");
		VoodooControl viewChangeLogCtrl =new VoodooControl("a", "css", ".fld_audit_button.detail a");

		// Controls already exist
		VoodooControl editCtrl = sugar().cases.recordView.getControl("editButton");
		VoodooControl copyCtrl = sugar().cases.recordView.getControl("copyButton");
		VoodooControl deleteCtrl = sugar().cases.recordView.getControl("deleteButton");

		// Add all for primary button dropdown list controls into an Array list
		ArrayList<VoodooControl> primaryButtomDropdownListControls = new ArrayList<VoodooControl>();
		primaryButtomDropdownListControls.add(editCtrl);
		primaryButtomDropdownListControls.add(shareCtrl);
		primaryButtomDropdownListControls.add(createArticleCtrl);
		primaryButtomDropdownListControls.add(findDuplicateCtrl);
		primaryButtomDropdownListControls.add(copyCtrl);
		primaryButtomDropdownListControls.add(historicalSummaryCtrl);
		primaryButtomDropdownListControls.add(viewChangeLogCtrl);
		primaryButtomDropdownListControls.add(deleteCtrl);

		// Verify that the action dropdown shows all of the actions in the dropdown list: Edit, Share, Create Article, Find Duplicates, Copy, Historical Summary, View Change Log and Delete
		for (int i = 0; i < primaryButtomDropdownListControls.size(); i++) {
			primaryButtomDropdownListControls.get(i).assertExists(true);
			primaryButtomDropdownListControls.get(i).assertEquals(actionDropdownList.get(i).get("primaryButtomDropdownListValues"), true);
		}

		// Click on any action in the list(Click on Copy button)
		copyCtrl.click();

		// Verify that the action is triggered
		sugar().cases.createDrawer.assertVisible(true);
		sugar().cases.createDrawer.getEditField("name").assertContains(sugar().cases.getDefaultData().get("name"), true);

		// Click on the Cnacel button on Create drawer
		sugar().cases.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
