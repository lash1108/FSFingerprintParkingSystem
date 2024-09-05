package com.fingerprint.parkingfpaaccessmanager.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "tblest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Tblregistry> tblregs;

    @Column(name = "ENTRYDATE")
    private LocalDateTime entrydate;

    @Column(name = "EXITDATE")
    private LocalDateTime exitdate;

    @Column(name = "TOTAL", precision = 10, scale = 2)
    private BigDecimal total;

}
