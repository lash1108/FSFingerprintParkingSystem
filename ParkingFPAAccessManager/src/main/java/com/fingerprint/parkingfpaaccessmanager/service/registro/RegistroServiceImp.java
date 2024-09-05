package com.fingerprint.parkingfpaaccessmanager.service.registro;

import com.fingerprint.parkingfpaaccessmanager.dao.Registro.RegistroDao;
import com.fingerprint.parkingfpaaccessmanager.dao.estacionamiento.EstacionamientoDao;
import com.fingerprint.parkingfpaaccessmanager.dao.usuario.UsuarioDao;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblest;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblregistry;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import com.fingerprint.parkingfpaaccessmanager.service.util.UtilService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
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
    public ResponseJsonGeneric createOrUpdateRegistryByToken(ConsumeJsonGeneric consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();

        // Validar si consume o consume.getDatos() es nulo
        if (consume == null || consume.getDatos() == null) {
            return response.badRequestResponse("JSON null or malformed", null);
        }

        Map<String, Object> datos = consume.getDatos();

        // Validar si el token fue proporcionado
        String token = (String) datos.get("token");
        if (token == null || token.isEmpty()) {
            return response.badRequestResponse("No token was provided", null);
        }

        // Verificar si existe un usuario con el token proporcionado
        if (!usuarioDao.existsTblusrByTokenusr(token)) {
            return response.notFoundResponse("User not found with token " + token);
        }

        // Procesar la actualización si cvereg está presente
        if (datos.containsKey("cvereg")) {
            return updateRegistry(datos, response);
        }

        // Verificar si ya existe un registro activo para el token
        if (estacionamientoDao.existActiveEstWithToken(token)) {
            return response.badRequestResponse("The token already has an active record", null);
        }

        // Crear nuevo registro
        return createNewRegistry(token, response);
    }

    private ResponseJsonGeneric updateRegistry(Map<String, Object> datos, ResponseJsonHandler response) {
        Integer cvereg = (Integer) datos.get("cvereg");
        Tblregistry tblregistry = registroDao.findRegistryByCvereg(cvereg);
        Tblest tblest = estacionamientoDao.findTblestByCveest(tblregistry.getTblest().getCveest());

        // Actualizar los datos de salida y tiempo de uso
        tblest.setExitdate(LocalDateTime.now());
        Duration duration = Duration.between(tblest.getEntrydate(), LocalDateTime.now());
        tblest.setTotal(BigDecimal.valueOf(duration.toHours()));

        // Guardar y devolver la respuesta
        tblregistry.setTblest(estacionamientoDao.saveOrUpdateTblest(tblest));
        return response.okResponse("Registry updated", registroDao.createOrUpdateRegistry(tblregistry));
    }

    private ResponseJsonGeneric createNewRegistry(String token, ResponseJsonHandler response) {
        // Crear un nuevo registro de estacionamiento
        Tblest tblest = new Tblest();
        tblest.setEntrydate(LocalDateTime.now());

        // Crear un nuevo registro y asociarlo con el usuario
        Tblregistry tblregistry = new Tblregistry();
        tblregistry.setTblusr(usuarioDao.findTblusrByTokenusr(token));
        tblregistry.setTblest(estacionamientoDao.saveOrUpdateTblest(tblest));

        // Guardar y devolver la respuesta
        return response.okResponse("Registry created", registroDao.createOrUpdateRegistry(tblregistry));
    }

    @Override
    public ResponseJsonGeneric findRegistryByToken(ConsumeJsonGeneric consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();
        if (consume != null) {
            if (consume.getDatos() != null) {

                Map<String, Object> data = consume.getDatos();
                Tblest est;

                List<Tblest> estList = estacionamientoDao.findActiveEstWithToken(data.get("token").toString());
                if (estList != null && !estList.isEmpty()) {
                    est = estList.get(0);
                } else {
                    return response.notFoundResponse("registries not found for " + data.get("token").toString());
                }

                Map<String, Object> responseMap = new LinkedHashMap<>();
                responseMap.put("entryDate", utilService.getDateAndHourFormated(est.getEntrydate()));
                return response.okResponse("Registro encontrado", responseMap);
            }
        }
        return response.badRequestResponse("json null or malformed", response);
    }


    @Override
    public ResponseJsonGeneric findAllRegistries() {
        ResponseJsonHandler response = new ResponseJsonHandler();
        List<Tblregistry> registryList = registroDao.findAllTblregistry();
        return response.okResponse("Registros activos", registryList);
    }


}
