package com.fingerprint.parkingfpaaccessmanager.service.Searchers;

import com.fingerprint.parkingfpaaccessmanager.dao.estacionamiento.EstacionamientoDao;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImp implements SearchService {

    final
    EstacionamientoDao estacionamientoDao;

    public SearchServiceImp(EstacionamientoDao estacionamientoDao) {
        this.estacionamientoDao = estacionamientoDao;
    }


    @Override
    public ResponseJsonGeneric SearchEstByRangeKeyOrType(ConsumeJsonGeneric consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();
        String startDate, endDate, key, type;
        int page, size;
        Pageable pageable;
        Map<String, Object> results = new HashMap<>();

        var data = consume.getDatos();
        if (data != null) {
            if (data.containsKey("actFlag")) {
                long actFlag = (Integer) data.get("actFlag");
                startDate = data.containsKey("startDate") ? (String) data.get("startDate") : null;
                endDate = data.containsKey("endDate") ? (String) data.get("endDate") : null;
                key = data.containsKey("key") ? (String) data.get("key") : null;
                type = data.containsKey("type") ? (String) data.get("type") : null;
                page = data.containsKey("page") ? (Integer) data.get("page") : 0;
                size = data.containsKey("size") ? (Integer) data.get("size") : 10;
                pageable = PageRequest.of(page, size);

                if (actFlag < 2) {
                    var estResults = estacionamientoDao.findEstByRangeTypeOrKey(startDate, endDate, key, type, pageable, actFlag);
                    results.put("content", estResults.getContent()); // Resultados de la página
                    results.put("page", estResults.getNumber()); // Página actual
                    results.put("size", estResults.getSize()); // Tamaño de la página
                    results.put("totalElements", estResults.getTotalElements()); // Total de elementos
                    results.put("totalPages", estResults.getTotalPages()); // Total de páginas
                    return response.okResponse("data", results);
                } else {
                    return response.badRequestResponse("Json Missing or Malformed: actFlag", consume);
                }
            }
            return response.badRequestResponse("Json Missing or Malformed", consume);
        }
        return response.badRequestResponse("No Json was provided", consume);
    }
}
