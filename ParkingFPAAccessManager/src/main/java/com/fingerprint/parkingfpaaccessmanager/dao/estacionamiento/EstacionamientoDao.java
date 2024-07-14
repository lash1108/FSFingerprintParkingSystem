package com.fingerprint.parkingfpaaccessmanager.dao.estacionamiento;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EstacionamientoDao {

    Tblest saveOrUpdateTblest(Tblest tblest);

    List<Tblest> findAllTblest();

    boolean existsTblestByCveest(long cveest);

    Tblest findTblestByCveest(long cveest);

    Page<Object[]> findRegularEstByRangePage(String startDate, String endDate, Pageable pageable);

    Page<Object[]> findAllActiveEstWithUsr(String startDate, String endDate, String key, Pageable pageable);

    Page<Object[]> findAllUnsetEstWithUsr(String startDate, String endDate, String key, Pageable pageable);

}
