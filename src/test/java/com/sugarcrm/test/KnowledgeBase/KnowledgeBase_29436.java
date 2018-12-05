package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29436 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Should not allow mass update status to Approved when Published Date is past
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29436_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		FieldSet customFS = testData.get(testName).get(0);
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customFS.get("kb_name"));
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("date_publish").set(customFS.get("publishDate"));
		sugar().knowledgeBase.createDrawer.save();

		// Mass Update change status=Approved.
		sugar().knowledgeBase.listView.checkRecord(1);
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();

		// Select Teams from fields drop down
		sugar().knowledgeBase.massUpdate.getControl("massUpdateField02").set(customFS.get("selectStatusKey"));
		VoodooControl massUpdate = sugar().knowledgeBase.massUpdate.getControl("massUpdateValue02");
		massUpdate.set(customFS.get("statusApproved"));
		sugar().knowledgeBase.massUpdate.getControl("update").click();

		// Verify that a red color error message - The Publish Date must occur on a later date than today's date. appears
		VoodooControl errorMessageCtrl = new VoodooControl("span", "css", ".massupdate.fld_status.error .help-block");
		errorMessageCtrl.assertContains(customFS.get("warningMessage"), true);

		// Cancel Mass Update
		sugar().knowledgeBase.massUpdate.getControl("cancelUpdate").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}