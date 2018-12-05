package com.sugarcrm.test.cases;

import org.junit.Test;
import org.openqa.selenium.Keys;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_28112 extends SugarTest {
	FieldSet customData;
	CaseRecord myCase;

	public void setup() throws Exception {
		myCase = (CaseRecord)sugar().cases.api.create();
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that Changing focus from a Text area field doesn't causes changes to disappear.
	 * @throws Exception
	 */
	@Test
	public void Cases_28112_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to any Case record view
		myCase.navToRecord();

		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Inline Edit
		// TODO: VOOD-854
		VoodooControl descriptionCtrl = new VoodooControl ("span", "css", "span[data-voodoo-name='description']");
		descriptionCtrl.hover();
		VoodooControl pencilCtrl = new VoodooControl ("i", "css", "span[data-name='description'] .fa.fa-pencil");
		pencilCtrl.click();
		sugar().cases.recordView.getEditField("description").set(customData.get("descriptionText1"));

		// Change the focus
		// TODO: VOOD-1437
		new VoodooControl ("span", "css", "span[data-voodoo-name='portal_viewable']").hover();
		new VoodooControl ("i", "css", "span[data-name='portal_viewable'] .fa.fa-pencil").click();
		new VoodooControl("span", "css" ,".fld_portal_viewable.edit").click();

		// Inline edit the description field again
		sugar().cases.recordView.getEditField("description").set(customData.get("descriptionText2"));

		// Inline edit the priority field
		new VoodooControl ("span", "css", "span[data-voodoo-name='priority']").hover();
		new VoodooControl ("i", "css", "span[data-name='priority'] .fa.fa-pencil").click();
		new VoodooControl("li", "css", "#select2-drop ul li:nth-child(1)").click();
		new VoodooControl("div", "css", ".record-label[data-name='priority']").click();

		// Verify description field remain untouched and text entered remains visible
		sugar().cases.recordView.getEditField("description").assertAttribute("value", customData.get("descriptionText1"));
		sugar().cases.recordView.getEditField("description").assertAttribute("value", customData.get("descriptionText2"));

		// Cancel
		sugar().cases.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
