package org.lds.cm.content.automation.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.lds.cm.content.automation.enums.MarkLogicDatabase;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.w3c.dom.Document;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.RawQueryByExampleDefinition;
import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;

public class MarkLogicUtils { // FIXME: Make this use @MarkLogicDatabase as a class for the methods here. No need to write another class.
	private static DatabaseClient workingClient = null;
	private static DatabaseClient publishedClient = null;	
	
	static {
		System.out.println("MarkLogic Host: " + Constants.mlPreviewHost);
	}
	
	/////////////////////// BEGIN Read Methods /////////////////////////////
	public static File readFile(String path) throws ResourceNotFoundException{
		return getDocManager(MarkLogicDatabase.PUBLISHED).read(path,  new FileHandle()).get();
	}
	
	public static boolean docExists(String path) {
		return getDocManager(MarkLogicDatabase.PUBLISHED).exists(path) != null;
	}

	public static List<Document> findHtml5ByExample(String example, MarkLogicDatabase database, int length) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return runQueryByExample(example, database, database.getContentRoot(), length);
	}

	public static List<Document> findMediaXMLByExample(String example, MarkLogicDatabase database, int length) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return runQueryByExample(example, database, database.getWebmlRoot(), length);
	}

	private static List<Document> runQueryByExample(String example, MarkLogicDatabase database, String rootDirectory, int length) {
		DatabaseClient client = getConnection(database);
		QueryManager queryMgr = client.newQueryManager();
		System.out.println("example ="+example);
		
		String query = 	"<q:qbe xmlns:q='http://marklogic.com/appservices/querybyexample' xmlns:lds='http://www.lds.org/schema/lds-meta/v1'>" +
							"<q:query>" +
								example + 
							"</q:query>" +
						"</q:qbe>";
		
		StringHandle rawHandle = new StringHandle(query);
		
		RawQueryByExampleDefinition queryDef = queryMgr.newRawQueryByExampleDefinition(rawHandle);
		
		queryDef.setDirectory(rootDirectory + "/");
		
		queryMgr.setPageLength(length);
		SearchHandle resultsHandle = queryMgr.search(queryDef, new SearchHandle());
		
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		Document doc = null;
		List<Document> docs = new ArrayList<>();
		if(results.length > 0) {
			for(int index = 0; index < results.length; index++) {
				String uri = results[index].getUri();
				XMLDocumentManager docMgr = client.newXMLDocumentManager();
				DOMHandle handle = new DOMHandle();
				docMgr.read(uri, handle);
				doc = handle.get();
				docs.add(doc);
			}
		}
		
		return docs;
	}
	
	public static List<Document> findHtml5ByFileId(String fileId, MarkLogicDatabase database, String rootDirectory, Integer items) {
		DatabaseClient client = getConnection(database);
		QueryManager queryMgr = client.newQueryManager();
		
		StructuredQueryBuilder qb = queryMgr.newStructuredQueryBuilder();
		List<StructuredQueryDefinition> definitions = new ArrayList<>();
		definitions.add(qb.value(qb.field("identifiers"), null, new String[] {"wildcarded"}, 1.0, fileId + "*"));

		StructuredQueryDefinition queryDef = qb.and(definitions.toArray(new StructuredQueryDefinition[definitions.size()]));
		queryDef.setDirectory(rootDirectory);
		queryMgr.setPageLength(items);

		SearchHandle resultsHandle = queryMgr.search(queryDef, new SearchHandle());
		
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();

		Document doc = null;
		List<Document> docs = new ArrayList<>();
		if(results.length > 0) {
			for(int i = 0; i < results.length; i++) {
				String uris = results[i].getUri();
				XMLDocumentManager docMgr = client.newXMLDocumentManager();
				DOMHandle handle = new DOMHandle();
				docMgr.read(uris, handle);
				doc = handle.get();
				docs.add(doc);
			}
		}
		return docs;
	}

	/////////////////////// END Read Methods /////////////////////////////
	
	/////////////////////// BEGIN Delete Methods /////////////////////////////
	public static void deleteFileFromMarkLogic(MarkLogicDatabase db, String path) {
		getDocManager(db).delete(path);
	}
	
	public static void deleteFilesFromMarkLogic(MarkLogicDatabase db, List<String> paths) {
		for(String path: paths) {
			getDocManager(db).delete(path);
		}
	}
	
	/**
	 * This method is slow. void delete(String... uris) does not seem to work so it does one file at a time.
	 * Also, it does not delete folders, only files, so it may be better to use webDAV to delete instead of this method.
	 * @param db
	 * @param directory
	 */
	public static void deleteDirectory(MarkLogicDatabase db, File directory) {
		Collection<File> files = FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for(File file: files) {
			String markLogicPath = file.getAbsolutePath().substring(2, file.getAbsolutePath().length()).replace("\\", "/");
			getDocManager(db).delete(markLogicPath);
		}
	}
	
	/////////////////////// END Delete Methods /////////////////////////////
	
	/////////////////////// BEGIN Connection Methods /////////////////////////////
	private static XMLDocumentManager getDocManager(MarkLogicDatabase db) {
		return getConnection(db).newXMLDocumentManager();
	}
	
	private static DatabaseClient getConnection(MarkLogicDatabase db) {
		DatabaseClient client = null;
		switch(db) {
			case PUBLISHED:
				if(null == publishedClient) {
					publishedClient = DatabaseClientFactory.newClient(Constants.mlPublishHost, Constants.mlPublishPort, Constants.mlPublishDatabase, Constants.mlPublishUsername, Constants.mlPublishPassword, Authentication.DIGEST);
				}
				client = publishedClient;
				break;
			case URI_MAPPING:
				if(null == workingClient) {
					workingClient = DatabaseClientFactory.newClient(Constants.mlPreviewHost, Constants.mlPreviewPort, Constants.mlPreviewUsername, Constants.mlPreviewDatabase, Constants.mlPreviewPassword, Authentication.DIGEST);
				}
				client = workingClient;
				break;
		}
		return client;
	}
	
	/////////////////////// END Connection Methods /////////////////////////////

}
