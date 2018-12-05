package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21426 extends SugarTest {
	public void setup() throws Exception {
		// Login as an Admin user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * create_article_only_required_fields_set 
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21426_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto KB module and create new Article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();

		// Verify name field is marked required
		FieldSet myData = testData.get(testName).get(0);
		sugar().knowledgeBase.createDrawer.getEditField("name").assertAttribute("placeholder", myData.get("placeholder"));

		// Click Save without updating any fields
		sugar().knowledgeBase.createDrawer.save();

		// Verify error alert and close it
		sugar().alerts.getError().assertEquals(myData.get("alertError"), true);
		sugar().alerts.getError().closeAlert();

		// Verify input field exists within error element
		// TODO: VOOD-1755 support to assert error class on input fields in SideCar record view
		VoodooControl nameErrFieldCtrl = new VoodooControl("input", "css", ".headerpane .fld_name.edit.error input[name='name']");
		nameErrFieldCtrl.assertExists(true);

		// Verify input field text and border turn red due to error
		nameErrFieldCtrl.assertCssAttribute("color",myData.get("redColor"));
		nameErrFieldCtrl.assertCssAttribute("border-color", myData.get("redBorderColor"));

		// Verify input field's error icon, its color 
		// TODO: VOOD-1292
		VoodooControl errIconCtrl = new VoodooControl("i", "css", "span.fld_name.edit.error .fa.fa-exclamation-circle");
		errIconCtrl.assertExists(true);
		errIconCtrl.assertCssAttribute("color", myData.get("redColor"));

		// Verify tool tip appears on hover and its text 
		// TODO: VOOD-1292
		VoodooControl toolTipValueCtrl = new VoodooControl("div", "css", ".tooltip-inner");
		toolTipValueCtrl.assertVisible(false);
		errIconCtrl.hover();
		toolTipValueCtrl.assertVisible(true);
		toolTipValueCtrl.assertEquals(myData.get("tootipText"),true);

		// Update name field and save
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Assert success message
		sugar().alerts.getSuccess().assertEquals(myData.get("successAlert") + testName + ".", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}