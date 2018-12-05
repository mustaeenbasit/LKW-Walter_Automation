package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_23115 extends SugarTest {
	DataSource Accounts;
	FieldSet firstAccount, secondAccount;

	public void setup() throws Exception {
		Accounts = testData.get("Accounts_23115");
		firstAccount = Accounts.get(0);
		secondAccount = Accounts.get(1);
		sugar().login();
	}

	/**
	 * Verify that the selected record is set as primary after dragging the
	 * "Primary" header when merging duplicates.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_23115_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		sugar().accounts.api.create(firstAccount);
		sugar().accounts.api.create(secondAccount);
		sugar().accounts.navToListView();
		// Verify that only 2 records are visible, before merge, using the
		// visible check boxes
		// TODO VOOD-697
		new VoodooControl("input", "css",
				"div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) input")
				.assertVisible(true);
		new VoodooControl("input", "css",
				"div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(3) input")
				.assertVisible(false);
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		// TODO VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list").click();

		// Drag and Drop the Primary heading to the second column
		new VoodooControl("div", "css", ".col.primary-edit-mode .primary-lbl")
				.dragNDrop(new VoodooControl("div", "css",
						".col:nth-of-type(2) .primary-lbl.ui-sortable"));
		sugar().alerts.getAlert().confirmAlert();

		new VoodooControl("a", "css",
				".fld_save_button.merge-duplicates-headerpane a").click();
		sugar().alerts.getAlert().confirmAlert();

		new VoodooControl("a", "css", ".fld_name.list").waitForVisible();
		// The old screen shows for a split second, safer to do a refresh here
		// before the assert
		VoodooUtils.refresh();
		sugar().accounts.listView.verifyField(1, "name",
				firstAccount.get("name"));
		sugar().accounts.listView.verifyField(1, "workPhone",
				firstAccount.get("workPhone"));
		// Verify that only 1 record is visible, after merge, using the visible
		// check boxes
		// TODO VOOD-697
		new VoodooControl("input", "css",
				"div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(1) input")
				.assertVisible(true);
		new VoodooControl("input", "css",
				"div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) input")
				.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
