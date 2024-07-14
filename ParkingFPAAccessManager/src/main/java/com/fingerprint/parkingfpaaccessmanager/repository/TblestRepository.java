package com.fingerprint.parkingfpaaccessmanager.repository;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblestRepository extends JpaRepository<Tblest, Long> {

    boolean existsTblestByCveest(long cveest);

    Tblest findTblestByCveest(long cveest);

    @Query(value = """
            SELECT t.cveest,t.entrydate,t.exitdate,t.total
            FROM tblest t
            WHERE t.cveusr IS NULL
              AND t.idcar IS NULL
              AND t.entrydate BETWEEN\s
                  IFNULL(STR_TO_DATE(:startDate, '%Y-%m-%d'), '1900-01-01') AND\s
                  IFNULL(STR_TO_DATE(:endDate, '%Y-%m-%d'), '9999-12-31')""", countQuery = """
            SELECT COUNT(*)
            FROM (
                SELECT t.cveest, t.entrydate, t.exitdate, t.total
                FROM tblest t
                WHERE t.cveusr IS NULL
                  AND t.idcar IS NULL
                  AND t.entrydate BETWEEN\s
                      IFNULL(STR_TO_DATE(:startDate, '%Y-%m-%d'), '1900-01-01') AND\s
                      IFNULL(STR_TO_DATE(:endDate, '%Y-%m-%d'), '9999-12-31')
            ) AS subquery;""", nativeQuery = true)
    Page<Object[]> findRegularEstByRangePage(@Param("startDate") String startDate,
                                             @Param("endDate") String endDate, Pageable pageable);

    @Query(value = """
            SELECT t.cveusr, u.nameusr, u.loginusr, u.emailusr, t.cveest, t.entrydate, t.idcar
            FROM tblest t
            INNER JOIN tblusr u ON u.cveusr = t.cveusr
            WHERE t.cveusr IS NOT NULL
              AND t.exitdate IS NULL
              AND u.typeusr = 1
              AND t.entrydate BETWEEN\s
                  IFNULL(STR_TO_DATE(:startDate, '%Y-%m-%d'), '1900-01-01') AND\s
                  IFNULL(STR_TO_DATE(:endDate, '%Y-%m-%d'), '9999-12-31')
              AND (
                :key IS NULL OR
                (
                  UPPER(REPLACE(u.nameusr, '-', '')) LIKE CONCAT('%', UPPER(REPLACE(:key, '-', '')), '%')
                  OR REPLACE(u.cveusr, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                  OR REPLACE(u.loginusr, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                  OR REPLACE(u.emailusr, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                  OR REPLACE(t.idcar, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                )
              )
            ORDER BY t.cveusr;""", nativeQuery = true)
    Page<Object[]> findAllActiveEstWithUsr(@Param("startDate") String startDate,
                                           @Param("endDate") String endDate,
                                           @Param("key") String key,
                                           Pageable pageable);

    @Query(value = """
            SELECT t.cveusr, u.nameusr, u.loginusr, u.emailusr, t.cveest, t.entrydate, t.exitdate, t.idcar, t.total
            FROM tblest t
            INNER JOIN tblusr u ON u.cveusr = t.cveusr
            WHERE t.cveusr IS NOT NULL
              AND t.exitdate IS NOT NULL
              AND u.typeusr = 1
              AND t.entrydate BETWEEN\s
                  IFNULL(STR_TO_DATE(:startDate, '%Y-%m-%d'), '1900-01-01') AND\s
                  IFNULL(STR_TO_DATE(:endDate, '%Y-%m-%d'), '9999-12-31')
              AND (
                :key IS NULL OR
                (
                  UPPER(REPLACE(u.nameusr, '-', '')) LIKE CONCAT('%', UPPER(REPLACE(:key, '-', '')), '%')
                  OR REPLACE(u.cveusr, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                  OR REPLACE(u.loginusr, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                  OR REPLACE(u.emailusr, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                  OR REPLACE(t.idcar, '-', '') LIKE CONCAT('%', REPLACE(:key, '-', ''), '%')
                )
              )
            ORDER BY t.cveusr;""", nativeQuery = true)
    Page<Object[]> findAllUnsetEstWithUsr(@Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("key") String key,
                                          Pageable pageable);
}

