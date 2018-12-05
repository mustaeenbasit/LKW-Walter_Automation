package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24301 extends SugarTest {
	OpportunityRecord myOpp;
	MeetingRecord myMeet;
	StandardSubpanel meetsSubpanel;
	DataSource meetDS;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		// TODO: Should be removed with sugar().meetings.api.create(); when SFA-2652 is fixed
		myMeet = (MeetingRecord) sugar().meetings.create();
		meetDS = testData.get(testName);

		// Open opportunity record view and link the created meeting
		myOpp.navToRecord();
		meetsSubpanel = sugar().opportunities.recordView.subpanels.get("Meetings");
		meetsSubpanel.clickLinkExisting();
		new VoodooControl("span", "css", ".fld_name.list").waitForVisible();
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * Test Case 24301: Edit Scheduled Meeting_Verify that scheduled meeting related to an opportunity
	 * can be inline-modified when using "Edit" button in Meetings subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24301_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Inline-edit related meeting
		meetsSubpanel.editRecord(1);

		/* Unable to use sugar().meetings.editView.getEditField("name").set();
		as subpanel edit view elements ids do not coincide with simple edit view elements
		*/

		new VoodooControl("div", "css", ".fld_name.edit input").set(meetDS.get(0).get("name"));
		meetsSubpanel.saveAction(1);
		VoodooUtils.waitForAlertExpiration();

		// Verify the meeting is successfully modified and visible in the meetings subpanel of the opportunity
		meetsSubpanel.verify(1, meetDS.get(0), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}