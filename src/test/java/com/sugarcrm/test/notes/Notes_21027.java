package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21027 extends SugarTest {

	public void setup() throws Exception {
		// Creating records for modules available in "Related To" dropdown
		sugar().contacts.api.create();
		sugar().opportunities.api.create();
		sugar().productCatalog.api.create();
		sugar().quotedLineItems.api.create();
		sugar().bugs.api.create();
		sugar().calls.api.create();
		sugar().targets.api.create();
		sugar().leads.api.create();
		sugar().knowledgeBase.api.create();
		sugar().revLineItems.api.create();
		sugar().quotes.api.create();
		sugar().cases.api.create();
		sugar().contracts.api.create();

		// Login as admin
		sugar().login();
	}

	/**
	 * Create Note_Verify that the note associated with each module listed in drop down list can be created without error displayed.
	 * Not covered modules Tasks, Meetings & Accounts in this test as Notes_29958, Notes_21086, Notes_17472 scripts exists for them. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21027_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource moduleNamesDS = testData.get(testName);
		VoodooControl subjectFieldEditCtrl = sugar().notes.createDrawer.getEditField("subject");
		VoodooControl subjectFieldListCtrl = sugar().notes.listView.getDetailField(1, "subject");
		VoodooControl relatedToModuleCtrl = sugar().notes.createDrawer.getEditField("relRelatedToModule");
		VoodooControl relatedToValueCtrl = sugar().notes.createDrawer.getEditField("relRelatedToValue");

		// Click "Create Note" link from navigation bar 
		sugar().navbar.selectMenuItem(sugar().notes, "createNote");

		for(int i = 0; i< moduleNamesDS.size(); i++) {
			//  Enter required field, "Subject"
			subjectFieldEditCtrl.set(testName + "_" + i);

			// Select a  module from Related to field 
			relatedToModuleCtrl.set(moduleNamesDS.get(i).get("moduleName"));
			relatedToValueCtrl.set(moduleNamesDS.get(i).get("value"));

			// Save
			sugar().notes.createDrawer.save();

			// Verify note can be saved without any error
			sugar().alerts.getError().assertVisible(false);

			// Verifying saved note
			subjectFieldListCtrl.assertEquals(testName + "_" + i, true);

			// Click "Create"
			sugar().notes.listView.create();
		}
	}

	public void cleanup() throws Exception {}
}