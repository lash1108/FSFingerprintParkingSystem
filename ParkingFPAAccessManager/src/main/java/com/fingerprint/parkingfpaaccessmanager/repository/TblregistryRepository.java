package com.fingerprint.parkingfpaaccessmanager.repository;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblregistryRepository extends JpaRepository<Tblregistry, Long> {

    @Query(value = """
            SELECT r.tokenusr, r.edoregistry\s
            FROM tblregistry r
            WHERE r.cvereg = (SELECT MAX(cvereg) FROM tblregistry)""", nativeQuery = true)
    List<Object[]> findLastEdoRegistryRecord();

    Tblregistry findByTokenusr(String token);

    boolean existsTblregistryByTokenusr(String token);


}
