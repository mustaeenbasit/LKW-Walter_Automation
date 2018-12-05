package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class MassUpdateTests extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
		sugar().leads.navToListView();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();
		sugar().leads.listView.massUpdate();

		// Verify add icon for First row
		VoodooControl addRow = sugar().leads.massUpdate.getControl(String.format("addField%02d", 2));
		addRow.assertVisible(true);
		VoodooControl cancelUpdate = sugar().leads.massUpdate.getControl("cancelUpdate");

		// Verify basic dropdown field and value elements
		sugar().leads.massUpdate.getControl(String.format("massUpdateField%02d", 2)).assertVisible(true);
		sugar().leads.massUpdate.getControl(String.format("massUpdateValue%02d", 2)).assertVisible(true);

		addRow.click();
		sugar().leads.massUpdate.getControl(String.format("addField%02d", 3)).assertVisible(true);

		// Verify remove icon for 2nd and 3rd row
		VoodooControl removeRow = sugar().leads.massUpdate.getControl(String.format("removeField%02d", 2));
		removeRow.assertVisible(true);
		sugar().leads.massUpdate.getControl(String.format("removeField%02d", 3)).assertVisible(true);

		// Verify cancel and update buttons
		cancelUpdate.assertVisible(true);
		sugar().leads.massUpdate.getControl("update").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyElements() complete.");
	}

	public void cleanup() throws Exception {}
}