package com.fingerprint.parkingfpaaccessmanager.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TBLUSR")
@Getter
@Setter
@NoArgsConstructor
@Data
public class Tblusr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CVEUSR")
    private Long cveusr;

    // Relación OneToMany con Tblregistry
    @OneToMany(mappedBy = "tblusr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tblregistry> tblregs;

    @ElementCollection
    @CollectionTable(name = "USER_IDCAR", joinColumns = @JoinColumn(name = "CVEUSR"))
    @Column(name = "IDCAR")
    private List<String> idcar;

    @Column(name = "NAMEUSR", length = 200)
    @NonNull
    private String nameusr;

    @Column(name = "LOGINUSR", length = 200, unique = true)
    @NonNull
    private String loginusr;

    @Column(name = "EMAILUSR", length = 200, unique = true)
    @NonNull
    private String emailusr;

    @Column(name = "PASSWORDUSR", length = 200)
    @NonNull
    private String passwordusr;

    @Column(name = "TOKENUSR", length = 20, unique = true)
    private String tokenusr;

    @Column(name = "TYPEUSR", length = 1)
    @NonNull
    private String typeusr;
}
