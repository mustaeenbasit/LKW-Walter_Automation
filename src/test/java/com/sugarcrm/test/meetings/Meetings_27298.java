package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27298 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Edit All Recurrences" button is available in 'view' mode only on parent record and not available in 'edit' mode
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27298_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.createDrawer.getEditField("repeatType").set(customData.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(customData.get("repeat_count"));

		// Set reminder email = 5 minutes
		sugar().meetings.createDrawer.getEditField("remindersEmail").set(customData.get("remindersEmail"));
		sugar().meetings.recordView.save();
		sugar().meetings.navToListView();

		// TODO: VOOD-1222 -Need library support to verify fields in ListView and RecordView of Calls/Meetings module
		VoodooControl actionDropdown = new VoodooControl("li", "xpath", "//*[@class='dropdown-menu']/li[contains(.,'"+customData.get("rowActionDropdown")+"')]");

		// Click on child meeting row action dropdown
		VoodooControl actionDropDown1 = new VoodooControl("span", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+customData.get("date_meeting2")+"')]/td[8]/span/div[1]/span/a/span");
		actionDropDown1.click();
		VoodooUtils.waitForReady();

		// Verify That "Edit All Recurrences" action is not available at the child meeting dropdown.
		actionDropdown.assertExists(false);
		actionDropDown1.click(); // Click to close opened action dropdown

		// Click on parent meeting row action dropdown
		VoodooControl actionDropDown2 = new VoodooControl("span", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+customData.get("date_meeting1")+"')]/td[8]/span/div[1]/span/a/span");
		actionDropDown2.click();
		VoodooUtils.waitForReady();

		// Verify That "Edit All Recurrences" action is not available at the parent meeting dropdown.		
		actionDropdown.assertExists(false);
		actionDropDown2.click(); // Click to close opened action dropdown

		// Go to parent meeting recordView
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+customData.get("date_meeting1")+"')]/td[2]/span/div/a").click();
		sugar().meetings.recordView.openPrimaryButtonDropdown();

		// Verify That "Edit All Recurrences" action is available in the parent meeting recordView action drop down.
		actionDropdown.assertExists(true);

		// Verify that "Edit All Recurrences" action is available in the child meeting recordView action drop down.
		sugar().meetings.navToListView();
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+customData.get("date_meeting2")+"')]/td[2]/span/div/a").click();
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		actionDropdown.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}