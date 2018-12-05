package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_18391 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar.login();
		myAccount = (AccountRecord) sugar.accounts.api.create();
	}

	/**
	 * check filters for activity stream at the Record and module list level
	 * 
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_18391_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Record view
		// TODO The JIRA story VOOD-712 which deals with Lib Support for filters
		// of activitystream
		myAccount.navToRecord();
		sugar.accounts.recordView.showActivityStream();
		VoodooUtils.pause(2000);
		new VoodooControl("div", "css", "div.select2.search-filter a").click();
		VoodooUtils.pause(2000);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(1)")
				.assertContains("All Activity Stream", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(2)")
				.assertContains("Messages for Create", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(3)")
				.assertContains("Messages for Link", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(4)")
				.assertContains("Messages for Post", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(5)")
				.assertContains("Messages for Unlink", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(6)")
				.assertContains("Messages for Update", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(1)")
				.click();

		// Module list view
		sugar.accounts.navToListView();
		sugar.accounts.listView.showActivityStream();
		new VoodooControl("div", "css", "div.select2.search-filter a").click();
		VoodooUtils.pause(2000);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(1)")
				.assertContains("All Activity Stream", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(2)")
				.assertContains("Messages for Create", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(3)")
				.assertContains("Messages for Link", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(4)")
				.assertContains("Messages for Post", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(5)")
				.assertContains("Messages for Unlink", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(6)")
				.assertContains("Messages for Update", true);
		new VoodooControl("li", "css", "li.select2-result:nth-of-type(1)")
				.click();


		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}