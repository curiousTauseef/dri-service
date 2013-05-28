package vphshare.driservice.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import vphshare.driservice.domain.DataSource;
import vphshare.driservice.registry.lobcder.WrappedLogicalDataList;
import vphshare.driservice.registry.lobcder.LogicalData;
import vphshare.driservice.registry.lobcder.WrappedLogicalData;

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
		assertEquals(1367964000000L, l.getCreateDate());
		assertEquals(0L, l.getLastValidationDate());
		assertEquals(3L, l.getLength());
		assertEquals("1.txt", l.getName());
		assertEquals(206, l.getUid());
		assertEquals(1367964000000L, l.getModifiedDate());
		assertEquals("marek", l.getOwner());
		assertEquals("logical.file", l.getType());
		assertEquals(false, l.isSupervised());
		assertEquals(144, l.getPdriGroupId());
		assertEquals(1L, l.getParentRef());
		DataSource ds = data.getDataSource();
		assertNotNull(ds);
		assertEquals("b004414f-2e70-4b43-9026-1eabf0d28592-1.txt", ds.getName());
		assertEquals("swift://example.org/rest/auth", ds.getResourceUrl());
		assertEquals("test:test", ds.getUsername());
		assertEquals("somepass", ds.getPassword());
		assertNotNull(data.getPermissions());
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
		assertEquals(1366296931000L, l.getCreateDate());
		assertEquals(0L, l.getLastValidationDate());
		assertEquals(0L, l.getLength());
		assertEquals("", l.getName());
		assertEquals(1, l.getUid());
		assertEquals(1366296931000L, l.getModifiedDate());
		assertEquals("root", l.getOwner());
		assertEquals("logical.folder", l.getType());
		assertEquals(false, l.isSupervised());
		assertEquals(0, l.getPdriGroupId());
		assertEquals(1L, l.getParentRef());
		assertNull(data.getDataSource());
		assertNotNull(data.getPermissions());
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
		assertEquals("logical.folder", data.get(0).getLogicalData().getType());
		assertEquals("logical.file", data.get(1).getLogicalData().getType());
		
	}
}
