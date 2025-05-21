package com.banula.ocpi.mapper;

import com.banula.ocpi.model.Tariff;
import com.banula.ocpi.model.dto.TariffDTO;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TariffMapper {
    static Tariff toTariffEntity(TariffDTO tariffDTO){
        return tariffDTO == null ? null : Tariff
                .builder()
                .id(tariffDTO.getId())
                .countryCode(tariffDTO.getCountryCode())
                .endDateTime(tariffDTO.getEndDateTime())
                .tariffAltUrl(tariffDTO.getTariffAltUrl())
                .type(tariffDTO.getType())
                .energyMix(tariffDTO.getEnergyMix())
                .tariffAltText(tariffDTO.getTariffAltText())
                .lastUpdated(tariffDTO.getLastUpdated())
                .maxPrice(tariffDTO.getMaxPrice())
                .minPrice(tariffDTO.getMinPrice())
                .currency(tariffDTO.getCurrency())
                .elements(tariffDTO.getElements())
                .partyId(tariffDTO.getPartyId())
                .startDateTime(tariffDTO.getStartDateTime())
                .build();
    }
    static TariffDTO toTariffDTO(Tariff tariff){
        return tariff == null ? null : TariffDTO
                .builder()
                .id(tariff.getId())
                .countryCode(tariff.getCountryCode())
                .endDateTime(tariff.getEndDateTime())
                .tariffAltUrl(tariff.getTariffAltUrl())
                .type(tariff.getType())
                .energyMix(tariff.getEnergyMix())
                .tariffAltText(tariff.getTariffAltText())
                .lastUpdated(tariff.getLastUpdated() == null ? LocalDateTime.now() : tariff.getLastUpdated())
                .maxPrice(tariff.getMaxPrice())
                .minPrice(tariff.getMinPrice())
                .currency(tariff.getCurrency())
                .elements(tariff.getElements())
                .partyId(tariff.getPartyId())
                .startDateTime(tariff.getStartDateTime())
                .build();
    }
    static List<TariffDTO> toListTariffDTO(List<Tariff> tariffs) {
        return tariffs == null ? null : tariffs.stream()
                .map(TariffMapper::toTariffDTO)
                .collect(Collectors.toList());
    }

    static List<Tariff> toListTariff(List<TariffDTO> tariffs) {
        return tariffs == null ? null : tariffs.stream()
                .map(TariffMapper::toTariffEntity)
                .collect(Collectors.toList());
    }
}
