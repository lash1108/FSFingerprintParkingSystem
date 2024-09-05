package com.fingerprint.parkingfpaaccessmanager.repository;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblregistryRepository extends JpaRepository<Tblregistry, Long> {

    Tblregistry findTblregistryByCvereg(long cvereg);
}
