package com.fingerprint.parkingfpaaccessmanager.service.estacionamiento;

import com.fingerprint.parkingfpaaccessmanager.dao.estacionamiento.EstacionamientoDao;
import com.fingerprint.parkingfpaaccessmanager.dao.usuario.UsuarioDao;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblest;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblusr;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import com.fingerprint.parkingfpaaccessmanager.service.util.UtilService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EstacionamientoServiceImp implements EstacionamientoService {

    final
    EstacionamientoDao estacionamientoDao;

    final
    UsuarioDao usuarioDao;

    final
    UtilService utilService;

    public EstacionamientoServiceImp(EstacionamientoDao estacionamientoDao, UsuarioDao usuarioDao, UtilService utilService) {
        this.estacionamientoDao = estacionamientoDao;
        this.usuarioDao = usuarioDao;
        this.utilService = utilService;
    }

    @Override
    public ResponseJsonGeneric createOrUpdateEstacionamiento(ConsumeJsonGeneric consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();
        Map<String, Object> datos = consume.getDatos();

        // Validación inicial de datos
        if (datos == null) {
            return response.badCredencialsError("No puede enviar un json nulo");
        } else if (!datos.containsKey("cveusr")) {
            return response.badCredencialsError("Json mal formado o con datos faltantes");
        } else if (!usuarioDao.existsTblusrByCveusr((Integer) datos.get("cveusr"))) {
            return response.notFoundResponse("No se encuentra el usuario");
        }

        Tblusr usr = usuarioDao.findTblusrByCveusr((Integer) datos.get("cveusr"));
        List<String> placas = usr.getIdcar();
        Map<String, Object> responseMap = new HashMap<>();
        LocalDateTime entryDate = LocalDateTime.now();

        if (datos.containsKey("placa")) {
            // Crear nuevo registro con la placa
            String placa = (String) datos.get("placa");
            if (!placas.contains(placa)) {
                placas.add(placa);
                usuarioDao.createOrUpdateUsuario(usr);
            }

            Tblest est = new Tblest();
            est.setEntrydate(entryDate);
            est.setIdcar(placa);
            est.setTblusr(usr);
            estacionamientoDao.saveOrUpdateTblest(est);

            String entryDateFormated = utilService.getDateAndHourFormated(entryDate).getValue1();
            String entryTimeFormated = utilService.getDateAndHourFormated(entryDate).getValue2();
            responseMap.put("entryDate", entryDateFormated);
            responseMap.put("entryTime", entryTimeFormated);
            responseMap.put("placa", placa);

            return response.okResponse("Registro creado", responseMap);
        } else if (datos.containsKey("cveest")) {
            // Actualizar registro existente
            long cveest = Long.parseLong(datos.get("cveest").toString());
            if (!estacionamientoDao.existsTblestByCveest(cveest)) {
                return response.notFoundResponse("No se encuentra el estacionamiento");
            }

            Tblest est = estacionamientoDao.findTblestByCveest(cveest);
            if (est.getIdcar() == null) {
                return response.badCredencialsError("El registro no tiene una placa asociada");
            }

            LocalDateTime exitDate = LocalDateTime.now();
            entryDate = est.getEntrydate();
            BigDecimal subtotal = utilService.getTotalForDateRange(entryDate, exitDate, 2);
            BigDecimal total = utilService.getTotalWithIva(subtotal);
            est.setExitdate(exitDate);
            est.setTotal(total);
            estacionamientoDao.saveOrUpdateTblest(est);

            placas.remove(est.getIdcar());
            usuarioDao.createOrUpdateUsuario(usr);

            responseMap.put("cveusr", usr.getCveusr());
            responseMap.put("nameUsr", usr.getNameusr());
            responseMap.put("email", usr.getEmailusr());
            responseMap.put("login", usr.getLoginusr());
            responseMap.put("exitDate", utilService.getDateAndHourFormated(exitDate).getValue1());
            responseMap.put("exitTime", utilService.getDateAndHourFormated(exitDate).getValue2());
            responseMap.put("subTotal", subtotal.toString());
            responseMap.put("total", total.toString());


            return response.okResponse("Registro actualizado", responseMap);
        } else {
            return response.badCredencialsError("Json mal formado o con datos faltantes");
        }
    }


    @Override
    public ResponseJsonGeneric chooseEstSearchProvider(ConsumeJsonGeneric consume) {
        ResponseJsonHandler responseJsonHandler = new ResponseJsonHandler();
        if (consume.getDatos() == null) {
            return responseJsonHandler.badCredencialsError("debes enviar un json válido");
        }
        if (!consume.getDatos().containsKey("type")) {
            return responseJsonHandler.badCredencialsError("Debes enviar un tipo de buscador válido");
        }
        Map<String, Object> datos = consume.getDatos();
        String startDate = datos.containsKey("startDate") ? (String) datos.get("startDate") : null;
        String endDate = datos.containsKey("endDate") ? (String) datos.get("endDate") : null;
        String key = datos.containsKey("key") ? (String) datos.get("key") : null;
        int page = datos.containsKey("page") ? (Integer) datos.get("page") : 0;
        int size = datos.containsKey("size") ? (Integer) datos.get("size") : 10;
        int type = (int) datos.get("type");
        Pageable pageable = PageRequest.of(page, size);

        /*1 -> regular | 2 -> active | 3 -> unset*/
        return this.chooseResponseProvider(startDate, endDate, pageable, key, type);
    }

    private ResponseJsonGeneric chooseResponseProvider(String startDate, String endDate, Pageable pageable, String key, int type) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> response = new LinkedHashMap<>();
        Page<Object[]> pageResult;
        List<Map<String, Object>> data = switch (type) {
            case 1 -> {
                pageResult = estacionamientoDao.findRegularEstByRangePage(startDate, endDate, pageable);
                yield this.findAllRegularEst(pageResult);
            }
            case 2 -> {
                pageResult = estacionamientoDao.findAllActiveEstWithUsr(startDate, endDate, key, pageable);
                yield this.findAllActiveEstWithUsr(pageResult);
            }
            case 3 -> {
                pageResult = estacionamientoDao.findAllUnsetEstWithUsr(startDate, endDate, key, pageable);
                yield this.findAllUnsetEstWithUsr(pageResult);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        response.put("data", data);
        response.put("currentPage", pageResult.getNumber()); // Número de la página actual
        response.put("totalItems", pageResult.getTotalElements()); // Total de elementos en todas las páginas
        response.put("totalPages", pageResult.getTotalPages()); // Número total de páginas
        response.put("first", pageResult.isFirst()); // Indica si es la primera página
        response.put("last", pageResult.isLast()); // Indica si es la última página
        response.put("pageSize", pageResult.getSize());

        responseJson.setDatos(response);
        return responseJson;
    }

    private List<Map<String, Object>> findAllRegularEst(Page<Object[]> page) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object[] resultado : page.getContent()) {
            Map<String, Object> datosResultado = new LinkedHashMap<>();
            datosResultado.put("cveest", resultado[0]);

            // Convertir entryDate y exitDate a LocalDateTime
            LocalDateTime fechaEntrada = ((Timestamp) resultado[1]).toLocalDateTime();
            LocalDateTime fechaSalida = ((Timestamp) resultado[2]).toLocalDateTime();

            // Formatear la fecha y hora
            String entryDateStr = utilService.getDateAndHourFormated(fechaEntrada).getValue1();//fechaEntrada.format(dateFormatter);
            String entryTimeStr = utilService.getDateAndHourFormated(fechaEntrada).getValue2();
            String exitDateStr = utilService.getDateAndHourFormated(fechaSalida).getValue1();//fechaSalida.format(dateFormatter);
            String exitTimeStr = utilService.getDateAndHourFormated(fechaSalida).getValue2();

            // Poner los valores en el mapa
            datosResultado.put("entryDate", entryDateStr);
            datosResultado.put("entryTime", entryTimeStr);
            datosResultado.put("exitDate", exitDateStr);
            datosResultado.put("exitTime", exitTimeStr);
            datosResultado.put("total", resultado[3]);

            data.add(datosResultado);
        }
        return data;
    }

    private List<Map<String, Object>> findAllActiveEstWithUsr(Page<Object[]> page) {
        Map<String, Map<String, Object>> groupedData = new LinkedHashMap<>();

        for (Object[] resultado : page.getContent()) {
            String cveusr = this.cveusrString(resultado[0]);  // Convertir cveusr a String usando un método privado

            // Obtener o crear el mapa para cveusr usando un método privado
            Map<String, Object> datosUsuario = getOrCreateUserMap(groupedData, cveusr, resultado);

            // Extraer y formatear fechas
            LocalDateTime fechaEntrada = ((Timestamp) resultado[5]).toLocalDateTime();
            Map<String, Object> datosResultado = getStringObjectMap(resultado, fechaEntrada);
            datosResultado.put("idcar", resultado[6]);

            // Agregar a la lista de datos del usuario
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> datosList = (List<Map<String, Object>>) datosUsuario.get("data");
            datosList.add(datosResultado);
        }

        // Convertir el mapa en una lista de mapas
        return new ArrayList<>(groupedData.values());
    }

    private List<Map<String, Object>> findAllUnsetEstWithUsr(Page<Object[]> page) {
        Map<String, Map<String, Object>> groupedData = new LinkedHashMap<>();

        for (Object[] resultado : page.getContent()) {
            String cveusr = this.cveusrString(resultado[0]);

            // Obtener o crear el mapa para cveusr
            Map<String, Object> datosUsuario = getOrCreateUserMap(groupedData, cveusr, resultado);

            // Extraer y formatear fechas
            LocalDateTime fechaEntrada = ((Timestamp) resultado[5]).toLocalDateTime();
            LocalDateTime fechaSalida = ((Timestamp) resultado[6]).toLocalDateTime();
            String exitDateStr = utilService.getDateAndHourFormated(fechaSalida).getValue1();
            String exitTimeStr = utilService.getDateAndHourFormated(fechaSalida).getValue2();

            // Crear el mapa para la entrada de datos
            Map<String, Object> datosResultado = getStringObjectMap(resultado, fechaEntrada);
            datosResultado.put("exitDate", exitDateStr);
            datosResultado.put("exitTime", exitTimeStr);
            datosResultado.put("idcar", resultado[6]);
            datosResultado.put("total", resultado[7]);

            // Agregar a la lista de datos del usuario
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> datosList = (List<Map<String, Object>>) datosUsuario.get("data");
            datosList.add(datosResultado);
        }

        return new ArrayList<>(groupedData.values());
    }

    private String cveusrString(Object cveusr) {
        return ((Long) cveusr).toString();
    }

    private Map<String, Object> getOrCreateUserMap(Map<String, Map<String, Object>> groupedData, String cveusr, Object[] resultado) {
        return groupedData.computeIfAbsent(cveusr, k -> {
            Map<String, Object> datos = new LinkedHashMap<>();
            datos.put("cveusr", resultado[0]);
            datos.put("nameusr", resultado[1]);
            datos.put("loginusr", resultado[2]);
            datos.put("emailusr", resultado[3]);
            datos.put("data", new ArrayList<Map<String, Object>>());
            return datos;
        });
    }

    private Map<String, Object> getStringObjectMap(Object[] resultado, LocalDateTime fechaEntrada) {
        String entryDateStr = utilService.getDateAndHourFormated(fechaEntrada).getValue1();
        String entryTimeStr = utilService.getDateAndHourFormated(fechaEntrada).getValue2();

        // Crear el mapa para la entrada de datos
        Map<String, Object> datosResultado = new LinkedHashMap<>();
        datosResultado.put("cveest", resultado[4]);
        datosResultado.put("entryDate", entryDateStr);
        datosResultado.put("entryTime", entryTimeStr);
        datosResultado.put("entryDateUnformated", fechaEntrada);
        return datosResultado;
    }

}
