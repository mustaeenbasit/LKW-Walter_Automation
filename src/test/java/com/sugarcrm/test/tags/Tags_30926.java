package com.sugarcrm.test.tags;
import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Tags_30926 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Javascript alert should not be display while merge tag records.
	 * @throws Exception
	 */
	@Test
	public void Tags_30926_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to tags module
		sugar().navbar.selectMenuItem(sugar().tags, "createTag");

		// Creating tag with XSS script
		VoodooControl editNameControl = sugar().tags.createDrawer.getEditField("name");
		editNameControl.set(testData.get(testName).get(0).get("javascript_message"));
		sugar().tags.createDrawer.save();

		// Another simple tag creation
		sugar().tags.listView.create();
		editNameControl.set(testName);
		sugar().tags.createDrawer.save();

		// Merge 2 tag records
		sugar().tags.listView.toggleSelectAll();
		sugar().tags.listView.openActionDropdown();

		// TODO: VOOD-681
		new VoodooControl("a", "css", ".fld_merge_button.list").click();
		new VoodooControl("a", "css", ".merge-duplicates-headerpane a[name='save_button']").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify record should be merge without any java script dialog. 
		Assert.assertFalse("Javascript Dialog appears on merging records.", VoodooUtils.isDialogVisible());

		// Also, verify tags count and tag name on list view after merge
		Assert.assertTrue("Tags record not equals One, while merging records.", sugar().tags.listView.countRows() == 1);
		sugar().tags.listView.verifyField(1, "name", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}