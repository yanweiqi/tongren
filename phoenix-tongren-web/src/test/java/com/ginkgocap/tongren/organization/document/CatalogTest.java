package com.ginkgocap.tongren.organization.document;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;

public class CatalogTest {
	private String cookie = "sessionID=\"d2ViNzk4ODE0NTc5NDM0NDY1NDA=\"";
	//private String baseUrl = "http://localhost/";
	//private String baseUrl = "http://192.168.101.131:6789/";
	private String baseUrl ="http://192.168.101.53:4448/";
	
	private String organizationId="3915313980899348";

	@Test
	public void testAddCatalog() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "歌曲");
		params.put("pid", "3946096896507924");
		params.put("organizationId", organizationId);
		params.put("type", "1");
		pt.testURI("document/catalog/addCatalog.json", params);
	}
	
	@Test
	public void testGetRootCatalogs() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", organizationId);
		params.put("type", "1");
		pt.testURI("document/catalog/getRootCatalogs.json", params);
	}
	
	@Test
	public void getResourceOfCatalog() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("catalogId", "3946096896507929");
		params.put("index", "1");
		params.put("type", "1");
		pt.testURI("document/catalog/getResourceOfCatalog.json", params);
	}
	
	@Test
	public void testSetDocumentToDirs() throws Exception{
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", "3920280301076510");
		params.put("resourceId", "3920387272605736");
		params.put("catalogIds", "3929036481101889");
		pt.testURI("document/catalog/setDocumentToDirs.json", params);
	}
	
	@Test
	public void testUpdateCatalogueName() throws Exception{
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("catalogId", "3930519415029780");
		params.put("name", "链家地产");
		pt.testURI("document/catalog/updateCatalogueName.json", params);
	}
	
	@Test
	public void testGetCatalogueTree()throws Exception{
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("resourceId", "3909855484313642");
		params.put("organizationId", "3920280301076510");
		pt.testURI("document/catalog/getCatalogueTree.json", params);
	}
	
	@Test
	public void testDeleteResourceOfCatalogue()throws Exception{
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("catalogResourceId", "3951849082455536");
		params.put("type", "2");
		pt.testURI("document/catalog/deleteResourceOfCatalogue.json", params);
	}
	
	@Test
	public void testDeleteCatalogue()throws Exception{
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("catalogId", "3930519503110169");
		pt.testURI("document/catalog/deleteCatalogue.json", params);
	}
	
}
