package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_20032 extends SugarTest {

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify "Search and Select" brings up selection view.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_20032_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities list view and bring up the detailed view of an Opportunity.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.showMore();
		sugar().opportunities.recordView.getDetailField("relAssignedTo").hover();
		VoodooUtils.waitForReady();

		// TODO: VOOD-795 - Need lib support for "Related to" dropdown on Task record view
		// Hover over "Assigned to" > click pencil button > click down arrow.
		new VoodooControl("i", "css", "span[data-name='assigned_user_name'] .fa.fa-pencil").click();

		// TODO: VOOD-1694 - Clicking on the relate dropdown using automation does not keep it expanded  
		new VoodooControl("span", "css",".fld_assigned_user_name.edit span").click();

		// Click on "Search and Select"		
		VoodooSelect searchSelect = (VoodooSelect) sugar().opportunities.recordView.getEditField("relAssignedTo");
		searchSelect.selectWidget.getControl("searchForMoreLink").click();

		// TODO: VOOD-1268 - Need lib support for records/rows count in Search and Select Drawer
		// Verify drawer appears displaying the available options with a radio button next to each item
		new VoodooControl("input", "css", ".fld_Users_select.list .selection").waitForVisible();
		DataSource customData = testData.get(testName);

		// Verify drawer appears displaying a radio button next to each item
		for (int i = 1; i <= customData.size(); i++) {
			sugar().users.searchSelect.getControl(String.format("selectInput%02d", i)).assertVisible(true);
		}

		// Verify drawer appears displaying the username
		for (int i = 0; i < customData.size(); i++) {
			sugar().users.searchSelect.assertContains(customData.get(i).get("username"), true);
		}

		// TODO: VOOD-1268 - Need lib support for records/rows count in Search and Select Drawer
		// Select an item on the list.
		new VoodooControl("input", "css", "div.flex-list-view-content tr:nth-child(2) input").click();
		VoodooUtils.waitForReady();

		// Verify original window displaying the proper selection
		sugar().accounts.recordView.getEditField("relAssignedTo").assertContains(customData.get(1).get("username"), true);
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}