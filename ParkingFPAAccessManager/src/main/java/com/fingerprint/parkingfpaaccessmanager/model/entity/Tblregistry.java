package com.fingerprint.parkingfpaaccessmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TBLREGISTRY")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Tblregistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cvereg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cveusr", nullable = false)
    @JsonBackReference
    private Tblusr tblusr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cveest", nullable = false)
    @JsonBackReference
    private Tblest tblest;


}
