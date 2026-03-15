package com.tugas.Tugas_CRUD_20230140128.repository;

import com.tugas.Tugas_CRUD_20230140128.entity.Ktp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KtpRepository extends JpaRepository<Ktp, Integer> {
    // Custom method untuk ngecek apakah nomor KTP sudah dipakai (buat validasi nanti)
    boolean existsByNomorKtp(String nomorKtp);

    // Custom method untuk mencari KTP berdasarkan nomornya
    Optional<Ktp> findByNomorKtp(String nomorKtp);
}
