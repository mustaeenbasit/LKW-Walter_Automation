package com.sugarcrm.sugar.modules;


import com.sugarcrm.candybean.datasource.DS;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.WsRestV10;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.MassUpdate;
import com.sugarcrm.sugar.views.SearchSelectDrawer;
import com.sugarcrm.sugar.views.Subpanel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base class from which all modules with records extend. Methods and data
 * which are common to all modules with records are stored here.
 *
 * Note: this module should not be extended directly into a SugarCRM module;
 * for that, use one of its subclasses (e.g. StandardModule, BWCModule).
 *
 * @author David Safar <dsafar@sugarcrm.com>
 */
public abstract class RecordsModule extends Module {
	/**
	 * A hash representing this module's fields. The keys should contain the
	 * VoodooGrimoire names of the fields (e.g. billingAddressStreet).
	 */
	public HashMap<String, SugarField> fields = new HashMap<String, SugarField>();
	/**
	 * A hash containing modules to which this module has a *-to-many
	 * relationship. Theoretically these should correlate to subpanels in the
	 * UI.
	 */
	public HashMap<String, RecordsModule> relatedModulesMany = new HashMap<String, RecordsModule>();
	/**
	 * A hash containing modules to which this module has a *-to-one
	 * relationship. Theoretically these should correlate to relationship fields
	 * in the UI.
	 */
	public HashMap<String, String> relatedModulesOne = new HashMap<String, String>();
	/** A collection of the subpanels for this module. */
	public HashMap<String, Subpanel> subpanels = new HashMap<String, Subpanel>();
	/** A representation of the Mass Update panel for this module. */
	public MassUpdate massUpdate;
	/** A representation of the Search and Select View of this module. */
	public SearchSelectDrawer searchSelect = new SearchSelectDrawer();
	/** The name of the subpanel that this module exists in, e.g. "Activities" */
	public String bwcSubpanelName = "";
	/**
	 * Default data for this module. The keys should contain the VoodooGrimoire
	 * names of the fields (e.g. billingAddressStreet), and the values should
	 * contain String representations of reasonable default values for those
	 * fields.
	 */
	public FieldSet defaultData = new FieldSet();
	public String recordClassName;
	public Api api = new Api();

	public RecordsModule() throws Exception {
		super();
	}

	/**
	 * Returns the requested SugarField
	 *
	 * @param fieldName
	 *            the VoodooGrimoire name for the desired field.
	 * @see SugarField
	 * @return the requested SugarField
	 */
	public SugarField getField(String fieldName) throws Exception {
		return fields.get(fieldName);
	}

	/**
	 * Loads this module's fields from its CSV file. Field CSV files should
	 * follow this naming convention - AccountsModuleFields.csv,
	 * ContactsModuleFields.csv etc...
	 *
	 * @throws Exception
	 */
	public void loadFields() throws Exception {
		DataSource fieldDefinitions = null;
		String fileName = RecordsModule.this.getClass().getSimpleName()
				+ "Fields";
		System.out.println(fileName);
		DS dsWrapper = new DS(fileName);
		String propNameCsvBaseDir = "datasource.csv.baseDir";
		String propValueCsvBaseDir = "src/main/resources/data";
		dsWrapper
		.init(DS.DataType.CSV, propNameCsvBaseDir, propValueCsvBaseDir);

		fieldDefinitions = dsWrapper.getDataSource(fileName);

		// Check value, if equal to "null" set SugarField value to null
		for(FieldSet field : fieldDefinitions) {
			fields.put(field.get("sugarField"), new SugarField(field));
			if(field.get("defaultValue") != null
					&& !(field.get("defaultValue").isEmpty()))
				defaultData.put(field.get("sugarField"),
						field.get("defaultValue"));
		}
	}

	/**
	 * Creates a single record via the UI using default data.
	 *
	 * @return a Record object representing the record created
	 * @throws Exception
	 */
	public Record create() throws Exception {
		VoodooUtils.voodoo.log.fine("Loading default data...");
		return create(defaultData);
	}

	/**
	 * Creates multiple Record objects via the UI from the data in a DataSource.
	 *
	 * @param recordData
	 * @return an ArrayList of Records representing the accounts created.
	 * @throws Exception
	 */
	public ArrayList<Record> create(DataSource recordData) throws Exception {
		VoodooUtils.voodoo.log.info("Creating " + recordData.size()
				+ "records...");
		ArrayList<Record> results = new ArrayList<Record>();
		for(FieldSet record : recordData) { // iterate over records
			results.add(create(record));
		}
		VoodooUtils.voodoo.log.info(recordData.size() + "records created.");
		return results;
	}

	public abstract Record create(FieldSet testData) throws Exception;

	/**
	 * Returns a single standard subpanel of this module type.
	 *
	 * @return a model of the Standard subpanel for this module.
	 */
	public Subpanel getSubpanel() throws Exception {
		return getSubpanel("standard");
	}

	/**
	 * Returns a single subpanel of this module.
	 * <p>
	 *
	 * @param	subpanelType	Subpanel Type to get from this module, "standard" or "bwc" in most cases.
	 * @return	a model of the specified subpanel for this module
	 */
	public Subpanel getSubpanel(String subpanelType) throws Exception {
		return subpanels.get(subpanelType);
	}

	/**
	 * Returns a single mass update panel of this module type
	 *
	 * @return a model of the default mass update panel of this module
	 */
	public MassUpdate getMassUpdate() throws Exception {
		return massUpdate;
	}

	/**
	 * Returns an independent copy of the defaultData FieldSet.
	 *
	 * @return - a copy of the default data
	 * @throws Exception
	 */
	public FieldSet getDefaultData() {
		return defaultData.deepClone();
	}

	/**
	 * Navigates to the list view of the current module.
	 *
	 * @throws Exception
	 */
	public void navToListView() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating to " + moduleNamePlural + " module ListView...");
		sugar().navbar.navToModule(moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Navigates to the list view of the current module in portal.
	 * <p>
	 * Must be in Portal to use.<br>
	 * When used, you will be left on the list view of this module in portal.
	 *
	 * @throws Exception
	 */
	public void navToPortalListView() throws Exception {
		VoodooUtils.voodoo.log.info("Navigating to " + moduleNamePlural + " module ListView in Portal...");
		portal().navbar.navToModule(moduleNamePlural);
		portal().alerts.waitForLoadingExpiration();
	}

	/**
	 * Groups together API methods for convenient access.
	 *
	 * @author Mazen Louis <mlouis@sugarcrm.com>
	 */
	public class Api {
		/**
		 * Prepare record data to be passed to SugarCRM's REST API.
		 * @param	toPrep	Record data to be processed.
		 * @return	a new HashMap of the translated data.
		 * @throws Exception
		 */
		HashMap<String, String> prepForRest(final FieldSet toPrep) throws Exception {
			HashMap<String, String> translatedData = new HashMap<String, String>();
			SugarField currentField;
			FieldSet cloneToPrep = (FieldSet)toPrep.clone();
			cloneToPrep.putAll(toPrep);

			prepDatesForRest(cloneToPrep);
			prepStatusForRest(cloneToPrep);
			// For each field in the fieldset...
			for(String key : cloneToPrep.keySet()) {
				// ...grab the field definition...
				currentField = fields.get(key);
				// ..and if it has a REST name, store the data under that name instead.
				if(currentField.get("rest_name") != null) {
					if("preferred_language".equals(key)) {
						DataSource displayToRest = loadDisplayToRestMap();
						for(FieldSet restValues : displayToRest){
							if(currentField.get("default_value").equals(restValues.get("display"))){
								translatedData.put(key, restValues.get("rest"));
								break;
							}
						}
					} else {
						translatedData.put(currentField.get("rest_name"), cloneToPrep.get(key));
					}
				}
			}

			// If there is no assigned user, assign admin.
			if(translatedData.get("assigned_user_id") == null) {
				translatedData.put("assigned_user_id", "1");
			}

			// Ref. VOOD-1681 sugar.opportunities.api.create() not working with "@Features(revenueLineItem = false)"
			if(RecordsModule.this instanceof OpportunitiesModule && translatedData.get("amount") == null){
				translatedData.put("amount", "0");
			}

			return translatedData;
		}

		/**
		 * Grabs the rest to display value relationships csv
		 * @return DataSource of rest vs display value relationships
		 * @throws Exception
		 */
		private DataSource loadDisplayToRestMap() throws Exception {
			DS dsWrapper = new DS("env_RestVsDisplay");
			dsWrapper.init(DS.DataType.CSV, "datasource.csv.baseDir", "src/main/resources/data");
			DataSource displayToRest = dsWrapper.getDataSource("env_RestVsDisplay");
			return displayToRest;
		}

		/**
		 * Formats any date/datetime fields in the record to be created as ISO-8601 so the REST API won't choke on them.
		 * @param	toPrep	the fields of the record to be created via REST
		 * @throws	Exception	if a date field with an invalid name is found in toPrep.
		 */
		private void prepDatesForRest(FieldSet toPrep) throws Exception {
			HashMap<String,String> dateFieldTypes = new HashMap<String,String>();

			// Pull date-related fields from toPrep and put them in dateFieldTypes
			getDateFields(toPrep, dateFieldTypes);

			// Now dateFieldTypes contains all the date field names as keys and which type they belong to as values.
			// For each field, generate the ISO-8601 formatted date from the provided components.
			updateDateFields(toPrep, dateFieldTypes);
		}

		/**
		 * Corrects the disconnect between UI and DB values for special modules before use in an API create.
		 * @param toPrep the fields of the record to be created via REST
		 * @throws Exception
		 */
		private void prepStatusForRest(FieldSet toPrep) throws Exception {
			if(RecordsModule.this instanceof CallsModule || RecordsModule.this instanceof MeetingsModule) {
				switch (toPrep.get("status")) {
					case "Scheduled":
						toPrep.put("status", "Planned");
						break;
					case "Canceled":
						toPrep.put("status", "Not Held");
						break;
					default:
						break;
				}
			} else if(RecordsModule.this instanceof KBModule) {
				toPrep.put("status", toPrep.get("status").toLowerCase().replaceAll("\\s", "-"));
			}
		}

		/**
		 * Let's take all the date fields from over here, and put them over there. {@link "https://imgflip.com/i/8wqr4"}
		 * @param	toPrep	a collection of fields which may or may not contain date fields
		 * @param	dateFieldTypes	a collection which will receive all date fields found within toPrep.
		 * @throws	Exception
		 */
		private void getDateFields(FieldSet toPrep, HashMap<String, String> dateFieldTypes) throws Exception {
			for(String key : toPrep.keySet()) {
				if(key.startsWith("date")) {
					// Determine the type of field: date, datetime, or datehmm (hours, minutes, meridiem).
					String type = getDateFieldType(key, toPrep);

					String[] keyPieces = key.split("_");

					// Figure out the fieldname (e.g. date_start's fieldname is "start")
					String fieldName = "";
					fieldName = keyPieces[1];

					if(keyPieces.length > 2) {
						// Exclude datetime and datehmm suffixes from the field name.
						int end;
						if("date".equals(type)) // date fields have no suffixes...
							end = keyPieces.length - 1;
						else // but the other types do, so the last token is not part of the field name.
							end = keyPieces.length - 2;

						for(int token = 2; token <= end; token++) {
							fieldName += ("_" + keyPieces[token]);
						}
					}

					// If the current date/datetime isn't already in the hash, add it.
					// If the field was already determined to be a different type, error.
					String existingType = dateFieldTypes.get(fieldName);
					if(existingType == null)
						dateFieldTypes.put(fieldName, type);
					else if(!existingType.equals(type))
						throw new Exception("REST API detected invalid combination of date fields.  Check your field names.");
				} // if(key.startsWith("date"))
			} // for(String key : toPrep.keySet())
		} // getDateFields

		/**
		 * Determines the type of a date field based on any one of its keys.
		 * @param	key	A String array generated by splitting the key on "_".  E.g. {"date", "start", "date"}.
		 * @param toPrep
		 * @return	A string representing the type of date field: "date" for plain dates, "datetime" for dates with a time field,
		 * 			or datehmm for dates with separate hours, minutes, and meridiem fields.  "" indicates we're not sure.
		 * @throws Exception
		 */
		private String getDateFieldType(String key, FieldSet toPrep) throws Exception {
			String type;
			String[] keyPieces = key.split("_");

			if(keyPieces.length == 2) {
				type = "date";
			} else {
				switch(keyPieces[keyPieces.length - 1]) {
				case "date":
					if(toPrep.containsKey(key.replaceFirst("date$", "time")))
						type = "datetime";
					else if(toPrep.containsKey(key.replaceFirst("date$", "hours")))
						type = "datehmm";
					else
						throw new Exception("_date field passed without time or hours.  Check your fields.");
					break;
				case "time":
					type = "datetime";
					break;
				case "hours":	// these three fields are all used together in one type.
				case "minutes":
				case "meridiem":
					type = "datehmm";
					break;
				default:
					type="date";
				}
			}

			return type;
		}

		/**
		 * Use a list of record fields and a list of the date fields contained therein to replace each componentized date field
		 * with a single ISO-8601 formatted datetime string.
		 * @param	toPrep	A collection of record fields.
		 * @param	dateFieldTypes	A collection of
		 * @throws Exception
		 */
		private void updateDateFields(FieldSet toPrep, HashMap<String, String> dateFieldTypes) throws Exception {
			String formattedDate = null;

			for(String field : dateFieldTypes.keySet()) {
				try {
					formattedDate = reformatDateFromComponents(field, dateFieldTypes.get(field), toPrep);
				} catch (ParseException e) {
					throw new Exception(field + ": " + e.getMessage(), (Throwable)e);
				}

				// Update the date field with the newly-formatted value and remove the other fields.
				String dateFieldKey = "date_" + field,
						timeFieldKey = "date_" + field + "_time",
						hoursFieldKey = "date_" + field + "_hours",
						minutesFieldKey = "date_" + field + "_minutes",
						meridiemFieldKey = "date_" + field + "_meridiem";
				if((dateFieldTypes.get(field).equals("date"))) {
					formattedDate = formattedDate.split("T")[0];

				} else {
					dateFieldKey += "_date";
				}

				toPrep.put(dateFieldKey, formattedDate);
				toPrep.remove(timeFieldKey);
				toPrep.remove(hoursFieldKey);
				toPrep.remove(minutesFieldKey);
				toPrep.remove(meridiemFieldKey);
			}
		}

		/**
		 *
		 * @param	field	The name of the specified date field, e.g. "start" or
		 * @param	type	The type of the specified date field, i.e. "date", "datetime", or "datehmm", or null if the type is not yet known.
		 * @param	components	The various fields that comprise the date field: a date, a date and a time, or a date, hours,
		 * 						minutes, and meridiem
		 * @return	An ISO-8601 representation of the specified date.
		 * @throws	Exception
		 */
		private String reformatDateFromComponents(String field, String type, FieldSet components) throws Exception {
			String dateString = null,
					timeString = null,
					hoursString = null,
					minutesString = null,
					meridiemString = null;

			if(type == null)
				type = "date";

			switch(type) {
			case "date":
				dateString = components.get("date_" + field);
				break;
			case "datetime":
				dateString = components.get("date_" + field + "_date");
				timeString = components.get("date_" + field + "_time");
				hoursString = timeString.split(":")[0];
				minutesString = timeString.split(":")[1].split("\\D")[0];
				meridiemString = timeString.split(":")[1].split("\\d")[2];
				break;
			case "datehmm":
				dateString = components.get("date_" + field + "_date");
				hoursString = components.get("date_" + field + "_hours");
				minutesString = components.get("date_" + field + "_minutes");
				meridiemString = components.get("date_" + field + "_meridiem");
				break;
			default:
				throw new Exception("Unknown date format: " + type);
			}

			if(hoursString == null) hoursString = "00";
			if(minutesString == null) minutesString = "00";
			if(meridiemString == null) meridiemString = "AM";

			return VoodooUtils.formatDateIso8601(dateString, hoursString, minutesString, meridiemString);
		}

		/**
		 * Creates a new record with the default data using REST.
		 *
		 * @return	a Record object representing the created record.
		 * @throws Exception
		 */
		public Record create() throws Exception {
			return create(getDefaultData());
		}

		/**
		 * Creates one new record with the specified data using REST.
		 *
		 * @param	recordData	A FieldSet containing the fields for the record to be created.
		 * @return	a Record representation of the record created in SugarCRM.
		 * @throws Exception
		 */
		public Record create(FieldSet recordData) throws Exception {
			DataSource wrapper = new DataSource();
			wrapper.add(recordData);
			return create(wrapper).get(0);
		}

		/**
		 * Creates one or more new records with the specified data using REST.
		 *
		 * @param	recordData	A DataSource containing the fields for the record(s) to be created.
		 * @return	An ArrayList<Record> containing representations of the records created in
		 * 			SugarCRM.
		 * @throws Exception
		 */
		public ArrayList<Record> create(DataSource recordData) throws Exception {
			ArrayList<Record> createdRecords = new ArrayList<Record>();
			ArrayList<HashMap<String, String>> listOfMaps = new ArrayList<HashMap<String, String>>();
			WsRestV10 rest = new WsRestV10();
			int beforeCount = rest.getTotalCount(moduleNamePlural);

			for(FieldSet record : recordData) {
				FieldSet finalRecordData = getDefaultData();
				finalRecordData.putAll(record);
				// TODO: This is a terrible hack. Fix email address relationship  handling.
				finalRecordData.remove("emailAddress");

				// Add the record to the payload for the REST call
				listOfMaps.clear();

				listOfMaps.add(prepForRest(finalRecordData));

				// Create one record.
				ArrayList<HashMap<String, Object>> result = rest.create(moduleNamePlural, listOfMaps);

				Record toAdd = (Record)(Class.forName(RecordsModule.this.recordClassName)
						.getConstructor(FieldSet.class).newInstance(finalRecordData));
				toAdd.setGuid((result.get(0).get("id").toString()));

				createdRecords.add(toAdd);
			}

			int afterCount = rest.getTotalCount(moduleNamePlural);
			VoodooUtils.voodoo.log.info("Record count changed from " + beforeCount + " to " + afterCount);

			return createdRecords;
		}

		/**
		 * Deletes all records in the current module using REST.
		 */
		public void deleteAll() throws Exception {
			WsRestV10 rest = new WsRestV10();
			int beforeCount = rest.getTotalCount(moduleNamePlural);
			rest.deleteAll(moduleNamePlural);
			int afterCount = rest.getTotalCount(moduleNamePlural);
			VoodooUtils.voodoo.log.info("Record count changed from " + beforeCount + " to " + afterCount);
		}
	} // API

}