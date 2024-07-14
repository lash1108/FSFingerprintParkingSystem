package com.fingerprint.parkingfpaaccessmanager.dao.usuario;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblusr;

import java.util.List;

public interface UsuarioDao {

    boolean existsTblusrByCveusr(long cveusr);

    boolean existsTblusrByLoginusr(String loginUsr);

    boolean existsTblusrByEmailusr(String emailUsr);

    boolean existsTblusrByTokenusr(String tokenUsr);

    Tblusr findTblusrByCveusr(long cveusr);

    Tblusr findTblusrByLoginusrAndPasswordusr(String loginUsr, String passwordUsr);

    Tblusr findTblusrByEmailusrAndPasswordusr(String emailUsr, String passwordUsr);

    Tblusr findTblusrByTokenusr(String token);

    Tblusr createOrUpdateUsuario(Tblusr tblusr);

    void deleteIdCarFromUsrByCveusr(long cveusr);

    void deleteTblusrByCveusr(long cveusr);

    List<Tblusr> findAllUsrByTypeusr(String typeusr);

    List<Object[]> findAllCarIdByCveusr(long cveusr);

    Tblusr findTblustByCveusr(long cveusr);
}
