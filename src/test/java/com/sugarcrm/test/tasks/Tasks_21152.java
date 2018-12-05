package com.sugarcrm.test.tasks;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21152 extends SugarTest {
	DataSource filterFields;

	public void setup() throws Exception {
		filterFields = testData.get(testName);
		FieldSet contactCreate = new FieldSet();

		// Creating 2 different Contacts
		sugar.contacts.api.create();		

		contactCreate.put("lastName", testName);
		sugar.contacts.api.create(contactCreate);
		contactCreate.clear();

		// Creating 2 different Tasks
		sugar.tasks.api.create();

		contactCreate.put("subject", testName);
		sugar.tasks.api.create(contactCreate);

		sugar.login();

		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);

		// Associating the 2 different Contacts with 2 different Tasks 
		sugar.tasks.listView.editRecord(1);
		sugar.tasks.listView.getEditField(1, "contactName").set(sugar.contacts.getDefaultData().get("lastName"));
		sugar.tasks.listView.saveRecord(1);

		sugar.tasks.listView.editRecord(2);
		sugar.tasks.listView.getEditField(2, "contactName").set(testName);
		sugar.tasks.listView.saveRecord(2);
	}

	/**
	 * Search Tasks: Verify that tasks can be searched by "Subject" and "Contact" 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21152_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Filter
		sugar.tasks.listView.openFilterDropdown();
		sugar.tasks.listView.selectFilterCreateNew();

		// TODO: VOOD-1797
		// Condition 1 : Subject
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterFields.get(0).get("filterBy"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterFields.get(0).get("filterOperator"));
		new VoodooControl("input", "css", ".fld_name input").set(testName);
		new VoodooControl("button", "css", ".filter-actions button[data-action='add']").click();
		VoodooUtils.waitForReady();

		// Condition 2 : Contact Name
		new VoodooSelect("span", "css", ".filter-body:nth-child(2) .detail.fld_filter_row_name").set(filterFields.get(1).get("filterBy"));
		new VoodooSelect("span", "css", ".filter-body:nth-child(2) .detail.fld_filter_row_operator").set(filterFields.get(1).get("filterOperator"));
		new VoodooSelect("div", "css", ".filter-body .fld_contact_name div").click();
		
		// TODO: VOOD-1648
		new VoodooControl("div", "css", "#select2-drop div").click();
		
		sugar.contacts.searchSelect.selectRecord(2);
		new VoodooControl("a", "css", "span[data-voodoo-name='select_button'] a").click();
		VoodooUtils.waitForReady();
		
		// Name the filter
		new VoodooControl("input", "css", ".layout_Tasks .filter-header input").set(testName);
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".layout_Tasks .filter-header .save_button").click();
		VoodooUtils.waitForReady();

		// Getting the text of the filter applied (i.e the filter label)
		String appliedFilterlabel = new VoodooControl("span", "css", ".layout_Tasks .choice-filter-label").getText().trim();

		// Asserting the filter label when filter has been applied
		Assert.assertTrue("The filter has not been applied!!", appliedFilterlabel.equals(testName));

		// Verifying that only those Task records are displayed i.e matching the search criteria (subject and ContactName)
		sugar.tasks.listView.verifyField(1, "subject", testName);
		sugar.tasks.listView.verifyField(1, "contactName", sugar.contacts.getDefaultData().get("fullName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}