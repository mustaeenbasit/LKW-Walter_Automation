package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23505 extends SugarTest {
	ContactRecord con1;

	public void setup() throws Exception {
		sugar().login();
		con1 =  (ContactRecord) sugar().contacts.api.create();
		con1.navToRecord();
	}

	/**
	 *  Verify that duplicating contact can be canceled
	 *  @throws Exception
	 */
	@Test
	public void Contacts_23505_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().contacts.recordView.copy();
		sugar().contacts.createDrawer.getEditField("firstName").set(ds.get(0).get("firstname"));
		sugar().contacts.createDrawer.getEditField("lastName").set(ds.get(0).get("lastname"));
		sugar().contacts.createDrawer.cancel();

		// TODO VOOD-581
		new VoodooControl("span", "css", "span[data-fieldname='full_name']").assertContains(con1.get("salutation")+" "+con1.get("firstName")+" "+con1.get("lastName"),true);

		sugar().contacts.navToListView();
		new VoodooControl("div", "css", "div.flex-list-view-content").assertContains(ds.get(0).get("lastname"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}

}
