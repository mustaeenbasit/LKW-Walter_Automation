package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30969 extends SugarTest {

	public void setup() throws Exception {
		FieldSet kbData = new FieldSet();
		String pastDate = DateTime.now().minusDays(1).toString("MM/dd/yyyy");
		kbData.put("date_expiration", pastDate);
		kbData.put("date_publish", pastDate);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().knowledgeBase.create(kbData);
	}

	/**
	 * Verify that Error field should be in red when red message "Error Please resolve any errors before proceeding." appears
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30969_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet kbTestData = testData.get(testName).get(0);
		// Change the status Draft to Published
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.getEditField("status").set(kbTestData.get("status"));
		sugar().knowledgeBase.recordView.save();

		// Verifying Error is appearing when save record
		sugar().alerts.getAlert().assertEquals(kbTestData.get("recordViewError"), true);
		sugar().alerts.getAlert().assertCssAttribute("background-color", kbTestData.get("errorColor"));
		sugar().alerts.getAlert().closeAlert();
		
		// Verifying tooltip text of Publish date is not visible before hover to tooltip.
		// TODO: VOOD-1292
		VoodooControl toolTipText = new VoodooControl("div", "css", ".tooltip.fade.top.in .tooltip-inner");
		VoodooControl publishDatetoolTipCtrl = new VoodooControl("span", "css", ".fld_active_date.edit.error .error-tooltip.add-on i");
		toolTipText.assertVisible(false);
		
		// Verifying publish date field is in red color
		publishDatetoolTipCtrl.assertCssAttribute("color", kbTestData.get("errorColor"));
		
		// hover to tooltip of publish date
		publishDatetoolTipCtrl.hover();
		VoodooUtils.waitForReady();
		
		// Verifying text of publish date after hovering to tooltip
		toolTipText.assertEquals(kbTestData.get("publishDateToolTipError"), true);
		
		// Verifying tooltip text of expire date is not visible before hover to tooltip.
		VoodooControl expDatetoolTipCtrl = new VoodooControl("span", "css", ".fld_exp_date.edit.error .error-tooltip.add-on i");
		
		// Verifying expire date field is in red color
		expDatetoolTipCtrl.assertCssAttribute("color", kbTestData.get("errorColor"));
		
		// hover to tooltip of expire date
		expDatetoolTipCtrl.hover();
		VoodooUtils.waitForReady();
		
		// Verifying text of expire date after hovering to tooltip
		toolTipText.assertEquals(kbTestData.get("expDateToolTipError"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}