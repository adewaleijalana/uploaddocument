package com.itssglobal.document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.onfido.Onfido;
import com.onfido.models.Document;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * @auther .: rose
 * @email ..: adewaleijalana@gmail.com
 * @created : 3/23/21
 */
@Slf4j
public class UploadDocument implements JavaService2 {

    private static final String fileInBase64 = new StringBuilder("").toString();

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ServicesManager sm = request.getServicesManager();
        ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
        String apiToken = paramHelper.getServerProperty("ONFIDO_SANDBOX_API_TOKEN");

        /*String fileType = request.getParameter("type");
        String fileInBase64 = request.getParameter("file");
        String fileName = request.getParameter("fileName");
        String applicantId = request.getParameter("applicantId");*/

        String fileType = "passport";
        String fileInBase64 = request.getParameter("file");
        String fileName = "drivinglicence";
        String applicantId = "cbd609ce-d7a5-4c35-966a-63429f685f2f";

        byte[] imageByteArray = Base64.getDecoder().decode(fileInBase64);

        InputStream inputStream = new ByteArrayInputStream(imageByteArray);

        Onfido onfido = Onfido.builder()
                .apiToken(apiToken)
                .build();

        Document.Request documentRequest = Document.request()
        .applicantId(applicantId)
        .type(fileType);

        Document documentResponse =
                onfido.document.upload(inputStream, fileName, documentRequest);

        log.info("documentResponse: {}", gson.toJson(documentResponse));

        return null;
    }
}
