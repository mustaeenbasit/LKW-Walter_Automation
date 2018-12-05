package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.PortalAppModel;

/**
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public abstract class Record extends FieldSet {
	private static final long serialVersionUID = 1L;
	protected String guid = null;
	
	/**
	 * Constructor for a Record accepting a FieldSet of the record data.
	 *  
	 * @param	data	a FieldSet of data contained in this record.
	 * @throws Exception
	 */
	public Record(FieldSet data) throws Exception {
		putAll(data);
	}
	
	/**
	 * Returns SugarCRM's GUID for this record.  Note that Records created using library should
	 * have these, but if a tester creates a record manually they are responsible for detecting
	 * and setting the GUID in the Record object themselves if needed.  
	 *   
	 * @return	a String representation of the record's GUID.
	 * @throws	Exception	if no GUID is set	
	 */
	public String getGuid() throws Exception {
		if(null == guid) {
			throw new Exception("No GUID set for this record.  Use a library method to create " +
				"the record or set it manually using setGuid() on creation.");
		}
		return guid;
	}
	
	/**
	 * Sets the GUID for this record.  This should be called automatically when a record is
	 * created by a library method, but a tester should call this explicitly if they create a
	 * record manually.
	 *  
	 * @param	guidIn	a String representation of SugarCRM's GUID for this record.
	 */
	public void setGuid(String guidIn) {
		guid = guidIn;
	}

	/**
	 * Helper method to return a AppModel instance
	 * @return AppModel instance
	 */
	protected static AppModel sugar() {
		return AppModel.getInstance();
	}

	/**
	 * Helper method to return a PortalAppModel instance
	 * @return PortalAppModel instance
	 */
	protected static PortalAppModel portal() {
		return PortalAppModel.getInstance();
	}

	public abstract void delete() throws Exception;
	public abstract void navToRecord() throws Exception;
	public abstract void verify() throws Exception;
	public abstract String getRecordIdentifier();
	public abstract StandardRecord copy(FieldSet edits) throws Exception;
}