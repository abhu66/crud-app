package com.abu

class Karyawan {
	String nama
	String email
	String posisi
	Double gaji
	Date tanggal = new Date() 
    static constraints = {
	email email:true
    }
}
