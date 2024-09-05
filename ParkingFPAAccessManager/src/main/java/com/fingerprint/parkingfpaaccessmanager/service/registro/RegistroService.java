package com.fingerprint.parkingfpaaccessmanager.service.registro;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;

public interface RegistroService {

    ResponseJsonGeneric createOrUpdateRegistryByToken(ConsumeJsonGeneric consume);

    ResponseJsonGeneric findAllRegistries();

    ResponseJsonGeneric findRegistryByToken(ConsumeJsonGeneric consume);

}
