package com.fingerprint.parkingfpaaccessmanager.dao.Registro;


import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;
import com.fingerprint.parkingfpaaccessmanager.repository.TblregistryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegistroDaoImp implements RegistroDao {

    final
    TblregistryRepository tblregistryRepository;

    public RegistroDaoImp(TblregistryRepository tblregistryRepository) {
        this.tblregistryRepository = tblregistryRepository;
    }

    @Override
    public Tblregistry createOrUpdateRegistry(Tblregistry tblregistry) {
        return tblregistryRepository.save(tblregistry);
    }

    @Override
    public Tblregistry findRegistryByCvereg(long cvereg) {
        return tblregistryRepository.findTblregistryByCvereg(cvereg);
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
