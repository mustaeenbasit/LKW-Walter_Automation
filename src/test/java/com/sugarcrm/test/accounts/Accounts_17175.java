package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17175 extends SugarTest {
	AccountRecord accountRecord;
	ContactRecord contactRecord;
	StandardSubpanel contactSubpanel;
	FieldSet contactFieldSetRecord;

	public void setup() throws Exception {
		sugar().login();
		accountRecord = (AccountRecord) sugar().accounts.api.create();
		contactRecord = (ContactRecord) sugar().contacts.api.create();
		contactFieldSetRecord = new FieldSet();
		contactFieldSetRecord.put("fullName", contactRecord.get("fullName"));
	}

	/**
	 * Verify panel row level actions on subpanels
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_17175_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		accountRecord.navToRecord();
		contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.clickLinkExisting();
		// TODO VOOD-726 : need defined controls on linking existing record window
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		sugar().alerts.getSuccess().closeAlert();
		contactSubpanel.verify(1, contactFieldSetRecord, true);

		contactSubpanel.clickPreview(1);
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("span", "css", "span[data-voodoo-name='full_name']").assertContains(contactRecord.get("fullName"), true);
		new VoodooControl("span", "css", "span[data-voodoo-name='title']").assertContains(contactRecord.get("title"), true);

		DataSource ds = testData.get(testName);
		contactSubpanel.editRecord(1);
		contactFieldSetRecord.put("email", ds.get(0).get("email"));
		// TODO VOOD-503: First pass: web performance testing, VOOD-866: Need Lib support to provide inline edit email address field under Contacts subpanel
		new VoodooControl("input", "css", ".fld_email.edit input").set(contactFieldSetRecord.get("email"));
		contactSubpanel.saveAction(1);
		sugar().alerts.waitForLoadingExpiration();
		contactSubpanel.verify(1, contactFieldSetRecord, true);

		contactSubpanel.unlinkRecord(1);
		sugar().alerts.confirmAllAlerts();
		contactSubpanel.assertContains(contactFieldSetRecord.get("email"), false);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
