package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_25402 extends SugarTest {

	public void setup() throws Exception {
		// Creating 6 contact records
		DataSource contactData = testData.get(testName);
		sugar().contacts.api.create(contactData);

		// Login as qauser
		FieldSet qaUser = sugar().users.getQAUser();
		sugar().login(qaUser);

		// Assign all contact records to qauser
		// TODO: VOOD-444 - Support creating relationships via API.
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("Assigned to", qaUser.get("userName"));
		sugar().contacts.massUpdate.performMassUpdate(massUpdateData);
	}

	/**
	 * Verify that column select pills works in Contact dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_25402_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to home
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// Click Configure on My Contacts Dashlet
		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl myContactsDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(3) .dashlet-container");
		VoodooControl configureBtnCtrl = new VoodooControl("i", "css", myContactsDashletCtrl.getHookString() + " .btn-toolbar .btn-group button i");
		configureBtnCtrl.click();

		// Edit
		VoodooControl editCtrl = new VoodooControl("a", "css", myContactsDashletCtrl.getHookString() + " .dropdown-menu li a");
		editCtrl.click();

		// Add all columns 
		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl selectFieldCtrl = new VoodooControl("ul", "css", ".select2-search-field.ui-sortable-handle");
		// Select all field names offered. 
		DataSource customData = testData.get(testName+"_customData");

		// Using xpath to select fields by names rather than using nth-child.
		for (int i = 0; i < customData.size() - 4; i++) {
			selectFieldCtrl.click();
			String fieldName = customData.get(i).get("field");
			new VoodooControl("li", "xpath", "//ul[contains(@class,'select2-results')]/li[not(contains(@class, 'select2-selected'))and div[text()='"+ fieldName +"']]").click();
		}

		// Save the Dashlet.
		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", "a[name='save_button']:not(.hide)");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify Multiple display columns are presented in dashlet
		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl tableHeadCtrl = new VoodooControl("thead", "css", ".row-fluid.sortable:nth-of-type(3) .dataTable thead");
		for (int i = 0; i < customData.size(); i++) {
			tableHeadCtrl.assertContains(customData.get(i).get("field"), true);
		}

		// Edit it again by delete some of the fields
		configureBtnCtrl.click();
		editCtrl.click();
		String columnNameSelector =  ".select2-choices.ui-sortable li:nth-child(%d) a";
		new VoodooControl("a", "css", String.format(columnNameSelector, 8)).click();
		new VoodooControl("a", "css", String.format(columnNameSelector, 7)).click();

		// Save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify deleted ones are removed
		tableHeadCtrl.assertContains(customData.get(2).get("field"), false);
		tableHeadCtrl.assertContains(customData.get(3).get("field"), false);

		// Edit it again by deleting all fields
		configureBtnCtrl.click();
		editCtrl.click();
		for (int i = customData.size() - 2; i > 0; i--) {
			new VoodooControl("a", "css", String.format(columnNameSelector, i)).click();
		}

		// Setting Display Rows to 5 to observe "More Contacts" link
		// TODO: VOOD-670 - More Dashlet Support
		new VoodooSelect("div", "css", ".edit.fld_limit div").set(customData.get(0).get("rowCount"));

		// Save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify Blank dashlet with only dashlet title i.e no column names are appearing
		for (int i = 0; i < customData.size(); i++) {
			myContactsDashletCtrl.assertContains(customData.get(i).get("field"), false);
		}

		// Verify "More Contacts" link is also displayed in My Contacts dashlet as the number of records is more than 5
		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl moreContactsLink = new VoodooControl("button", "css", ".row-fluid.sortable:nth-of-type(3) [data-action='show-more']");
		moreContactsLink.assertEquals(customData.get(0).get("moreContactsText"), true);
		moreContactsLink.assertAttribute("class", customData.get(0).get("class"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}