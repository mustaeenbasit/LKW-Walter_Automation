package com.sugarcrm.test.accounts;

import org.junit.Test;
import org.junit.Ignore;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17096 extends SugarTest {
	DataSource tData;
	
	// TODO: VOOD-1429	LoginScreen object: Need direct page access for footer buttons: Home, Mobile & Language
	VoodooControl languageList;

	//Setup- Deutsch language, an account with long description 
	public void setup() throws Exception {
		tData = testData.get(testName);
		FieldSet customData = new FieldSet();
		customData.put("description", tData.get(0).get("description"));
		sugar().accounts.api.create(customData);
		
		// TODO: VOOD-999 Need a unique, consistent hook value- that doesn't change when translated.
		languageList = new VoodooControl("span", "id", "languageList");
		VoodooUtils.waitForReady();
		languageList.click();
		new VoodooControl("a", "css", "a[data-lang-key='de_DE']").click();
		VoodooUtils.waitForReady();
		sugar().login();
	}

	/**
	 * Test Case 17096: Verify "...more" and "...less" in the textarea field should be language translatable in the preview
	 * @throws Exception
	 */
	@Ignore("Vood-1446")
	@Test
	public void Accounts_17096_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		 
		sugar().accounts.navToListView();
		
		// TODO: VOOD-999 
		new VoodooControl("span", "css", ".flex-list-view-content tr:nth-child(1) a[data-event='list:preview:fire']").click();
		VoodooControl checkBtnCtrl =  new VoodooControl("span", "css", ".preview-pane.active .btn-invisible.more.hide");
		VoodooControl showMoreBtnCtrl = new VoodooControl("span", "css", ".preview-pane.active .btn-invisible.more");
		VoodooControl showLessBtnCtrl = new VoodooControl("span", "css", ".preview-pane.active .btn-invisible.less");
		if (!checkBtnCtrl.queryVisible()) showMoreBtnCtrl.click();
		
		// TODO: VOOD-999 Need a unique, consistent hook value- that doesn't change when translated.
		VoodooControl descMoreLessToggleCtrl = new VoodooControl("button", "css", ".preview-data .detail.fld_description .btn.btn-link.btn-invisible.toggle-text");
		descMoreLessToggleCtrl.scrollIntoViewIfNeeded(false);
		descMoreLessToggleCtrl.assertEquals(tData.get(0).get("moreInDeutsch"), true);
		descMoreLessToggleCtrl.click();
		descMoreLessToggleCtrl.scrollIntoViewIfNeeded(false);
		descMoreLessToggleCtrl.assertEquals(tData.get(0).get("lessInDeutsch"), true);
		descMoreLessToggleCtrl.click();
		if (checkBtnCtrl.queryVisible()) showLessBtnCtrl.click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}