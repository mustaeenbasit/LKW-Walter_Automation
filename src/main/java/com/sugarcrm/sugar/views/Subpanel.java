package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.modules.RecordsModule;
import com.sugarcrm.sugar.records.Record;

/**
 * Abstract Class for SugarCRM Subpanel view
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public abstract class Subpanel extends View {
	RecordsModule identityModule = null;
	boolean isBWC;
	
	public Subpanel(RecordsModule identityModule, String tagIn, String strategyNameIn, String hookStringIn ) throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
		this.identityModule = identityModule;
	}
	
	/**
	 * Click on a record in this subpanel by index.
	 * <p>
	 * NOTE: Please see info in implementing subclasses.
	 * 
	 * @param row	1-based index of the row where a record exists.
	 * @throws Exception
	 */
	public abstract void clickRecord(int row) throws Exception;
	
	/**
	 * Edit a record in this subpanel.
	 * <p>
	 * NOTE: Please see info in implementing subclasses.
	 * 
	 * @param row	1-based index of the row you want to edit.
	 * @throws Exception
	 */
	public abstract void editRecord(int row) throws Exception;
	
	/**
	 * Unlink a record in this subpanel.
	 * <p>
	 * NOTE: Please see info in implementing subclasses.
	 * 
	 * @param row	1-based index of the row you want to unlink.
	 * @throws Exception
	 */
	public abstract void unlinkRecord(int row) throws Exception;
	
	/**
	 * Expand the actions menu in this subpanel.
	 * <p>
	 * There must be more than 1 action available to the user for this method to work.<br>
	 * 
	 * @param row	1-based index of the row you want to expand actions menu on.
	 * @throws Exception
	 */
	public abstract void expandSubpanelRowActions(int row) throws Exception;
	
	/**
	 * Create a Record of this Subpanels module type via UI.
	 * 
	 * @param data	FieldSet of data to use to create a record of this subpanels module type.<br>
	 * @return		Record of this subpanels module type.
	 * @throws Exception
	 */
	public abstract Record create(FieldSet data) throws Exception;
	
	/**
	 * Return the value of isBWC.
	 * 
	 * @return isBWC	true if this subpanel is BWC, false if it is Standard.
	 * @throws Exception
	 */
	public boolean isBWC() throws Exception {
		return isBWC;
	}

	/**
	 * Add a header to  Subpanel definition.
	 *
	 * @param toAdd
	 *            the SugarCRM internal name for the field
	 * @throws Exception
	 */
	public abstract void addHeader(String toAdd) throws Exception;

	/**
	 * Remove a header from Subpanel definition
	 *
	 * @param toRemove
	 * @throws Exception
	 */
	public abstract void removeHeader(String toRemove) throws Exception;

	/**
	 * Sorts the subpanel listview by the specified column in either ascending or
	 * descending order
	 *
	 * @param header
	 *            the SugarCRM internal name for the field you want to sort by and prepend "header" as text.
	 * @param ascending
	 *            true for ascending, false for descending.
	 *@throws Exception
	 */
	public abstract void sortBy(String header, boolean ascending) throws Exception;

} // Subpanel
