package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_18528 extends SugarTest {

	CaseRecord myCase;

	// Common VoodooControl and VoodooSelect references
	VoodooControl studioLinkCases, layoutsButton, listviewButton;
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();
		sugar().accounts.api.create();
		myCase = (CaseRecord)sugar().cases.create();
	}

	/**
	 * Verify the default List View for Cases is correct in Studio
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_18528_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();

		// TODO VOOD-517 Create Studio Module (BWC) - will provide the
		// references to replace these explicit VoodooControls
		studioLinkCases = new VoodooControl("a", "id", "studiolink_Cases");
		layoutsButton = new VoodooControl("a", "css", "td#layoutsBtn a");
		listviewButton = new VoodooControl("a", "css", "td#viewBtnlistview a");

		studioLinkCases.click();

		layoutsButton.click();

		listviewButton.click();

		// Verify Default panel should contain: Num, Subject, Account Name,
		// Priority, Status, Assigned to, Date Created
		for (int i = 0; i < ds.size(); i++) {
			new VoodooControl("td", "css", "td#Default td#subslot" + i + "label")
			.assertContains(ds.get(i).get("field_name"), true);
		}

		VoodooUtils.focusDefault();

		// Verify the columns on the List View should match the Studio defaults
		sugar().cases.navToListView();
		sugar().cases.listView.verifyField(1, "caseNumber", "");
		sugar().cases.listView.verifyField(1, "name", myCase.get("name"));
		sugar().cases.listView.verifyField(1, "relAccountName", myCase.get("relAccountName"));
		sugar().cases.listView.verifyField(1, "priority", myCase.get("priority"));
		sugar().cases.listView.verifyField(1, "status", myCase.get("status"));
		new VoodooControl("a", "css", ".fld_date_entered").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
