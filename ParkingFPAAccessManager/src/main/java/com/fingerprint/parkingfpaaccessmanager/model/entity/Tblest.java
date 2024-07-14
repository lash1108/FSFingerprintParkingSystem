package com.fingerprint.parkingfpaaccessmanager.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBLEST")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Tblest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CVEEST")
    private long cveest;

    @ManyToOne
    @JoinColumn(name = "CVEUSR", referencedColumnName = "CVEUSR")
    private Tblusr tblusr;

    @Column(name = "IDCAR", length = 100)
    private String idcar;

    @Column(name = "ENTRYDATE")
    private LocalDateTime entrydate;

    @Column(name = "EXITDATE")
    private LocalDateTime exitdate;

    @Column(name = "TOTAL", precision = 10, scale = 2)
    private BigDecimal total;

}
