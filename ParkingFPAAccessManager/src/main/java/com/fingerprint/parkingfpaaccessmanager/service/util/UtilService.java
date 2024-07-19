package com.fingerprint.parkingfpaaccessmanager.service.util;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonLongString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonStringString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UtilService {

    public ResponseJsonString getTotalTimeForDateRange(LocalDateTime startDate, LocalDateTime endDate);

    ResponseJsonStringString getDateAndHourFormated(LocalDateTime localDateTime);

    BigDecimal getTotalForDateRange(LocalDateTime startDate, LocalDateTime endDate, int type);

    BigDecimal getTotalWithIva(BigDecimal subTotal);

}
