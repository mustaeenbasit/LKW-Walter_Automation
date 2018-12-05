package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Emails_20660 extends SugarTest {
	DataSource customData;
	VoodooControl settingsButton, signatureName, sigTextCode, htmlSource, insertBtnCtrl, bodyCtrl, selectAllEditorCtrl, editorLinkIconCtrl;
	VoodooControl editorImgIconCtrl, setImgUrl, cancelBtnCtrl, saveBtnCtrl, deleteBtnCtrl, linkTextSet, editorTableIconCtrl;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar.login();
	}

	/**
	 * verify signature setting for emails
	 * @throws Exception
	 */
	@Test
	public void Emails_20660_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		// TODO: VOOD-1114 -Library support needed for controls in Emails > Settings > General > create(Signature)
		settingsButton = new VoodooControl("button", "id", "settingsButton");
		signatureName = new VoodooControl("input", "css", "input[id='name']");
		sigTextCode = new VoodooControl("a", "css", "#sigText_code");
		htmlSource = new VoodooControl("textarea", "css", "#htmlSource");
		insertBtnCtrl = new VoodooControl("input", "css", ".mceActionPanel #insert");
		bodyCtrl = new VoodooControl("body", "css", "body");
		selectAllEditorCtrl = new VoodooControl("a", "css", "#sigText_selectall");
		editorLinkIconCtrl = new VoodooControl("a", "css", "#sigText_link");
		linkTextSet = new VoodooControl("a", "css", "#href");
		editorTableIconCtrl = new VoodooControl("a", "css", "#sigText_table");
		editorImgIconCtrl = new VoodooControl("a", "css", "#sigText_image");
		setImgUrl = new VoodooControl("input", "css", "#image #src");
		cancelBtnCtrl = new VoodooControl("input", "css", "input[title='Cancel']");
		saveBtnCtrl = new VoodooControl("input", "css", "input[title='Save']");
		deleteBtnCtrl = new VoodooControl("input", "css", "#delete_sig input");
		
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		settingsButton.click();
		
		// Cancel signature
		for(int i = 0; i < customData.size(); i++) {
			// TODO: VOOD-672
			new VoodooControl("a", "css", "input[value='Create']").click();
			VoodooUtils.focusWindow(1);
			sugar.alerts.waitForLoadingExpiration();
			signatureName.set(customData.get(i).get("Dataset_5")); // add signature name
			
			if(i == 0) {
				// link to a text
				sigTextCode.click();
				VoodooUtils.focusWindow("HTML Source Editor");
				htmlSource.set("testing");
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();		
				sugar.alerts.waitForLoadingExpiration();
			
				// Select all text
				selectAllEditorCtrl.click();
				// Click on link icon
				editorLinkIconCtrl.click();
				VoodooUtils.focusWindow(2);
				linkTextSet.set(customData.get(i).get("Dataset_5"));
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();
			} else if(i == 1) {
				// insert photo
				editorImgIconCtrl.click();
				VoodooUtils.focusWindow(2);
				setImgUrl.set(customData.get(i).get("Dataset_6"));
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();
			} else if(i == 3) {
				// insert table
				editorTableIconCtrl.click(); 
				VoodooUtils.focusWindow(2);
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();
			} else {
				sigTextCode.click();
				VoodooUtils.focusWindow("HTML Source Editor");
				htmlSource.set(customData.get(i).get("Dataset_5"));
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();		
				sugar.alerts.waitForLoadingExpiration();
			}
			
			// For save signature
			cancelBtnCtrl.click();		
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");			
			
			// TODO: VOOD-1114 -Library support needed for controls in Emails > Settings > General > create(Signature)
			// Verify that operation of creating signature is canceled.
			new VoodooControl("select", "name", "signature_id").assertContains(customData.get(i).get("Dataset_5"), false);	
		}
		
		// Save signature
		for(int i = 0; i < customData.size(); i++) {
			// TODO: VOOD-672
			new VoodooControl("a", "css", "input[value='Create']").click();
			VoodooUtils.focusWindow(1);
			sugar.alerts.waitForLoadingExpiration();
			signatureName.set(customData.get(i).get("Dataset_5")); // add signature name
			
			if(i == 0) {
				// link to a text
				sigTextCode.click();
				VoodooUtils.focusWindow("HTML Source Editor");
				htmlSource.set("testing");
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();		
				sugar.alerts.waitForLoadingExpiration();
			
				// Select all text
				selectAllEditorCtrl.click();
				// Click on link icon
				editorLinkIconCtrl.click();
				VoodooUtils.focusWindow(2);
				linkTextSet.set(customData.get(i).get("Dataset_5"));
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();
			} else if(i == 1) {
				// insert photo
				editorImgIconCtrl.click();
				VoodooUtils.focusWindow(2);
				setImgUrl.set(customData.get(i).get("Dataset_6"));
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();
			} else if(i == 3) {
				// insert table
				editorTableIconCtrl.click(); 
				VoodooUtils.focusWindow(2);
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();
			} else {
				sigTextCode.click();
				VoodooUtils.focusWindow("HTML Source Editor");
				htmlSource.set(customData.get(i).get("Dataset_5"));
				insertBtnCtrl.click();
				VoodooUtils.focusWindow(1);
				bodyCtrl.click();		
				sugar.alerts.waitForLoadingExpiration();
			}
		
			// For save signature
			saveBtnCtrl.click();
			VoodooUtils.pause(3000);
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");
			
			// TODO: VOOD-1114 -Library support needed for controls in Emails > Settings > General > create(Signature)
			// Verify that operation of creating signature is saved successfully.
			new VoodooControl("select", "name", "signature_id").assertContains(customData.get(i).get("Dataset_5"), true);
		}
					
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
		
	public void cleanup() throws Exception {}
}