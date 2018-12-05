package com.sugarcrm.sugar.views;

public class StudioBasicSearch extends View {
	protected static StudioBasicSearch view;
	
	private StudioBasicSearch() throws Exception {}

	public static StudioBasicSearch getInstance() throws Exception {
		if (view == null)
			view = new StudioBasicSearch();
		return view;
	}
}