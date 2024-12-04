package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.model.Company;
import com.eucaliptus.springboot_app_person.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las compañías.
 * Esta clase proporciona métodos para:
 * - Obtener todas las compañías.
 * - Buscar compañías por su NIT.
 * - Verificar la existencia de una compañía por su NIT.
 * - Guardar una nueva compañía.
 * - Actualizar los detalles de una compañía existente.
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Obtiene todas las compañías de la base de datos.
     *
     * Este método devuelve una lista con todas las compañías almacenadas en el repositorio.
     *
     * @return Una lista de todas las compañías.
     */
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    /**
     * Busca una compañía por su NIT.
     *
     * Este método consulta la base de datos buscando una compañía cuyo NIT coincida con el proporcionado.
     *
     * @param id El NIT de la compañía a buscar.
     * @return Un {@code Optional} que contiene la compañía encontrada, o {@code Optional.empty()} si no se encuentra ninguna compañía con ese NIT.
     */
    public Optional<Company> findByNit(String id) {
        return companyRepository.findById(id);
    }

    /**
     * Verifica si existe una compañía con el NIT proporcionado.
     *
     * Este método consulta el repositorio para verificar si una compañía con el NIT especificado ya está registrada.
     *
     * @param id El NIT de la compañía a verificar.
     * @return {@code true} si la compañía existe, {@code false} en caso contrario.
     */
    public boolean existsByNItCompany(String id) {
        return companyRepository.existsByNitCompany(id);
    }

    /**
     * Guarda una nueva compañía en la base de datos.
     *
     * Este método guarda una nueva instancia de la clase {@code Company} en el repositorio.
     *
     * @param company La compañía que se desea guardar.
     * @return La compañía guardada, incluyendo el ID asignado si es la primera vez que se guarda.
     */
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    /**
     * Actualiza los detalles de una compañía existente utilizando su NIT.
     *
     * Este método busca una compañía con el NIT proporcionado y, si se encuentra, actualiza sus detalles
     * (nombre, correo electrónico, teléfono, dirección) con la información proporcionada.
     * Luego guarda la compañía actualizada en el repositorio.
     *
     * @param nitCompany El NIT de la compañía a actualizar.
     * @param companyDetails Un objeto {@code Company} que contiene los nuevos detalles de la compañía.
     * @return Un {@code Optional} que contiene la compañía actualizada, o {@code Optional.empty()} si no se encontró la compañía.
     */
    public Optional<Company> update(String nitCompany, Company companyDetails) {
        return companyRepository.findById(nitCompany).map(company -> {
            company.setNameCompany(companyDetails.getNameCompany());
            company.setEmailCompany(companyDetails.getEmailCompany());
            company.setPhoneNumberCompany(companyDetails.getPhoneNumberCompany());
            company.setAddressCompany(companyDetails.getAddressCompany());
            return companyRepository.save(company);
        });
    }
}
