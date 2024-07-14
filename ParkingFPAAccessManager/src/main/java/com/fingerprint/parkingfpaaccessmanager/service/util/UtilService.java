package com.fingerprint.parkingfpaaccessmanager.service.util;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonStringString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UtilService {

    ResponseJsonStringString getDateAndHourFormated(LocalDateTime localDateTime);

    BigDecimal getTotalForDateRange(LocalDateTime startDate, LocalDateTime endDate, int type);

}
