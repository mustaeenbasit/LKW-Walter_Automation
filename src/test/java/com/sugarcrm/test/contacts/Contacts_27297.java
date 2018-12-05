package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contacts_27297 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that record edit mode fields treated like edit mode
	 * @throws Exception
	 */
	@Test
	public void Contacts_27297_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verify title and department fields inline edit in record-view
		// TODO: VOOD-854
		sugar().contacts.recordView.getDetailField("title").hover();
		VoodooControl titlePencilIcon = new VoodooControl ("i", "css", "span[data-name='title'] .fa.fa-pencil");
		titlePencilIcon.assertVisible(true);

		sugar().contacts.recordView.getDetailField("department").hover();
		VoodooControl departmentPencilIcon = new VoodooControl ("i", "css", "span[data-name='department'] .fa.fa-pencil");
		departmentPencilIcon.assertVisible(true);

		// Edit record and Verify no pencil icon on title and department fields and should stay editable
		sugar().contacts.recordView.edit();
		VoodooControl titleEditFieldCtrl = sugar().contacts.recordView.getEditField("title");
		titleEditFieldCtrl.click();
		titlePencilIcon.assertVisible(false);
		titleEditFieldCtrl.set(testName);
		titleEditFieldCtrl.assertEquals(sugar().contacts.defaultData.get("title"), false);

		VoodooControl departmentEditFieldCtrl = sugar().contacts.recordView.getEditField("department");
		departmentEditFieldCtrl.click();
		departmentPencilIcon.assertVisible(false);
		departmentEditFieldCtrl.set(testName);
		departmentEditFieldCtrl.assertEquals(sugar().contacts.defaultData.get("department"), false);

		sugar().contacts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
