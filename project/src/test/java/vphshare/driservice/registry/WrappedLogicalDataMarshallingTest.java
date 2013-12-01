package vphshare.driservice.registry;

import org.junit.Test;
import vphshare.driservice.domain.DataSource;
import vphshare.driservice.registry.lobcder.DataType;
import vphshare.driservice.registry.lobcder.LogicalData;
import vphshare.driservice.registry.lobcder.WrappedLogicalData;
import vphshare.driservice.registry.lobcder.WrappedLogicalDataList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WrappedLogicalDataMarshallingTest {
	
	private InputStream getFileAsStream(String path) {
		return getClass()
				.getClassLoader()
				.getResourceAsStream(path);
	}
	
	@Test
	public void testUnmarshallingLogicalFile() throws Exception {
		JAXBContext context = JAXBContext.newInstance(WrappedLogicalData.class);
		Unmarshaller um = context.createUnmarshaller();
		
		InputStream xml = getFileAsStream("marshalling/logical-file-jaxb.xml");
		WrappedLogicalData data = (WrappedLogicalData) um.unmarshal(xml);
		
		assertNotNull(data);
		assertEquals("/1.txt", data.getPath());
		LogicalData l = data.getLogicalData();
		assertNotNull(l);
		assertEquals("0", l.getChecksum());
		assertEquals(0L, l.getLastValidationDate());
		assertEquals(3L, l.getLength());
		assertEquals("1.txt", l.getName());
		assertEquals(206, l.getUid());
		assertEquals(1367964000000L, l.getModifiedDate());
		assertEquals("marek", l.getOwner());
		assertEquals(DataType.FILE, l.getType());
		assertEquals(false, l.isSupervised());
		assertEquals(144, l.getPdriGroupId());
		assertEquals(1L, l.getParentRef());
		DataSource ds = data.getDataSources().get(0);
		assertNotNull(ds);
		assertEquals("b004414f-2e70-4b43-9026-1eabf0d28592-1.txt", ds.getName());
		assertEquals("swift://example.org/rest/auth", ds.getResourceUrl());
		assertEquals("test:test", ds.getUsername());
		assertEquals("somepass", ds.getPassword());
	}
	
	@Test
	public void testUnmarshallingLogicalFolder() throws Exception {
		JAXBContext context = JAXBContext.newInstance(WrappedLogicalData.class);
		Unmarshaller um = context.createUnmarshaller();
		
		InputStream xml = getFileAsStream("marshalling/logical-folder-jaxb.xml");
		WrappedLogicalData data = (WrappedLogicalData) um.unmarshal(xml);
		
		assertNotNull(data);
		assertEquals("/", data.getPath());
		LogicalData l = data.getLogicalData();
		assertNotNull(l);
		assertEquals("0", l.getChecksum());
		assertEquals(0L, l.getLastValidationDate());
		assertEquals(0L, l.getLength());
		assertEquals("", l.getName());
		assertEquals(1, l.getUid());
		assertEquals(1366296931000L, l.getModifiedDate());
		assertEquals("root", l.getOwner());
		assertEquals(DataType.DIRECTORY, l.getType());
		assertEquals(false, l.isSupervised());
		assertEquals(0, l.getPdriGroupId());
		assertEquals(1L, l.getParentRef());
		assertEquals(0, data.getDataSources().size());
	}
	
	@Test
	public void testUnmarshallingListOfLogicals() throws Exception {
		JAXBContext context = JAXBContext.newInstance(WrappedLogicalDataList.class);
		Unmarshaller um = context.createUnmarshaller();
		
		InputStream xml = getFileAsStream("marshalling/logicals-list-jaxb.xml");
		List<WrappedLogicalData> data = ((WrappedLogicalDataList) um.unmarshal(xml)).getItems();
		
		assertNotNull(data);
		assertEquals(2, data.size());
		assertNotNull(data.get(0));
		assertNotNull(data.get(1));
		assertEquals(DataType.DIRECTORY, data.get(0).getLogicalData().getType());
		assertEquals(DataType.FILE, data.get(1).getLogicalData().getType());
		
	}
}
