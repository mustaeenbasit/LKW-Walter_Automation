package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23543 extends SugarTest {
	ContactRecord con1;

	public void setup() throws Exception {
		sugar().login();
		con1 = (ContactRecord) sugar().contacts.api.create();
		con1.navToRecord();
        }

        /**
         * Verify that a related lead can be created in lead subpanel from contact record view
         * @throws Exception
         */
        @Test
        public void Contacts_23543_execute() throws Exception {
        	VoodooUtils.voodoo.log.info("Running " + testName + "...");

        	DataSource ds = testData.get(testName);
        	StandardSubpanel leadSub = new StandardSubpanel(sugar().leads);
        	leadSub.addRecord();
        	sugar().leads.createDrawer.getEditField("lastName").set(ds.get(0).get("lastName"));
        	sugar().leads.createDrawer.save();
        	// TODO VOOD-609
        	new VoodooControl("a", "css", "div[data-voodoo-name='Leads'] td[data-type='fullname'] a").assertContains(ds.get(0).get("lastName"), true);

        	VoodooUtils.voodoo.log.info(testName + " complete.");
        }

        public void cleanup() throws Exception {}
}
