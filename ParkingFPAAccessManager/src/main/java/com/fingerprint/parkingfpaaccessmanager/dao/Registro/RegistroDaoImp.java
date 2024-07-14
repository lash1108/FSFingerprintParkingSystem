package com.fingerprint.parkingfpaaccessmanager.dao.Registro;


import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;
import com.fingerprint.parkingfpaaccessmanager.repository.TblregistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegistroDaoImp implements RegistroDao {

    @Autowired
    TblregistryRepository tblregistryRepository;

    @Override
    public Tblregistry createOrUpdateRegistry(Tblregistry tblregistry) {
        return tblregistryRepository.save(tblregistry);
    }

    @Override
    public List<Object[]> findLastEdoRegistryRecord() {
        return tblregistryRepository.findLastEdoRegistryRecord();
    }

    @Override
    public Tblregistry findByTokenusr(String token) {
        return tblregistryRepository.findByTokenusr(token);
    }

    @Override
    public boolean existsTblregistryByTokenusr(String token) {
        return tblregistryRepository.existsTblregistryByTokenusr(token);
    }

    @Override
    public void deleteTblRegistryByCveregistry(long cvereg) {
        tblregistryRepository.deleteById(cvereg);
    }

    @Override
    public List<Tblregistry> findAllTblregistry() {
        return tblregistryRepository.findAll();
    }
}
