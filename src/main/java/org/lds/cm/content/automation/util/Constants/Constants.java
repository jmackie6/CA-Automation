package org.lds.cm.content.automation.util.Constants;

import org.lds.stack.qa.TestConfig;

/**
 * A container for test environment property injection. {@link TestConfig}
 * will initialize each public, static, non-final field with a matching test
 * environment property. The test environment properties are selected based on
 * the value of the "testEnv" system property.
 * 
 * @see TestConfig
 *
 * @author <a href="http://code.lds.org/maven-sites/stack/">Stack Starter</a>
 */
public final class Constants {
	public static String transformFileStartDir;
	public static String translationStartDir;
	public static String mediaXmlFileStartDir;
	public static String actionFilesStartDir;
	public static String bulkOperationsFileStartDir;
	public static String LoadTestingFilesStartDir;
	
	public static String mlPreviewHost;
	public static int mlPreviewPort;
	public static String mlPreviewUsername;
	public static String mlPreviewPassword;
	public static String mlPreviewDatabase;

	public static String mlPublishHost;
	public static int mlPublishPort;
	public static String mlPublishUsername;
	public static String mlPublishPassword;
	public static String mlPublishDatabase;
	public static String epTranslationDocumentDocId;
	
	public static String dbUrl;
	public static String dbUsername;
	public static String dbPassword;
	public static String dbDriver;
	public static String ECdbUrl;
	public static String ECdbUsername;
	public static String ECdbPassword;

	public static String annotationServerUsername;
	public static String annotationServerPassword;

	public static String epTranslatedFile;
	public static String epTransform;
	public static String epTransformSA;
	public static String epEditorialTransform;
	public static String epSandboxTransform;
	public static String epValidate;
	public static String epApprove;
	public static String epTranslationMetadata;
	public static String epTranslationDocument;
	public static String epPublishBroadcastSA;
	public static String epQueryDAM;
	public static String epPreviewFileByBatchGuid;
	public static String epAnnotationServerDocMapDocId;
	public static String epDelete;
	public static String epManifest;
	public static String epTitanAssetSearch;
	public static String epTitanContentAPI;
	public static String epDeleteDocument;

	public static String xmlRoot;
	public static String cixRoot;
	public static String coverArt;
	public static String gitRoot;
	public static String mediaXMLRoot;
	public static String validationFileRoot;
	public static String transformFileRoot;
	public static String epGetAudio;
	public static String epGetAuthorData;
	public static String epGetMediaId;

	public static String epGetMediaIdByFileId;
	public static String epDamIdAndVersionId;

	public static String epGetLanguage;
	public static String epGetDocument;
	public static String apiClientId;
	public static String apiClientSecret;
	public static String epScripture;

	public static String epqaPublishFile;
	
	public static String epGetProcessLogId;
	public static String epDeleteDocmapByUriAndLanguage;
	public static String epDeleteDocmapByDataAid;
	public static String epDeleteDocmap;
	public static String epBroadcastUri;
	public static String epUpdateDocumentMedia;
	public static String epTranslationUpload;
	public static String getEpBroadcastUri;
	public static String epUpdateManifestEnglishTitle;
	public static String epGetDocumentIdFromLanguageIdAndUri;

	public static String epDocXService1;
	public static String epDocXService2;


	public static String baseURL;
	public static String driverLocation;
	public static String environment;

	public static String broadcastContentGroupId;
	public static String liahonaContentGroupId;
	public static String ensignContentGroupId;
	public static String friendContentGroupId;
	public static String newEraContentGroupId;
	public static String generalConferenceContentGroupId;
	public static String youthContentGroupId;
	public static String scripturesContentGroupId;
	public static String manualContentGroupId;
	public static String testContentGroupId;

	public static final int numOfTestsToRun = 5;

	static {
		// Load test environment properties using the "testEnv" System property.
		new TestConfig("test").applyConfiguration(Constants.class);
	}

	// Private constructor to discourage instantiation.
	private Constants() {}

	
}
