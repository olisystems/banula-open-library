package com.banula.openlib.ocpi.mapper;

import com.banula.openlib.ocpi.model.CDR;
import com.banula.openlib.ocpi.model.dto.CdrDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CdrMapper {

    static CDR toCdrEntity(CdrDTO cdrDTO) {
        if (cdrDTO == null) {
            return null;
        }
        CDR cdr = new CDR();
        BeanUtils.copyProperties(cdrDTO, cdr);
        return cdr;
    }

    static CdrDTO toCdrDTO(CDR cdr) {
        if (cdr == null)
            return null;
        CdrDTO cdrDTO = new CdrDTO();
        BeanUtils.copyProperties(cdr, cdrDTO);
        return cdrDTO;
    }

    static List<CdrDTO> toListCdrDTO(List<CDR> cdrList) {
        return cdrList == null ? null
                : cdrList.stream()
                        .map(CdrMapper::toCdrDTO)
                        .collect(Collectors.toList());
    }

    static List<CDR> toListCdrEntity(List<CdrDTO> cdrDTOList) {
        return cdrDTOList == null ? null
                : cdrDTOList.stream()
                        .map(CdrMapper::toCdrEntity)
                        .collect(Collectors.toList());
    }
}
