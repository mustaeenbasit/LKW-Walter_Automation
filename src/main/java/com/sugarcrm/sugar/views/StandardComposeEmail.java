package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Model of the Compose Email View in Sidecar. Its elements, controls and methods.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class StandardComposeEmail extends View {

	public StandardComposeEmail() throws Exception {
		super("div","css",".layout_Emails.drawer.active");
		// Common controls
		addControl("cancelButton", "a", "css", getHookString() + " .fld_cancel_button.detail a");
		addControl("sendButton", "a", "css", getHookString() + " .fld_send_button.detail a");
		addControl("actionDropdown", "a", "css", getHookString() + " .actions.btn-group.detail a[data-toggle='dropdown']");
		addControl("saveDraft", "a", "css", getHookString() + " .fld_draft_button.detail a");
		
		// Common elements
		addSelect("fromAddress","a","css", getHookString() + " .fld_email_config.edit a");
		addControl("toAddress", "input", "css", getHookString() + " .fld_to_addresses.edit input");
		addControl("subject", "input", "css", getHookString() + " .fld_subject.edit input");
		addControl("body", "body", "css", "#tinymce"); // In an iFrame
		addSelect("teams","a","css", getHookString() + " .fld_team_name.edit a");
		addSelect("parentModule","a","css", getHookString() + " .fld_parent_name.edit div a");
		addSelect("parentRecord","a","css", getHookString() + " .fld_parent_name.edit div:nth-of-type(2) a");
		
		addControl("addressBook", "a", "css", getHookString() + " a[data-name='to_addresses']");
		addControl("carbonCopy", "a", "css", getHookString() + " .compose-sender-options [data-toggle-field='cc_addresses']");
		addControl("blindCarbonCopy", "a", "css", getHookString() + " .compose-sender-options [data-toggle-field='bcc_addresses']");
		addSelect("ccField", "span", "css", getHookString() + " .fld_cc_addresses.edit");
		addSelect("bccField", "span", "css", getHookString() + " .fld_bcc_addresses.edit");
		addControl("template", "a", "css", getHookString() + " .fld_template_button.edit a");
		addControl("signature", "a", "css", getHookString() + " .fld_signature_button.edit a");
		addControl("attachmentActions", "a", "css", getHookString() + " .actions.btn-group.edit a[data-toggle='dropdown']");
		addControl("attachmentFromFile", "span", "css", getHookString() + " [data-voodoo-name='upload_new_button']");
		addControl("attachmentFromSugar", "a", "css", getHookString() + " [data-voodoo-name='attach_sugardoc_button'] a");
	}
	
	/**
	 * Open Address book for main TO: addresses.
	 * <p>
	 * When used, a search and select drawer opens to choose addresses from.<br>
	 * 
	 * @throws Exception
	 */
	public void openAddressBook() throws Exception {
		getControl("addressBook").click();
	}
	
	/**
	 * Toggle visibility of CC: address field.
	 * <p>
	 * When used the carbon copy, CC: field will be either made visible or hidden.<br>
	 * 
	 * @throws Exception
	 */
	public void toggleCCAddresses() throws Exception {
		getControl("carbonCopy").click();
	}
	
	/**
	 * Toggle visibility of BCC: address field.
	 * <p>
	 * When used the blind carbon copy, BCC: field will be either made visible or hidden.<br>
	 * 
	 * @throws Exception
	 */
	public void toggleBCCAddresses() throws Exception {
		getControl("blindCarbonCopy").click();
	}
	
	/**
	 * Add a message to the body of this email.
	 * <p>
	 * 
	 * @param bodyMessage String text of the message to send in this email.
	 * @throws Exception
	 */
	public void addBodyMessage(String bodyMessage) throws Exception {
		VoodooUtils.focusFrame("mce_0_ifr");
		getControl("body").set(bodyMessage);
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Click cancel in this compose email view.
	 * <p>
	 * 
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		getControl("cancelButton").click();
		sugar().alerts.waitForLoadingExpiration();
	}
}
