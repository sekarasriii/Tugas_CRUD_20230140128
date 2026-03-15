const apiUrl = 'http://localhost:8080/ktp';

$(document).ready(function() {
    // 1. Load data saat halaman pertama kali dibuka
    loadData();

    // 2. Event listener ketika form disubmit (Create / Update)
    $('#formKtp').submit(function(e) {
        e.preventDefault(); // Mencegah halaman refresh
        saveData();
    });

    // 3. Event listener untuk tombol Batal Edit
    $('#btnCancel').click(function() {
        resetForm();
    });
});

// ================= FUNGSI READ (GET) =================
function loadData() {
    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function(response) {
            let html = '';
            if(response.data && response.data.length > 0) {
                $.each(response.data, function(index, ktp) {
                    html += `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${ktp.nomorKtp}</td>
                            <td>${ktp.namaLengkap}</td>
                            <td>${ktp.alamat}</td>
                            <td>${ktp.tanggalLahir}</td>
                            <td>${ktp.jenisKelamin}</td>
                            <td>
                                <button class="btn btn-warning" onclick="editData(${ktp.id})">Edit</button>
                                <button class="btn btn-danger" onclick="deleteData(${ktp.id})">Hapus</button>
                            </td>
                        </tr>
                    `;
                });
            } else {
                html = '<tr><td colspan="7" style="text-align:center;">Belum ada data KTP</td></tr>';
            }
            $('#tableBody').html(html);
        },
        error: function() {
            showNotif('Gagal mengambil data dari server', false);
        }
    });
}

// ================= FUNGSI CREATE & UPDATE (POST / PUT) =================
function saveData() {
    const id = $('#ktpId').val(); // Cek apakah form punya ID tersimpan
    const dataKtp = {
        nomorKtp: $('#nomorKtp').val(),
        namaLengkap: $('#namaLengkap').val(),
        alamat: $('#alamat').val(),
        tanggalLahir: $('#tanggalLahir').val(),
        jenisKelamin: $('#jenisKelamin').val()
    };

    const isUpdate = id !== ''; // Kalau ID ada isinya, berarti mode Update
    const method = isUpdate ? 'PUT' : 'POST';
    const url = isUpdate ? `${apiUrl}/${id}` : apiUrl;

    $.ajax({
        url: url,
        type: method,
        contentType: 'application/json',
        data: JSON.stringify(dataKtp),
        success: function(response) {
            showNotif(response.message, true); // Munculin notif sukses
            resetForm(); // Kosongin form
            loadData();  // Refresh tabel otomatis
        },
        error: function(xhr) {
            // Tangkap pesan error dari API (misal: NIK sudah dipakai)
            let errorMsg = 'Terjadi kesalahan saat menyimpan data';
            if(xhr.responseJSON && xhr.responseJSON.message) {
                errorMsg = xhr.responseJSON.message;
            }
            showNotif(errorMsg, false);
        }
    });
}

// ================= FUNGSI SIAPKAN DATA EDIT =================
function editData(id) {
    $.ajax({
        url: `${apiUrl}/${id}`,
        type: 'GET',
        success: function(response) {
            const ktp = response.data;
            // Isi data ke dalam form
            $('#ktpId').val(ktp.id);
            $('#nomorKtp').val(ktp.nomorKtp);
            $('#namaLengkap').val(ktp.namaLengkap);
            $('#alamat').val(ktp.alamat);
            $('#tanggalLahir').val(ktp.tanggalLahir);
            $('#jenisKelamin').val(ktp.jenisKelamin);

            // Ubah tampilan tombol
            $('#btnSave').text('Update Data');
            $('#btnCancel').show();
            window.scrollTo(0, 0); // Scroll halaman ke atas (ke arah form)
        },
        error: function() {
            showNotif('Gagal mengambil data untuk diedit', false);
        }
    });
}

// ================= FUNGSI HAPUS (DELETE) =================
function deleteData(id) {
    if(confirm('Apakah Anda yakin ingin menghapus data KTP ini?')) {
        $.ajax({
            url: `${apiUrl}/${id}`,
            type: 'DELETE',
            success: function(response) {
                showNotif(response.message, true);
                loadData(); // Refresh tabel
            },
            error: function() {
                showNotif('Gagal menghapus data', false);
            }
        });
    }
}

// ================= FUNGSI BANTUAN (RESET & NOTIF) =================
function resetForm() {
    $('#ktpId').val(''); // Kosongkan ID
    $('#formKtp')[0].reset(); // Kosongkan inputan
    $('#btnSave').text('Simpan Data');
    $('#btnCancel').hide();
}

function showNotif(message, isSuccess) {
    const notifArea = $('#notifArea');
    notifArea.text(message);
    notifArea.removeClass('success error'); // Bersihkan class warna sebelumnya

    if(isSuccess) {
        notifArea.addClass('success'); // Warna hijau
    } else {
        notifArea.addClass('error'); // Warna merah
    }

    notifArea.slideDown();

    // Sembunyikan notifikasi setelah 3.5 detik
    setTimeout(() => {
        notifArea.slideUp();
    }, 3500);
}