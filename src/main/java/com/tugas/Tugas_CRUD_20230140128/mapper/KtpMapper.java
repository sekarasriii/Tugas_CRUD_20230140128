package com.tugas.Tugas_CRUD_20230140128.mapper;

import com.tugas.Tugas_CRUD_20230140128.model.dto.KtpDto;
import com.tugas.Tugas_CRUD_20230140128.model.entity.Ktp;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface KtpMapper {

    // Mengubah Entity menjadi DTO
    KtpDto toDto(Ktp ktp);

    // Mengubah DTO menjadi Entity
    Ktp toEntity(KtpDto ktpDto);

    // Mengubah List Entity menjadi List DTO (berguna saat get all KTP)
    List<KtpDto> toDtoList(List<Ktp> ktpList);
}
