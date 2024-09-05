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
    public List<Tblest> findActiveEstWithToken(String token) {
        return tblestRepository.findActiveEstWithToken(token);
    }

    @Override
    public boolean existActiveEstWithToken(String token) {
        return tblestRepository.existsActiveEstWithToken(token) > 0;
    }

    @Override
    public Tblest saveOrUpdateTblest(Tblest tblest) {
        return tblestRepository.save(tblest);
    }

    @Override
    public Tblest findTblestByCveest(long cveest) {
        return tblestRepository.findTblestByCveest(cveest);
    }

    @Override
    public Page<Object[]> findEstByRangeTypeOrKey(String startDate, String endDate, String key, String type,
                                                  Pageable pageable, long actFlag) {
        if (actFlag == 0) {
            return tblestRepository.findActiveEstByRangeTypeOrKey(startDate, endDate, key, type, pageable);
        } else if (actFlag == 1) {
            return tblestRepository.findUnActiveEstByRangeTypeOrKey(startDate, endDate, key, type, pageable);
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
