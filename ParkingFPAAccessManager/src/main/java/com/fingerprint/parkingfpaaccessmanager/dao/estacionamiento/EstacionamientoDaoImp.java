package com.fingerprint.parkingfpaaccessmanager.dao.estacionamiento;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblest;
import com.fingerprint.parkingfpaaccessmanager.repository.TblestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstacionamientoDaoImp implements EstacionamientoDao {

    @Autowired
    private TblestRepository tblestRepository;


    @Override
    public Tblest saveOrUpdateTblest(Tblest tblest) {
        return tblestRepository.save(tblest);
    }

    @Override
    public List<Tblest> findAllTblest() {
        return tblestRepository.findAll();
    }

    @Override
    public boolean existsTblestByCveest(long cveest) {
        return tblestRepository.existsTblestByCveest(cveest);
    }

    @Override
    public Tblest findTblestByCveest(long cveest) {
        return tblestRepository.findTblestByCveest(cveest);
    }

    @Override
    public Page<Object[]> findRegularEstByRangePage(String startDate, String endDate, Pageable pageable) {
        return tblestRepository.findRegularEstByRangePage(startDate, endDate, pageable);
    }

    @Override
    public Page<Object[]> findAllActiveEstWithUsr(String startDate, String endDate, String key, Pageable pageable) {
        return tblestRepository.findAllActiveEstWithUsr(startDate, endDate, key, pageable);
    }

    @Override
    public Page<Object[]> findAllUnsetEstWithUsr(String startDate, String endDate, String key, Pageable pageable) {
        return tblestRepository.findAllUnsetEstWithUsr(startDate, endDate, key, pageable);
    }
}
