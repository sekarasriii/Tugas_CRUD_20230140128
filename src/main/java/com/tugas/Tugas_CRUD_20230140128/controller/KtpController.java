package com.tugas.Tugas_CRUD_20230140128.controller;

import com.tugas.Tugas_CRUD_20230140128.dto.KtpDto;
import com.tugas.Tugas_CRUD_20230140128.service.KtpService;
import com.tugas.Tugas_CRUD_20230140128.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ktp")
@CrossOrigin(origins = "*") // Penting! Biar JQuery Ajax di HTML nanti diizinkan akses API ini
public class KtpController {

    @Autowired
    private KtpService ktpService;

    // POST /ktp: Tambah data KTP baru
    @PostMapping
    public ResponseEntity<ApiResponse<KtpDto>> createKtp(@RequestBody KtpDto ktpDto) {
        try {
            KtpDto createdKtp = ktpService.createKtp(ktpDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "Data KTP berhasil ditambahkan", createdKtp));
        } catch (RuntimeException e) {
            // Menangkap error dari Service (misal: Nomor KTP sudah terdaftar)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage(), null));
        }
    }


}