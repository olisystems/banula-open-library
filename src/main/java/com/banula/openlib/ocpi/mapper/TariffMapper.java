package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.Tariff;
import com.banula.openlib.ocpi.model.dto.TariffDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TariffMapper {
        static Tariff toTariffEntity(TariffDTO tariffDTO) {
                if (tariffDTO == null) {
                        return null;
                }
                Tariff tariff = new Tariff();
                BeanUtils.copyProperties(tariffDTO, tariff);
                return tariff;
        }

        static TariffDTO toTariffDTO(Tariff tariff) {
                if (tariff == null) {
                        return null;
                }
                TariffDTO tariffDto = new TariffDTO();
                BeanUtils.copyProperties(tariff, tariffDto);
                return tariffDto;
        }

        static List<TariffDTO> toListTariffDTO(List<Tariff> tariffs) {
                return tariffs == null ? null
                                : tariffs.stream()
                                                .map(TariffMapper::toTariffDTO)
                                                .collect(Collectors.toList());
        }

        static List<Tariff> toListTariff(List<TariffDTO> tariffs) {
                return tariffs == null ? null
                                : tariffs.stream()
                                                .map(TariffMapper::toTariffEntity)
                                                .collect(Collectors.toList());
        }
}
