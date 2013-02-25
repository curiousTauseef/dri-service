package vphshare.driservice.testing.prepare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vphshare.driservice.domain.DataSource;

public class DataSourcePreparation {
	
	private static final List<DataSource> DS = new ArrayList<DataSource>();
	
	static {
		DataSource ds = new DataSource();
		ds.setType(DataSource.SWIFT_DATA_SOURCE);
		ds.setUrl("https://149.156.10.131:8443/auth/v1.0");
		
		DS.add(ds);
	}
	
	public static List<DataSource> getDataSources() {
		return Collections.unmodifiableList(DS);
	}
}
