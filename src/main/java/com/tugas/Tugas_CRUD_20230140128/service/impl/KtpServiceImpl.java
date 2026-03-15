package com.tugas.Tugas_CRUD_20230140128.service.impl;

import com.tugas.Tugas_CRUD_20230140128.model.dto.KtpDto;
import com.tugas.Tugas_CRUD_20230140128.model.entity.Ktp;
import com.tugas.Tugas_CRUD_20230140128.mapper.KtpMapper;
import com.tugas.Tugas_CRUD_20230140128.repository.KtpRepository;
import com.tugas.Tugas_CRUD_20230140128.service.KtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KtpServiceImpl implements KtpService {

    private final KtpRepository ktpRepository;
    private final KtpMapper ktpMapper;

    @Override
    public KtpDto createKtp(KtpDto ktpDto) {
        // Validasi jika Nomor KTP sudah ada
        if (ktpRepository.existsByNomorKtp(ktpDto.getNomorKtp())) {
            throw new RuntimeException("Nomor KTP sudah terdaftar!");
        }
        Ktp ktp = ktpMapper.toEntity(ktpDto);
        Ktp savedKtp = ktpRepository.save(ktp);
        return ktpMapper.toDto(savedKtp);
    }

    @Override
    public List<KtpDto> getAllKtp() {
        List<Ktp> ktpList = ktpRepository.findAll();
        return ktpMapper.toDtoList(ktpList);
    }

    @Override
    public KtpDto getKtpById(Integer id) {
        Ktp ktp = ktpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data KTP tidak ditemukan!"));
        return ktpMapper.toDto(ktp);
    }

    @Override
    public KtpDto updateKtp(Integer id, KtpDto ktpDto) {
        Ktp existingKtp = ktpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data KTP tidak ditemukan!"));

        // Cek jika nomor KTP diubah dan nomor baru sudah dipakai orang lain
        if (!existingKtp.getNomorKtp().equals(ktpDto.getNomorKtp()) &&
                ktpRepository.existsByNomorKtp(ktpDto.getNomorKtp())) {
            throw new RuntimeException("Nomor KTP baru sudah terdaftar!");
        }

        existingKtp.setNomorKtp(ktpDto.getNomorKtp());
        existingKtp.setNamaLengkap(ktpDto.getNamaLengkap());
        existingKtp.setAlamat(ktpDto.getAlamat());
        existingKtp.setTanggalLahir(ktpDto.getTanggalLahir());
        existingKtp.setJenisKelamin(ktpDto.getJenisKelamin());

        Ktp updatedKtp = ktpRepository.save(existingKtp);
        return ktpMapper.toDto(updatedKtp);
    }

    @Override
    public void deleteKtp(Integer id) {
        if (!ktpRepository.existsById(id)) {
            throw new RuntimeException("Data KTP tidak ditemukan!");
        }
        ktpRepository.deleteById(id);
    }
}