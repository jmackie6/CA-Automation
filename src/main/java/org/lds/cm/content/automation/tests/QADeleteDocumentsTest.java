package org.lds.cm.content.automation.tests;

import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.annotations.Test;

import java.io.File;

public class QADeleteDocumentsTest {

    public static String file_id = "PD10042565_000_spiritual-experiences";


    @Test
    public void deleteFromDbAndAnnotations () throws Exception {
        String file_id = "09687_130_007";
        String path = "/history/saints-V1/14-visions-and-nightmares";
        String langID = "0";
        String lang = "eng";
        String fileName = "spiritual-healing.html";

        String fullUriSecondDoc = "/automation/bulkOperations/general-conference/1998/10/are-we-keeping-pace";
        String fullUriFirstDoc = "/automation/bulkOperations/general-conference/1998/10/a-voice-of-warning";
        String publishedDoc1FileId = "PD10042565_000_spiritual-experiences";
        String publishedDoc2FileId = "PD60001624_000_14-visions-and-nightmares";

//        QADocumentService.validatedByFileId(publishedDoc2FileId);

//        QADeleteService.bulkDeleteFromDbByFileId(publishedDoc1FileId);

//        QADeleteService.deleteFromDbByFileIdSingleDocument(publishedDoc1FileId);
        QADeleteService.deleteFromDbByFileIdSingleDocument(publishedDoc2FileId);
//        QADeleteService.deleteSingleDocmapByUriAndLang(publishedDoc1FileId);
//        QADeleteService.deleteSingleDocmapByUriAndLang(publishedDoc2FileId);
//        QADeleteService.deleteSingleDocmapByDataAid(publishedDoc1FileId);
//        QADeleteService.deleteSingleDocmapByDataAid(publishedDoc2FileId);

//        File startSpaGeneralConference1Doc = new File(Constants.bulkOperationsFileStartDir + "/GeneralConference/spaGeneralConference/a-voice-of-warning.html");
//        File startSpaGeneralConference2Doc = new File(Constants.bulkOperationsFileStartDir + "/GeneralConference/spaGeneralConference/are-we-keeping-pace.html");
//
//        QATransformService.transformFileGivenContentGroupId(startSpaGeneralConference1Doc, testContentGroupId);
//        QATransformService.transformFileGivenContentGroupId(startSpaGeneralConference2Doc, testContentGroupId);
//        QADeleteService.deleteSingleDocmapByDataAid(file_id);
//        QADeleteService.deleteSingleDocmapByUriAndLang(path);
//        QADeleteService.deleteFromDbByFileIdSingleDocument(file_id);

//        QADeleteService.deleteSingleDocumentFromDbByPathLangIdAndFilename(fileName, path, langID);
//        QADeleteService.bulkDeleteFromDbByLangIdAndPath(path, langID);
    }
}
