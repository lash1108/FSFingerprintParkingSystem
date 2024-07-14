package com.fingerprint.parkingfpaaccessmanager.service.registro;

import com.fingerprint.parkingfpaaccessmanager.dao.Registro.RegistroDao;
import com.fingerprint.parkingfpaaccessmanager.dao.estacionamiento.EstacionamientoDao;
import com.fingerprint.parkingfpaaccessmanager.dao.usuario.UsuarioDao;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblest;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonStringString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonStringString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import com.fingerprint.parkingfpaaccessmanager.service.util.UtilService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegistroServiceImp implements RegistroService {

    final
    RegistroDao registroDao;

    final
    UsuarioDao usuarioDao;

    final
    EstacionamientoDao estacionamientoDao;

    final
    UtilService utilService;

    public RegistroServiceImp(RegistroDao registroDao, UsuarioDao usuarioDao, EstacionamientoDao estacionamientoDao, UtilService utilService) {
        this.registroDao = registroDao;
        this.usuarioDao = usuarioDao;
        this.estacionamientoDao = estacionamientoDao;
        this.utilService = utilService;
    }

    @Override
    public ResponseJsonStringString findLastRegistryRecord() {
        ResponseJsonStringString responseJson = new ResponseJsonStringString();
        List<Object[]> datos = registroDao.findLastEdoRegistryRecord();
        for (Object[] data : datos) {
            responseJson.setValue1(String.valueOf(data[0]));
            responseJson.setValue2(String.valueOf(data[1]));
        }
        return responseJson;
    }

    @Override
    public ResponseJsonGeneric setNewRegistryByToken(ConsumeJsonString consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String token = consume.getName();
        if (token == null) {
            return response.badCredencialsError("No se proporcoono un token");
        }

        if (registroDao.existsTblregistryByTokenusr(token)) {
            return response.badCredencialsError("ya existe un usuario con ese token");
        }

        Tblregistry tblregistry = new Tblregistry();
        tblregistry.setEdoregistry("0");
        tblregistry.setEntryDate(LocalDateTime.now());
        tblregistry.setTokenusr(token);

        registroDao.createOrUpdateRegistry(tblregistry);
        responseMap.put("registro", tblregistry);
        responseMap.put("fecha", utilService.getDateAndHourFormated(tblregistry.getEntryDate()));

        return response.okResponse("Registro creado exitosamente", responseMap);
    }

    @Override
    public ResponseJsonGeneric findRegistryByToken(ConsumeJsonStringString consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String token = consume.getValue1();
        String edo = consume.getValue2();
        if (edo == null || edo.isEmpty()) {
            edo = "1";
        }
        if (token == null) {
            return response.badCredencialsError("No se proporcionó un token");
        }
        if (!registroDao.existsTblregistryByTokenusr(token)) {
            return response.badCredencialsError("No existe el registro con token: " + token);
        }

        Tblregistry tblregistry = registroDao.findByTokenusr(token);
        if (tblregistry == null) {
            return response.notFoundResponse("No se pudo localizar el registro con token: " + token);
        }
        tblregistry.setEdoregistry(edo);
        registroDao.createOrUpdateRegistry(tblregistry);

        responseMap.put("registro", tblregistry);
        responseMap.put("fecha", utilService.getDateAndHourFormated(tblregistry.getEntryDate()));

        return response.okResponse("Registro Encontrado", responseMap);
    }

    @Override
    public ResponseJsonGeneric unsetRegistryByToken(ConsumeJsonString consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String token = consume.getName();
        if (token == null) {
            return response.badCredencialsError("No se proporcionó un token");
        }
        if (!registroDao.existsTblregistryByTokenusr(token)) {
            return response.badCredencialsError("No existe el registro con token: " + token);
        }
        Tblregistry tblregistry = registroDao.findByTokenusr(token);
        if (tblregistry == null) {
            return response.notFoundResponse("No se pudo localizar el registro con token: " + token);
        }

        Tblest est = new Tblest();
        LocalDateTime entryDate = tblregistry.getEntryDate();
        LocalDateTime exitDate = LocalDateTime.now();
        est.setEntrydate(entryDate);
        est.setExitdate(exitDate);
        est.setTotal(utilService.getTotalForDateRange(entryDate, exitDate, 1));

        estacionamientoDao.saveOrUpdateTblest(est);

        registroDao.deleteTblRegistryByCveregistry(tblregistry.getCvereg());
        responseMap.put("registro", est);
        responseMap.put("fechaEntrada", utilService.getDateAndHourFormated(est.getEntrydate()).getValue1());
        responseMap.put("horaEntrada", utilService.getDateAndHourFormated(est.getEntrydate()).getValue2());
        responseMap.put("fechaSalida", utilService.getDateAndHourFormated(est.getExitdate()).getValue1());
        responseMap.put("horaSalida", utilService.getDateAndHourFormated(est.getExitdate()).getValue2());

        return response.okResponse("Registro migrado exitosamente", responseMap);
    }

    @Override
    public ResponseJsonGeneric findAllRegistries() {
        ResponseJsonHandler response = new ResponseJsonHandler();
        List<Tblregistry> registryList = registroDao.findAllTblregistry();
        return response.okResponse("Registros activos", registryList);
    }


}
