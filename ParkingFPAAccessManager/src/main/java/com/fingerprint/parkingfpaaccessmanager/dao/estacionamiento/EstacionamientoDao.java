package com.fingerprint.parkingfpaaccessmanager.dao.estacionamiento;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EstacionamientoDao {

    List<Tblest> findActiveEstWithToken(String token);

    boolean existActiveEstWithToken(String token);

    Tblest saveOrUpdateTblest(Tblest tblest);

    Tblest findTblestByCveest(long cveest);

    Page<Object[]> findEstByRangeTypeOrKey(String startDate, String endDate, String key, String type,
                                           Pageable pageable, long actFlag);

}
