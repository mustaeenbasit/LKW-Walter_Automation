package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_26981 extends SugarTest {
	VoodooControl historicalSummaryCtrl, columnsSettingsCtrl, resetDefaultCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the stickiness on the Historical Summary page 
	 * @throws Exception
	 */
	@Test
	public void Accounts_26981_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet assertionData = testData.get(testName).get(0);

		// Go to one of the modules with activities (Accounts), click on a record to go to the record view.Click the actions drop down
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-965 : Support for Historical Summary page
		historicalSummaryCtrl = new VoodooControl("a", "css", ".fld_historical_summary_button.detail a");
		columnsSettingsCtrl = new VoodooControl("i", "css", ".nosort.morecol button i");
		resetDefaultCtrl = new VoodooControl("button", "css", ".nosort.morecol .dropdown-menu [data-columns-order='reset']");
		VoodooControl emaiToCtrl = new VoodooControl("span", "css", "th[data-fieldname = 'to_addrs'] span");
		VoodooControl hideAndDisplayEmaitToCtrl = new VoodooControl("button", "css", ".nosort.morecol .dropdown-menu [data-field-toggle='to_addrs']");
		VoodooControl typeHeaderCtrl = new VoodooControl("th", "css", ".reorderable-columns th:nth-child(1)");
		VoodooControl subjectHeaderCtrl = new VoodooControl("th", "css", ".reorderable-columns th:nth-child(2)");
		VoodooControl closeHistoricalSummaryCtrl = new VoodooControl("a", "css", ".history-summary-headerpane a[name='cancel_button']");

		// Select Historical Summary and verify that the "Email To" field is shown in header
		historicalSummaryCtrl.click();
		VoodooUtils.waitForReady();
		emaiToCtrl.assertContains(assertionData.get("emailTo"), true);

		// Click the "Setting" icon then uncheck "Email To" (or any column).
		columnsSettingsCtrl.click();
		hideAndDisplayEmaitToCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the "Email To" column hides
		emaiToCtrl.assertVisible(false);

		// Reorder the columns in a different way
		// TODO: VOOD-965
		typeHeaderCtrl.dragNDrop(new VoodooControl("div", "css", ".reorderable-columns th:nth-child(3) .th-droppable-placeholder.ui-droppable"));

		// Click the "Close" button to close the drawer
		closeHistoricalSummaryCtrl.click();
		VoodooUtils.waitForReady();

		// Click the actions drop down and reselect "Historical Summary"
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		historicalSummaryCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that "Email To" remains hidden and your last state was saved
		emaiToCtrl.assertVisible(false);

		// Verify that reordered columns state has been saved
		subjectHeaderCtrl.assertEquals(assertionData.get("type"), true);
		typeHeaderCtrl.assertEquals(assertionData.get("subject"), true);

		// Click the "Close" button to close the drawer
		closeHistoricalSummaryCtrl.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}