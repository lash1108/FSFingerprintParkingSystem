package com.fingerprint.parkingfpaaccessmanager.service.util;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonStringString;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UtilServiceImp implements UtilService {


    @Override
    public ResponseJsonStringString getDateAndHourFormated(LocalDateTime localDateTime) {
        ResponseJsonStringString responseJson = new ResponseJsonStringString();

        // Definir los formateadores para la fecha y la hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Formatear la fecha y la hora
        String fecha = localDateTime.format(dateFormatter);
        String hora = localDateTime.format(timeFormatter);

        // Establecer los valores en el objeto de respuesta
        responseJson.setValue1(fecha);
        responseJson.setValue2(hora);

        return responseJson;
    }

    @Override
    public BigDecimal getTotalForDateRange(LocalDateTime startDate, LocalDateTime endDate, int type) {
        // Obtenemos la duración en segundos entre las dos fechas
        Duration duration = Duration.between(startDate, endDate);
        long seconds = duration.getSeconds();

        BigDecimal price;
        if (seconds < 600) {
            price = BigDecimal.ZERO;
        } else if (seconds <= 3600 && type == 1) {
            price = BigDecimal.valueOf(20.0);
        } else if (seconds <= 3600 && type == 2) {
            price = BigDecimal.valueOf(10.0);
        } else if (type == 2) {
            long hours = seconds / 3600;
            price = BigDecimal.valueOf(10.0).multiply(BigDecimal.valueOf(hours));
        } else {
            // Calculamos la cantidad de horas completas y multiplicamos por 20
            long hours = seconds / 3600;
            price = BigDecimal.valueOf(20.0).multiply(BigDecimal.valueOf(hours));
        }

        return price;
    }

}
