package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooUtils;

/**
 * Model of the Compose Email View in BWC Modules. Its elements, controls and methods.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class BWCComposeEmail extends View {
	public BWCComposeEmail() throws Exception {
		super("div","css","#container1");

		// Common controls
		addControl("closeButton", "a", "css", getHookString() + " .container-close");
		addControl("sendButton", "button", "xpath", "//*[@id='composeHeaderTable0']//table//tbody//tr/td/button[@class='button'][1]");
		addControl("saveDraft", "button", "xpath", "//*[@id='composeHeaderTable0']//table//tbody//tr/td/button[@class='button'][2]");
		addControl("attachButton", "button", "xpath", "//*[@id='composeHeaderTable0']//table//tbody//tr/td/button[@class='button'][3]");
		addControl("optionsButton", "button", "xpath", "//*[@id='composeHeaderTable0']//table//tbody//tr/td/button[@class='button'][4]");
		
		// Common elements
		addBWCRelate("fromAddress","a","css", getHookString() + " #addressFrom0");
		addControl("toAddress", "input", "css", getHookString() + " input[name='addressTO0']");
		addControl("subject", "input", "css", getHookString() + " #emailSubject0");
		addControl("body", "body", "css", "#tinymce"); // In an iFrame
		addBWCRelate("parentModule","a","css", getHookString() + " #data_parent_type0");
		addSelect("parentRecord","a","css", getHookString() + " #data_parent_name0");
		
		addControl("addressBook", "button", "css", getHookString() + " .emailUILabel button");
		addControl("addCarbonCopy", "a", "css", getHookString() + " #cc_span0 a");
		addControl("addBlindCarbonCopy", "a", "css", getHookString() + " #bcc_span0 a");
		addControl("ccField", "input", "css", getHookString() + " #addressCC0");
		addControl("bccField", "input", "css", getHookString() + " #addressBCC0");
	}
	
	/**
	 * Open Address book for main TO: addresses.
	 * <p>
	 * When used, the sugar address book view opens.<br>
	 * 
	 * @throws Exception
	 */
	public void openAddressBook() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("addressBook").click();
		getControl("addressBook").waitForVisible();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Add input field for CC: address field.
	 * <p>
	 * When used the carbon copy, CC: field will be either made visible.<br>
	 * 
	 * @throws Exception
	 */
	public void addCCAddresses() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("addCarbonCopy").click();
		getControl("ccField").waitForVisible();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Add input field for BCC: address field.
	 * <p>
	 * When used the blind carbon copy, BCC: field will be made visible.<br>
	 * 
	 * @throws Exception
	 */
	public void addBCCAddresses() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("blindCarbonCopy").click();
		getControl("bccField").waitForVisible();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Add a message to the body of this email.
	 * <p>
	 * 
	 * @param bodyMessage String text of the message to send in this email.
	 * @throws Exception
	 */
	public void addBodyMessage(String bodyMessage) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.focusFrame("htmleditor0_ifr");
		getControl("body").set(bodyMessage);
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Click on the save draft button.
	 * <p>
	 * Must be on the email compose view to use.<br>
	 * When used, you will be left on the previous screen with the email saved to the My Drafts folder.
	 * 
	 * @throws Exception
	 */
	public void saveDraft() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("saveDraft").click();
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Click the send button.
	 * <p>
	 * Must be on the email compose view to use.<br>
	 * When used, if you have filled out the proper fields, To:, From:, Body:, then the email will be sent. If there are missing
	 * fields, then an error message will appear requesting user interaction.
	 * 
	 * @throws Exception
	 */
	public void send() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("sendButton").click();
		VoodooUtils.focusDefault();
	}
}
