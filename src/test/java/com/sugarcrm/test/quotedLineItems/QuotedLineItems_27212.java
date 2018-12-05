package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_27212 extends SugarTest {
	FieldSet qliData;

	public void setup() throws Exception {
		qliData = testData.get(testName).get(0);
		sugar().contacts.api.create();
		sugar().quotedLineItems.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that contact field is present and functional on QLI record view by default
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_27212_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.clickRecord(1);
		sugar().quotedLineItems.recordView.showMore();
		sugar().quotedLineItems.recordView.edit();

		// TODO: VOOD-1191
		new VoodooControl("div", "css", ".span6 div[data-name='contact_name']").assertEquals(qliData.get("contact_name_label"), true);
		sugar().quotedLineItems.recordView.getEditField("relContactName").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().quotedLineItems.recordView.save();

		// Verify that Contact Name field appears in record view by default
		sugar().quotedLineItems.recordView.getDetailField("relContactName").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);
		sugar().quotedLineItems.recordView.getDetailField("relContactName").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify contact record view and fullName field
		sugar().contacts.recordView.assertVisible(true);
		sugar().contacts.recordView.getDetailField("fullName").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}