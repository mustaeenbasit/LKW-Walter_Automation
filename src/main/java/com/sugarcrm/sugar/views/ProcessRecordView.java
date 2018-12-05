package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.modules.RecordsModule;

/**
 * Models the ProcessRecordView for Process type Modules in SugarCRM.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ProcessRecordView extends RecordView {
	/**
	 * Initializes the ProcessRecordView and specifies its parent module so that it knows which fields are available.
	 * @param parentModule - the module that owns this ProcessRecordView, likely passed in using the module's this
	 *                        variable when constructing the RecordView.
	 * @throws Exception
	 */
	public ProcessRecordView(RecordsModule parentModule) throws Exception {
		super(parentModule);

		addControl("designButton", "a", "css", ".detail.fld_open_designer a");
		addControl("exportButton", "a", "css", ".detail.fld_export_process a");
		addControl("shareButton", "a", "css", ".detail.fld_share a");
	}

	/**
	 * Starts the Design process on this Process Record.
	 * <p>
	 * When used, you will be taken to the Design view for this Process Record.
	 *
	 * @throws Exception
	 */
	public void design() throws Exception {
		openPrimaryButtonDropdown();
		getControl("designButton").click();
	}

	/**
	 * Starts the Export process on this Process Record.
	 * <p>
	 * When used, you will be prompted to accept or decline the export.<br>
	 *
	 * @throws Exception
	 */
	public void export() throws Exception {
		openPrimaryButtonDropdown();
		getControl("exportButton").click();
	}

	/**
	 * Starts the Share process on this Process Record.
	 * <p>
	 * When used, an email create drawer will be displayed for you to send an email.
	 *
	 * @throws Exception
	 */
	public void share() throws Exception {
		openPrimaryButtonDropdown();
		getControl("shareButton").click();
	}
}