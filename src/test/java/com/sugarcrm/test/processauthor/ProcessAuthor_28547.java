package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_28547 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify module fields are shown on the "Fields selector" page for process email template
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_28547_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource accountFieldName = testData.get(testName);
		String moduleName = sugar().accounts.moduleNamePlural;

		// Create a Process Email template and navigate to its Design view
		sugar().navbar.navToModule(sugar().processEmailTemplates.moduleNamePlural);
		sugar().processEmailTemplates.listView.create();
		sugar().processEmailTemplates.createDrawer.getEditField("name").set(sugar().processEmailTemplates.getDefaultData().get("name"));
		sugar().processEmailTemplates.createDrawer.saveAndDesign();
		VoodooUtils.waitForReady();

		// Click the Gear icon (Field Selector button) i.e in the extreme right of the Subject field.
		sugar.processEmailTemplates.designView.getControl("subjectSelector").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1859
		// Controls on the Field Selector Drawer
		VoodooControl showMore = new VoodooControl("button", "css", "div[data-voodoo-name='compose-varbook-list-bottom'] .more");
		VoodooControl cancelLink = new VoodooControl("a", "css", ".active span[data-voodoo-name='cancel_button'] a");

		// Scroll to and click the "show more variables" link in the Field Selector Drawer
		showMore.scrollIntoViewIfNeeded(true);
		VoodooUtils.waitForReady();

		// Assert that a list of fields is being displayed on the Field selector page.
		for(int i=1; i<=accountFieldName.size(); i++){
			// Assert the field name
			new VoodooControl("div", "css", ".flex-list-view .single:nth-child(" + i +") td:nth-child(2) div").
			assertEquals(accountFieldName.get(i-1).get("fieldName"), true);

			// Assert the module name
			new VoodooControl("div", "css", ".flex-list-view .single:nth-child(" + i +") td:nth-child(3) div").
			assertEquals(moduleName, true);
		}

		// Click cancel link to navigate back to the design view of Process Email Template
		cancelLink.click();
		VoodooUtils.waitForReady();

		// Click the Gear icon (Field Selector button) on the Email Body tool bar
		sugar.processEmailTemplates.designView.getControl("fieldsSelector").click();
		VoodooUtils.waitForReady();

		// Scroll to and click the "show more variables" link
		showMore.scrollIntoViewIfNeeded(true);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1859
		// Assert that a list of fields is being displayed on the Field selector page.
		for(int i=1; i<=accountFieldName.size(); i++){
			// Assert the field name
			new VoodooControl("div", "css", ".flex-list-view .single:nth-child(" + i +") td:nth-child(2) div").
			assertEquals(accountFieldName.get(i-1).get("fieldName"), true);

			// Assert the module name
			new VoodooControl("div", "css", ".flex-list-view .single:nth-child(" + i +") td:nth-child(3) div").
			assertEquals(moduleName, true);
		}

		// Click cancel link to navigate back to the design view of Process Email Template
		cancelLink.click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
