package com.fingerprint.parkingfpaaccessmanager.dao.usuario;

import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblusr;
import com.fingerprint.parkingfpaaccessmanager.repository.TblusrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioDaoImp implements UsuarioDao {

    @Autowired
    private TblusrRepository tblusrRepository;

    @Override
    public boolean existsTblusrByCveusr(long cveusr) {
        return tblusrRepository.existsTblusrByCveusr(cveusr);
    }

    @Override
    public boolean existsTblusrByLoginusr(String loginUsr) {
        return tblusrRepository.existsTblusrByLoginusr(loginUsr);
    }

    @Override
    public boolean existsTblusrByEmailusr(String emailUsr) {
        return tblusrRepository.existsTblusrByEmailusr(emailUsr);
    }

    @Override
    public boolean existsTblusrByTokenusr(String tokenUsr) {
        return tblusrRepository.existsTblusrByTokenusr(tokenUsr);
    }

    @Override
    public Tblusr findTblusrByCveusr(long cveusr) {
        return tblusrRepository.findTblusrByCveusr(cveusr);
    }

    @Override
    public Tblusr findTblusrByLoginusrAndPasswordusr(String loginUsr, String passwordUsr) {
        return tblusrRepository.findTblusrByLoginusrAndPasswordusr(loginUsr, passwordUsr);
    }

    @Override
    public Tblusr findTblusrByEmailusrAndPasswordusr(String emailUsr, String passwordUsr) {
        return tblusrRepository.findTblusrByEmailusrAndPasswordusr(emailUsr, passwordUsr);
    }

    @Override
    public Tblusr findTblusrByTokenusr(String token) {
        return tblusrRepository.findTblusrByTokenusr(token);
    }

    @Override
    public Tblusr createOrUpdateUsuario(Tblusr tblusr) {
        return tblusrRepository.save(tblusr);
    }

    @Override
    public void deleteIdCarFromUsrByCveusr(long cveusr) {
        tblusrRepository.deleteIdCarFromUsrByCveusr(cveusr);
    }

    @Override
    public void deleteTblusrByCveusr(long cveusr) {
        tblusrRepository.deleteTblusrByCveusr(cveusr);
    }

    @Override
    public List<Tblusr> findAllUsrByTypeusr(String typeusr) {
        return tblusrRepository.findAllByTypeusr(typeusr);
    }

    @Override
    public List<Object[]> findAllCarIdByCveusr(long cveusr) {
        return tblusrRepository.findAllCarIdByCveusr(cveusr);
    }

    @Override
    public Tblusr findTblustByCveusr(long cveusr) {
        return tblusrRepository.findTblusrByCveusr(cveusr);
    }


}
