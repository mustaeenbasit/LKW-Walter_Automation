package com.sugarcrm.test.tags;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tags_29836 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify Unlink records option no longer be available in the subpanels of a Tag Record
	 * @throws Exception
	 */
	@Test
	public void Tags_29836_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet tagsData = testData.get(testName).get(0);

		// Navigating to Contacts record view and add a tag to it.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("tags").set(testName);
		sugar().contacts.recordView.save();

		// Navigating to Tags record view 
		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(1);

		// Expanding contacts subpanel and asserting the record present in the subpanel
		StandardSubpanel contactSubpanel = (StandardSubpanel) sugar().tags.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.expandSubpanel();
		contactSubpanel.getDetailField(1, "fullName" ).assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		// Asserting the values in rowaction dropdown of contact record.
		// TODO: VOOD-568
		contactSubpanel.expandSubpanelRowActions(1);
		VoodooControl dropdownMenu = new VoodooControl("ul", "css", ".fieldset-field .dropdown-menu");
		dropdownMenu.assertContains(tagsData.get("rowactionMenuItem"), true);
		dropdownMenu.assertContains(tagsData.get("unlinkOption"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}