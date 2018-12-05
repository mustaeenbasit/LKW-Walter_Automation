package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27286 extends SugarTest {
	FieldSet customData;
		
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Contact and Lead don't have availabilities colors in meeting scheduler.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27286_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		
		// Selecting contact
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().contacts.getDefaultData().get("lastName"));		

		// Selecting Lead
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().leads.getDefaultData().get("lastName"));

		VoodooUtils.voodoo.log.info("Contacts color: " + new VoodooControl("div", "css", ".participants-schedule [data-module='Contacts'] .cell.unavailable > div:nth-child(2)").getCssAttribute("background-color"));
		VoodooUtils.voodoo.log.info("Leads color: " + new VoodooControl("div", "css", ".participants-schedule [data-module='Leads'] .cell.unavailable > div:nth-child(2)").getCssAttribute("background-color"));
		
		// Verify light grey color for contact and lead, which means Schedule is unavailable.
		new VoodooControl("div", "css", ".participants-schedule [data-module='Contacts'] .cell.unavailable > div:nth-child(2)").assertCssAttribute("background-color", "rgba(0, 0, 0, 0.0980392)");
		new VoodooControl("div", "css", ".participants-schedule [data-module='Leads'] .cell.unavailable > div:nth-child(2)").assertCssAttribute("background-color", "rgba(0, 0, 0, 0.0980392)");

		// Verify that Schedule is unavailable for Contact and Lead. 
		// TODO: VOOD-1223
		new VoodooControl("div", "css", ".participants-schedule [data-module='Contacts'] .cell.unavailable > div:nth-child(2)").assertContains(customData.get("assert_text"), true);
		new VoodooControl("div", "css", ".participants-schedule [data-module='Leads'] .cell.unavailable > div:nth-child(2)").assertContains(customData.get("assert_text"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}