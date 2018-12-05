package com.sugarcrm.sugar.views;

public class StudioFilterSearch extends View {
	protected static StudioFilterSearch view;
	
	private StudioFilterSearch() throws Exception {}
	
	public static StudioFilterSearch getInstance() throws Exception {
		if (view == null)
			view = new StudioFilterSearch();
		return view;
	}
}