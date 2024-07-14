package com.fingerprint.parkingfpaaccessmanager.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(name = "EDOREGISTRY", length = 1)
    private String edoregistry;

    @Column(name = "ENTRYDATE")
    private LocalDateTime entryDate;

    @Column(name = "TOKENUSR", length = 20, unique = true)
    private String tokenusr;
}
