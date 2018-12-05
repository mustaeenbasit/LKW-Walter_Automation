package com.sugarcrm.sugar.views;

public class StudioAdvancedSearch extends View {
	protected static StudioAdvancedSearch view;
	
	private StudioAdvancedSearch() throws Exception {}

	public static StudioAdvancedSearch getInstance() throws Exception {
		if (view == null)
			view = new StudioAdvancedSearch();
		return view;
	}
}