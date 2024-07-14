package com.fingerprint.parkingfpaaccessmanager.service.estacionamiento;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;

public interface EstacionamientoService {

    ResponseJsonGeneric createOrUpdateEstacionamiento(ConsumeJsonGeneric consume);

    ResponseJsonGeneric chooseEstSearchProvider(ConsumeJsonGeneric consume);

}
