package com.fingerprint.parkingfpaaccessmanager.service.usuario;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonLong;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonBoolean;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;

public interface UsuarioService {

    ResponseJsonBoolean existUsrByParam(String param, int type);

    ResponseJsonGeneric findUsrByParamAndPassword(String param, String password, int type);

    ResponseJsonGeneric createOrUpdateUser(ConsumeJsonGeneric consume);

    ResponseJsonGeneric deleteUsrByCveusr(ConsumeJsonLong consume);

    ResponseJsonGeneric findAllUsersByTypeusr(String typeusr);

    ResponseJsonGeneric findUserByCveusr(ConsumeJsonLong consume);
}
