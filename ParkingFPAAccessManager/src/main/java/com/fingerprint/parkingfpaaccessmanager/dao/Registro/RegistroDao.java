package com.fingerprint.parkingfpaaccessmanager.dao.Registro;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;

import java.util.List;

public interface RegistroDao {

    Tblregistry createOrUpdateRegistry(Tblregistry tblregistry);

    Tblregistry findRegistryByCvereg(long cvereg);

    void deleteTblRegistryByCveregistry(long cvereg);

    List<Tblregistry> findAllTblregistry();

}
