package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23544 extends SugarTest {
	ContactRecord con1;
	StandardSubpanel leadSub;
	DataSource ds;

	public void setup() throws Exception {
		con1 = (ContactRecord) sugar().contacts.api.create();
		ds = testData.get(testName);

		sugar().login();
		con1.navToRecord();
		leadSub= sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSub.hover();
		leadSub.addRecord();
		sugar().leads.createDrawer.getEditField("lastName").set(ds.get(0).get("lastName"));
		sugar().leads.createDrawer.save();
	}

	/**
	 * Verify that a related lead can be edited from contact record view
	 * @throws Exception
	 */
	@Test
	public void Contacts_23544_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		leadSub.hover();
		leadSub.editRecord(1);

		// TODO: VOOD-503 need lib support of all controls in subpanel inline edit form
		leadSub.getChildElement("input", "css", ".fld_first_name input").set(ds.get(0).get("firstName"));
		leadSub.getChildElement("input", "css", ".fld_last_name input").set(ds.get(0).get("lastName2"));
		// This is needed because in inline edit mode of lead record "Work phone" field is partially hidden behind RHS panel
		// so some scrolling is needed to bring it into view.
		new VoodooControl("div","css","div[data-subpanel-link='leads'] div.flex-list-view-content").scrollHorizontally(100);
		leadSub.getChildElement("input", "css", ".fld_phone_work input").set(ds.get(0).get("phoneWork"));

		leadSub.saveAction(1);

		// Verify the lead record is updated in the Leads sub-panel of Contact record view
		FieldSet fs = new FieldSet();
		fs.put("fullName", ds.get(0).get("firstName")+" "+ds.get(0).get("lastName2"));
		fs.put("phoneWork",ds.get(0).get("phoneWork"));
		leadSub.verify(1, fs, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
