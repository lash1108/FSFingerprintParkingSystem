package com.fingerprint.parkingfpaaccessmanager.service.Searchers;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;

public interface SearchService {

    ResponseJsonGeneric SearchEstByRangeKeyOrType(ConsumeJsonGeneric consumeJsonGeneric);
}
