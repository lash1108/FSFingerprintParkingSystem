package com.fingerprint.parkingfpaaccessmanager.model.pojos.util;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseJsonHandler {

    public ResponseJsonGeneric responseError(String msj, String clave, String codigo) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> error = new HashMap<>();
        error.put("msj", msj);
        error.put("clave", clave);
        error.put("codigo", codigo);

        responseJson.setDatos(error);
        return responseJson;
    }

    public ResponseJsonGeneric badCredencialsError(String msj) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> error = new HashMap<>();
        error.put("msj", msj);
        error.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        error.put("code", HttpStatus.UNAUTHORIZED.value());
        responseJson.setDatos(error);
        return responseJson;
    }

    public ResponseJsonGeneric notFoundResponse(String msj) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> error = new HashMap<>();
        error.put("msj", msj);
        error.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        error.put("code", HttpStatus.NOT_FOUND.value());
        responseJson.setDatos(error);
        return responseJson;
    }

    public ResponseJsonGeneric serverErrorResponse(String msj) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> error = new HashMap<>();
        error.put("msj", msj);
        error.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseJson.setDatos(error);
        return responseJson;
    }

    public ResponseJsonGeneric okResponse(String msj, Object obj) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> okMap = new HashMap<>();
        okMap.put("msj", msj);
        okMap.put("value", HttpStatus.OK.getReasonPhrase());
        okMap.put("obj", obj);
        okMap.put("code", HttpStatus.OK.value());
        responseJson.setDatos(okMap);
        return responseJson;
    }

    public ResponseJsonGeneric badRequestResponse(String msj, Object obj) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> okMap = new HashMap<>();
        okMap.put("msj", msj);
        okMap.put("value", HttpStatus.BAD_REQUEST.getReasonPhrase());
        okMap.put("obj", obj);
        okMap.put("code", HttpStatus.BAD_REQUEST.value());
        responseJson.setDatos(okMap);
        return responseJson;
    }

}
