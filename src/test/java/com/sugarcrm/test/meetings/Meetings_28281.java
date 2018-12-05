package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_28281 extends SugarTest {
	DataSource customData,itemsPerPage;

	public void setup() throws Exception {
		customData = testData.get(testName);
		itemsPerPage = testData.get(testName+"_Limit");
		sugar().meetings.api.create();
		sugar().contacts.api.create(customData);
		sugar().login();
		// Navigate to Admin > System Settings and changing the Subpanel limit to 3.
		sugar().admin.setSystemSettings(itemsPerPage.get(0));
	}

	/**
	 * Verify that number of guests are displaying correctly in preview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_28281_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating a meeting that has 6 guests totally
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Selecting contact

		// Selecting contact
		for (int count=2; count < customData.size(); count++)
		{
			sugar().meetings.recordView.clickAddInvitee();
			sugar().meetings.recordView.selectInvitee(customData.get(count).get("lastName"));
		}
		sugar().meetings.recordView.save();

		sugar().meetings.navToListView();
		// Preview the meeting from listview.
		sugar().meetings.listView.previewRecord(1);
		VoodooControl guestCtrl= new VoodooControl("div", "css", ".participants");
		VoodooControl moreCtrl = new VoodooControl("button", "css", ".btn.btn-link.btn-invisible.more");

		// Verify  "More Guests" link appear
		// TODO: VOOD-1354 Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView
		moreCtrl.assertVisible(true);

		//Verify "More Guests" link appear and allow to click to open. 3 more guests appear. Totally 6 guests.
		for (int count = 0; count < customData.size(); count++){
			switch (count) {
			case 3:
				moreCtrl.click();
				VoodooUtils.waitForReady();
				guestCtrl.assertContains(customData.get(count).get("lastName"), true);	
				System.out.println(customData.get(count).get("lastName"));
				break;
			case 6:
				moreCtrl.click();
				VoodooUtils.waitForReady();
				guestCtrl.assertContains(customData.get(count).get("lastName"), true);	
				System.out.println(customData.get(count).get("lastName"));
				break;
			default:
				guestCtrl.assertContains(customData.get(count).get("lastName"), true);
				System.out.println(customData.get(count).get("lastName"));
				break;
			}
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}