package cn.itcast.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

public class SolrTest {
	//添加 修改
	@Test
	public void add() throws IOException, SolrServerException {
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
		SolrInputDocument solrInputDocument=new SolrInputDocument();
		solrInputDocument.setField("id", "3");
		solrInputDocument.setField("name", "赵云");
		solrServer.add(solrInputDocument, 1000);
	}
	//删除
	@Test
	public void delete() throws IOException, SolrServerException {
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
//		solrServer.deleteByQuery("*:*");
		solrServer.deleteById("1");
		solrServer.commit();
	}
	//查询
	@Test
	public void query() throws IOException, SolrServerException {
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
		SolrQuery solrQuery=new SolrQuery();
		solrQuery.setQuery("*:*");
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList results = queryResponse.getResults();
		long numFound = results.getNumFound();
		System.out.println("总共"+numFound+"条数据");
		
		for(SolrDocument doc:results) {
			System.out.println(doc.get("id"));
			System.out.println(doc.get("name"));
			System.out.println(doc.get("title"));
		}
		
		
	}
	//查询页面详细
	@Test
	public void queryWeb() throws IOException, SolrServerException {
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
		SolrQuery solrQuery=new SolrQuery();
		solrQuery.set("q", "钻石");
		solrQuery.set("fq", "product_price:[10 TO 100]");
		solrQuery.addSort("product_price", ORDER.desc);
		solrQuery.setStart(0);
		solrQuery.setRows(10);
		solrQuery.set("fl","id,product_name");
		solrQuery.set("df", "product_keywords");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("product_name");
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		
		
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList results = queryResponse.getResults();
		
		long numFound = results.getNumFound();
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		System.out.println("总共查到了"+numFound+"条数据");
		for(SolrDocument sd:results){
			String id = (String) sd.get("id");
			String product_name = (String) sd.get("product_name");
			String product_catalog_name = (String) sd.get("product_catalog_name");
			System.out.println(id);
			System.out.println(product_name);
			System.out.println(product_catalog_name);
			Map<String, List<String>> map = highlighting.get(id);
			if(map!=null&&map.size()>0) {
				List<String> list = map.get("product_name");
				System.out.println("高亮名字为:"+list.get(0));
				
			}
			System.out.println("-------------------------------------");
			
		}
		
		
	}
}
