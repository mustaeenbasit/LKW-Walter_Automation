package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23552 extends SugarTest {
	ContactRecord con1;
	CaseRecord case1;

	public void setup() throws Exception {
		sugar().login();
		con1 = (ContactRecord) sugar().contacts.api.create();
		case1 = (CaseRecord) sugar().cases.api.create();
		con1.navToRecord();
	}

	/**
	 * Verify that a related case can be selected into a contact from contact
	 * record view
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23552_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		StandardSubpanel caseSub = new StandardSubpanel(sugar().cases);
		caseSub.clickLinkExisting();

		new VoodooControl("span", "css", ".fld_name.list").waitForVisible();
		new VoodooControl("input", "css", ".toggle-all").click();
		new VoodooControl("a", "css", ".fld_link_button a").click();

		// TODO VOOD-609
		new VoodooControl("a", "css",
				"div[data-voodoo-name='Cases'] span.fld_name.list a")
				.assertContains(case1.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
