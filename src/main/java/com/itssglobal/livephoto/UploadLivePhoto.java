package com.itssglobal.livephoto;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.onfido.Onfido;
import com.onfido.models.LivePhoto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;

/**
 * @auther .: rose
 * @email ..: adewaleijalana@gmail.com
 * @created : 3/23/21
 */

public class UploadLivePhoto implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
        ServicesManager sm = request.getServicesManager();
        ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
        String apiToken = paramHelper.getServerProperty("ONFIDO_SANDBOX_API_TOKEN");


        String fileInBase64 = request.getParameter("file");
        String fileName = request.getParameter("fileName");
        String applicantId = request.getParameter("applicantId");

        byte[] imageByteArray = Base64.getDecoder().decode(fileInBase64);

        InputStream inputStream = new ByteArrayInputStream(imageByteArray);

        Onfido onfido = Onfido.builder()
                .apiToken(apiToken)
                .build();

        String mimeType = URLConnection.guessContentTypeFromStream(inputStream).split("/")[1];

        LivePhoto.Request lvePhotoRequest = LivePhoto.request()
                .applicantId(applicantId);

        LivePhoto livePhotoResponse = onfido.livePhoto.upload(inputStream,
                fileName + "." + mimeType, lvePhotoRequest);

        Result result = new Result();

        result.addParam(new Param("id", livePhotoResponse.getId(), "string"));
        result.addParam(new Param("fileName", livePhotoResponse.getFileName(), "string"));
        result.addParam(new Param("fileType", livePhotoResponse.getFileType(), "string"));
        result.addParam(new Param("fileSize", String.valueOf(livePhotoResponse.getFileSize()), "string"));
        result.addParam(new Param("createdAt", livePhotoResponse.getCreatedAt().toString(), "string"));
        result.addParam(new Param("href", livePhotoResponse.getHref(), "string"));
        result.addParam(new Param("downloadHref", livePhotoResponse.getDownloadHref(), "string"));

        return result;
    }
}
