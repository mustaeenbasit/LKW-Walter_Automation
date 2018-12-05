package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20331 extends SugarTest {
	DataSource callData;

	public void setup() throws Exception {
		callData = testData.get(testName);
		sugar.login();

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Access 08:00 time slot and click Schedule Call from warning
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().clickLink(1);

		// Call Subject data and save.
		sugar.calls.createDrawer.getEditField("name").set(callData.get(0).get("name"));
		sugar.calls.createDrawer.getEditField("status").set(callData.get(0).get("status"));
		sugar.calls.createDrawer.getEditField("direction").set(callData.get(0).get("direction"));
		sugar.calls.createDrawer.getEditField("description").set(callData.get(0).get("description"));
		sugar.calls.createDrawer.save();
	}

	/**
	 * Edit call_Verify that an existing call can be edited
	 * @throws Exception
	 */
	@Test
	public void Calendar_20331_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click any call subject in Calendar view.
		// TODO: VOOD-863
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am'] div.head div:nth-child(2)").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("name").set(callData.get(1).get("name"));
		sugar.calls.recordView.getEditField("status").set(callData.get(1).get("status"));
		sugar.calls.recordView.getEditField("direction").set(callData.get(1).get("direction"));
		sugar.calls.recordView.getEditField("description").set(callData.get(1).get("description"));
		sugar.calls.recordView.getEditField("date_start_time").set(callData.get(1).get("date_start_time"));
		sugar.calls.recordView.save();

		// Verify that the Updated call information (Subject) is displayed in Calendar view.
		// TODO: VOOD-863
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am']").assertContains(callData.get(0).get("name"), false);
		new VoodooControl("div", "css", ".week div[time='09:00am'] div.head div:nth-child(2)").assertContains(callData.get(1).get("name"), true);
		new VoodooControl("div", "css", ".week div[time='09:00am'] .head .adicon").click();
		VoodooUtils.waitForReady();

		// Verify the Call subject at the Additional Detail page
		// TODO: VOOD-863
		VoodooControl additionalDetailPageValues = new VoodooControl("div", "css", ".ui-dialog-content");
		additionalDetailPageValues.assertContains(callData.get(1).get("name"), true);
		additionalDetailPageValues.assertContains(callData.get(1).get("status"), true);
		additionalDetailPageValues.assertContains(callData.get(1).get("description"), true);
		additionalDetailPageValues.assertContains(callData.get(1).get("date_start_time"), true);

		// Click on the view icon on the Additional Detail page
		// TODO: VOOD-863
		new VoodooControl("a", "css", ".ui-dialog-title a[title='View']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that the Call detail information is displayed on "Calls Detail View" page.
		sugar.calls.recordView.getDetailField("name").assertEquals(callData.get(1).get("name"), true);
		sugar.calls.recordView.getDetailField("status").assertEquals(callData.get(1).get("status"), true);
		sugar.calls.recordView.getDetailField("direction").assertEquals(callData.get(1).get("direction"), true);
		sugar.calls.recordView.getDetailField("description").assertEquals(callData.get(1).get("description"), true);
		sugar.calls.recordView.getDetailField("date_start_time").assertContains(callData.get(1).get("date_start_time"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}