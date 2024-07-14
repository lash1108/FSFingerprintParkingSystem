package com.fingerprint.parkingfpaaccessmanager.service.registro;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonStringString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonStringString;

public interface RegistroService {

    ResponseJsonStringString findLastRegistryRecord();

    ResponseJsonGeneric setNewRegistryByToken(ConsumeJsonString consume);

    ResponseJsonGeneric findRegistryByToken(ConsumeJsonStringString consume);

    ResponseJsonGeneric unsetRegistryByToken(ConsumeJsonString consume);

    ResponseJsonGeneric findAllRegistries();

}
