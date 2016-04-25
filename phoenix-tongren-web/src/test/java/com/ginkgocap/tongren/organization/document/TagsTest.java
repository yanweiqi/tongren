package com.ginkgocap.tongren.organization.document;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;

public class TagsTest {

	private String cookie = "sessionID=\"d2ViMzY0MDE0NTgyNzE1NDE4NDQ=\"";
	private String baseUrl = "http://localhost/";
	//private String baseUrl = "http://192.168.101.131:6789/";
	private String organizationId="3920280301076510";

	@Test
	public void testAddCatalog() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "金融");
		params.put("type", "2");
		params.put("organizationId", organizationId);
		pt.testURI("/document/tags/addTags.json", params);
	}
	
	@Test
	public void testGetAllTags() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", organizationId);
		params.put("type", "2");
		pt.testURI("/document/tags/getAllTags.json", params);
	}
	@Test
	public void delTagsById() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("tagId", "3930788735483909");
		pt.testURI("/document/tags/delTagsById.json", params);
	}
	
	@Test
	public void addSourceToTags() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		
		String rectags="[{\"tagid\":\"11122\",\"tagName\":\"jinrong\"}]";
		params.put("recommendTag", rectags);
		params.put("tagId","3954403853991946");
		params.put("organizationId", "3920280301076510");
		params.put("sourceId", "3921073511071749");
		params.put("type", "2");
		pt.testURI("/document/tags/addSourceToTags.json", params);
	}
	
	
	@Test
	public void getAllSoucesFromTags() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("tagId", "3930189939867668");
		params.put("organizationId", "3920280301076510");
		params.put("index", "1");
		pt.testURI("/document/tags/getAllSoucesFromTags.json", params);
	}
	
	@Test
	public void delSourceFromTags() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("tagSourceId", "3930813062447119");
		pt.testURI("/document/tags/delSourceFromTags.json", params);
	}
	
	@Test
	public void getAssociateTagsBySourceId() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sourceId", "3920387570401325");
		params.put("organizationId", "3920280301076510");
		pt.testURI("/document/tags/getAssociateTagsBySourceId.json", params);
	}
	
	
}
