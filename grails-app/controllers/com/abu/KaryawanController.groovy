package com.abu

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class KaryawanController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Karyawan.list(params), model:[karyawanCount: Karyawan.count()]
    }

    def show(Karyawan karyawan) {
        respond karyawan
    }

    def create() {
        respond new Karyawan(params)
    }

    @Transactional
    def save(Karyawan karyawan) {
        if (karyawan == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (karyawan.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond karyawan.errors, view:'create'
            return
        }

        karyawan.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'karyawan.label', default: 'Karyawan'), karyawan.id])
                redirect karyawan
            }
            '*' { respond karyawan, [status: CREATED] }
        }
    }

    def edit(Karyawan karyawan) {
        respond karyawan
    }

    @Transactional
    def update(Karyawan karyawan) {
        if (karyawan == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (karyawan.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond karyawan.errors, view:'edit'
            return
        }

        karyawan.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'karyawan.label', default: 'Karyawan'), karyawan.id])
                redirect karyawan
            }
            '*'{ respond karyawan, [status: OK] }
        }
    }

    @Transactional
    def delete(Karyawan karyawan) {

        if (karyawan == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        karyawan.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'karyawan.label', default: 'Karyawan'), karyawan.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'karyawan.label', default: 'Karyawan'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
