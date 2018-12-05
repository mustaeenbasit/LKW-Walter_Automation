package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import org.junit.Assert;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29233 extends SugarTest {
	FieldSet myData = new FieldSet();
	VoodooControl configureSaveButton;

	public void setup() throws Exception {
		myData = testData.get(testName).get(0);
		sugar().login();
		
		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Go to KB configuration page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// TODO: VOOD-1762
		// Adding one more language in Configuration of KB
		new VoodooControl("button", "css", ".btn.first").click();
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) input[name='key_languages']")
			.set(myData.get("language_code"));
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) .controls.controls-three input[name='value_languages']")
			.set(myData.get("language_name"));
		configureSaveButton = new VoodooControl("a", "css", ".rowaction.btn.btn-primary");
		configureSaveButton.click();
		VoodooUtils.waitForReady(30000);
		sugar().logout();
	}

	/**
	 * Verify that saved localization or revision KB appear in the listview without manual refresh
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29233_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Log in as QAuser
		sugar().login(sugar().users.getQAUser());
				
		// QAuser creating KB Article from default data 
		sugar().knowledgeBase.create();
		
		// Creating Localization from list view action drop down
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		
		// TODO: VOOD-1760
		new VoodooControl("a", "css", ".list.fld_create_localization_button .rowaction").click();
		sugar().knowledgeBase.createDrawer.setField("name", myData.get("localizedName"));
		sugar().knowledgeBase.createDrawer.save();
		
		// Sort the list view in ascending order as order is not consistent
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		
		// Verifying Localization is created and showing in list view
		Assert.assertEquals("After Creating Localization total row count should be 2", 2, sugar().knowledgeBase.listView.countRows());
		new VoodooControl("td", "css", ".single:nth-child(2) .fld_language").assertEquals(myData.get("language_name"), true);
		
		// Creating Revision from list view action drop down
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		new VoodooControl("a", "css", ".list.fld_create_revision_button .rowaction").click();
		sugar().knowledgeBase.createDrawer.setField("name", myData.get("revisedName"));
		sugar().knowledgeBase.createDrawer.save();
		
		// Sort the list view in ascending order as order is not consistent
		sugar().knowledgeBase.listView.sortBy("headerName", true);
		
		// Verifying Revision is created and showing in list view
		new VoodooControl("td", "css", ".single:nth-child(1) .list.fld_name")
			.assertEquals(myData.get("revisedName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}