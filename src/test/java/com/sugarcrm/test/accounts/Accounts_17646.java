package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Accounts_17646 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Select and edit duplicate record when found duplicate records during create account
	 * @throws Exception
	 */
	@Test
	public void Accounts_17646_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet defaultData = sugar().accounts.getDefaultData();
		String accountName = defaultData.get("name");
		String workPhone = defaultData.get("workPhone");
		String updatedPhone = defaultData.get("fax");

		// Navigate to Accounts List View and Click Create
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();

		// Enter the name and WorkPhone Fields and save the Record
		VoodooControl editFieldName = sugar().accounts.createDrawer.getEditField("name");
		editFieldName.set(accountName.substring(0,7));
		VoodooControl editFieldWorkPhone = sugar().accounts.createDrawer.getEditField("workPhone");
		editFieldWorkPhone.set(workPhone);
		sugar().accounts.createDrawer.save();

		// Verify Duplicate Record Check Window Appears
		sugar().accounts.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);

		// Verify Original Account created first is shown in the Duplicate Window list
		// TODO: VOOD-513 -Need lib support for duplicate check panel
		new VoodooControl("span", "css", ".duplicates-selectedit .single .fld_name").assertEquals(accountName, true);

		// Verify Select and Preview button is shown
		new VoodooControl("a", "css", "a[data-event='list:dupecheck-list-select-edit:fire']").assertVisible(true);
		new VoodooControl("a", "css", "div[data-voodoo-name='dupecheck-list-edit'] a[data-event='list:preview:fire']").assertVisible(true);

		// Click the Select button
		sugar().accounts.createDrawer.selectAndEditDuplicate(1);

		// Verify the Account record Edit pane Appears with fields having value of Account created previously 
		editFieldName.assertEquals(accountName, true);
		editFieldWorkPhone.assertEquals(workPhone, true);

		// Update few fields in the edit pane of Account Record and Save the Record.
		editFieldName.set(testName);
		editFieldWorkPhone.set(updatedPhone);

		// Verify Updated Data is shown in the Edit Pane of Account Record
		editFieldName.assertEquals(testName, true);
		editFieldWorkPhone.assertEquals(updatedPhone, true);

		// TODO: VOOD-632 - Selectors fails if one drawer is created on top of other
		new VoodooControl("a", "css", ".drawer.active [name='select_button']").click();
		VoodooUtils.waitForReady();

		// Account List View Appears
		sugar().accounts.listView.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}