package com.fingerprint.parkingfpaaccessmanager.dao.Registro;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;

import java.util.List;

public interface RegistroDao {

    Tblregistry createOrUpdateRegistry(Tblregistry tblregistry);

    List<Object[]> findLastEdoRegistryRecord();

    Tblregistry findByTokenusr(String token);

    boolean existsTblregistryByTokenusr(String token);

    void deleteTblRegistryByCveregistry(long cvereg);

    List<Tblregistry> findAllTblregistry();
}
