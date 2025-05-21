package com.banula.ocpi.mapper;

import com.banula.ocpi.model.CDR;
import com.banula.ocpi.model.dto.CdrDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CdrMapper {

    static CDR toCdrEntity(CdrDTO cdrDTO) {
        if (cdrDTO == null) return null;
        return CDR.builder()
                .countryCode(cdrDTO.getCountryCode())
                .partyId(cdrDTO.getPartyId())
                .startDateTime(cdrDTO.getStartDateTime())
                .endDateTime(cdrDTO.getEndDateTime())
                .sessionId(cdrDTO.getSessionId())
                .cdrToken(cdrDTO.getCdrToken())
                .authMethod(cdrDTO.getAuthMethod())
                .cdrLocation(cdrDTO.getCdrLocation())
                .currency(cdrDTO.getCurrency())
                .tariffs(cdrDTO.getTariffs())
                .chargingPeriods(cdrDTO.getChargingPeriods())
                .signedData(cdrDTO.getSignedData())
                .totalCost(cdrDTO.getTotalCost())
                .totalEnergy(cdrDTO.getTotalEnergy())
                .totalEnergyCost(cdrDTO.getTotalEnergyCost())
                .totalParkingTime(cdrDTO.getTotalParkingTime())
                .totalParkingCost(cdrDTO.getTotalParkingCost())
                .totalTime(cdrDTO.getTotalTime())
                .lastUpdated(cdrDTO.getLastUpdated())
                .id(cdrDTO.getId())
                .meterId(cdrDTO.getMeterId())
                .authorizationReference(cdrDTO.getAuthorizationReference())
                .totalFixedCost(cdrDTO.getTotalFixedCost())
                .totalTime(cdrDTO.getTotalTime())
                .totalTimeCost(cdrDTO.getTotalTimeCost())
                .totalReservationCost(cdrDTO.getTotalReservationCost())
                .remark(cdrDTO.getRemark())
                .invoiceReferenceId(cdrDTO.getInvoiceReferenceId())
                .credit(cdrDTO.getCredit())
                .creditReferenceId(cdrDTO.getCreditReferenceId())
                .build();
    }

    static CdrDTO toCdrDTO(CDR cdr) {
        if (cdr == null) return null;
        return CdrDTO.builder()
                .countryCode(cdr.getCountryCode())
                .partyId(cdr.getPartyId())
                .startDateTime(cdr.getStartDateTime())
                .endDateTime(cdr.getEndDateTime())
                .sessionId(cdr.getSessionId())
                .cdrToken(cdr.getCdrToken())
                .authMethod(cdr.getAuthMethod())
                .cdrLocation(cdr.getCdrLocation())
                .currency(cdr.getCurrency())
                .tariffs(cdr.getTariffs())
                .chargingPeriods(cdr.getChargingPeriods())
                .signedData(cdr.getSignedData())
                .totalCost(cdr.getTotalCost())
                .totalEnergy(cdr.getTotalEnergy())
                .totalEnergyCost(cdr.getTotalEnergyCost())
                .totalParkingTime(cdr.getTotalParkingTime())
                .totalParkingCost(cdr.getTotalParkingCost())
                .totalTime(cdr.getTotalTime())
                .lastUpdated(cdr.getLastUpdated())
                .id(cdr.getId())
                .meterId(cdr.getMeterId())
                .authorizationReference(cdr.getAuthorizationReference())
                .totalFixedCost(cdr.getTotalFixedCost())
                .totalTime(cdr.getTotalTime())
                .totalTimeCost(cdr.getTotalTimeCost())
                .totalReservationCost(cdr.getTotalReservationCost())
                .remark(cdr.getRemark())
                .invoiceReferenceId(cdr.getInvoiceReferenceId())
                .credit(cdr.getCredit())
                .creditReferenceId(cdr.getCreditReferenceId())
                .build();
    }

    static List<CdrDTO> toListCdrDTO(List<CDR> cdrList) {
        return cdrList == null ? null : cdrList.stream()
                .map(CdrMapper::toCdrDTO)
                .collect(Collectors.toList());
    }

    static List<CDR> toListCdrEntity(List<CdrDTO> cdrDTOList) {
        return cdrDTOList == null ? null : cdrDTOList.stream()
                .map(CdrMapper::toCdrEntity)
                .collect(Collectors.toList());
    }
}
