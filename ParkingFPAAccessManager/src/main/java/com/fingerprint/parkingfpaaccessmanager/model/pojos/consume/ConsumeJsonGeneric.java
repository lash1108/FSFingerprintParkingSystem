package com.fingerprint.parkingfpaaccessmanager.model.pojos.consume;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ConsumeJsonGeneric {
    private Map<String, Object> datos;
}
