package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los vendedores.
 * Esta clase proporciona métodos para:
 * - Obtener todos los vendedores.
 * - Obtener los vendedores activos.
 * - Buscar un vendedor por su ID.
 * - Buscar un vendedor por el ID de la persona.
 * - Buscar un vendedor por su nombre de usuario.
 * - Guardar un nuevo vendedor.
 * - Actualizar los detalles de un vendedor existente.
 * - Verificar la existencia de un vendedor por su ID o nombre de usuario.
 * - Eliminar un vendedor (desactivarlo).
 */
@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    /**
     * Obtiene todos los vendedores almacenados en la base de datos.
     *
     * Este método devuelve una lista con todos los vendedores registrados en el repositorio.
     *
     * @return Una lista de todos los vendedores.
     */
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    /**
     * Obtiene todos los vendedores activos.
     *
     * Este método devuelve una lista con todos los vendedores activos que tienen el rol de "vendedor" registrado en el repositorio.
     *
     * @return Una lista de todos los vendedores activos.
     */
    public List<Seller> getAllActiveSellers() {
        return sellerRepository.findByActiveTrueAndRole(EnumRole.ROLE_SELLER);
    }

    /**
     * Busca un vendedor por su número de identificación.
     *
     * Este método consulta la base de datos buscando un vendedor cuyo número de identificación coincida con el proporcionado.
     *
     * @param id El número de identificación del vendedor a buscar.
     * @return Un {@code Optional} que contiene el vendedor encontrado, o {@code Optional.empty()} si no se encuentra ninguno con ese número de identificación.
     */
    public Optional<Seller> getSellerById(String id) {
        return sellerRepository.findById(id);
    }

    /**
     * Busca un vendedor por su número de identificación de persona.
     *
     * Este método consulta la base de datos buscando un vendedor cuyo número de identificación de persona coincida con el proporcionado.
     *
     * @param personId El número de identificación de persona del vendedor.
     * @return Un {@code Optional} que contiene el vendedor encontrado, o {@code Optional.empty()} si no se encuentra ninguno con ese número de identificación de persona.
     */
    public Optional<Seller> getSellerByPersonId(String personId) {
        return sellerRepository.findByIdNumber(personId);
    }

    /**
     * Busca un vendedor por su nombre de usuario.
     *
     * Este método consulta la base de datos buscando un vendedor cuyo nombre de usuario coincida con el proporcionado.
     *
     * @param username El nombre de usuario del vendedor.
     * @return Un {@code Optional} que contiene el vendedor encontrado, o {@code Optional.empty()} si no se encuentra ninguno con ese nombre de usuario.
     */
    public Optional<Seller> getSellerByUsername(String username) {
        return sellerRepository.getByUsername(username);
    }

    /**
     * Guarda un nuevo vendedor en la base de datos.
     *
     * Este método guarda una nueva instancia de la clase {@code Seller} en el repositorio.
     *
     * @param seller El vendedor que se desea guardar.
     * @return El vendedor guardado, incluyendo el ID asignado si es la primera vez que se guarda.
     */
    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    /**
     * Actualiza los detalles de un vendedor existente.
     *
     * Este método busca un vendedor con el número de identificación proporcionado y, si se encuentra, actualiza sus detalles con los valores proporcionados en {@code sellerDetails}.
     * Luego guarda los detalles actualizados en el repositorio.
     *
     * @param id El número de identificación del vendedor a actualizar.
     * @param sellerDetails Un objeto {@code Seller} que contiene los nuevos detalles del vendedor.
     * @return Un {@code Optional} que contiene el vendedor actualizado, o {@code Optional.empty()} si no se encuentra un vendedor con ese número de identificación.
     */
    public Optional<Seller> updateSeller(String id, Seller sellerDetails) {
        return sellerRepository.findByIdNumber(id).map(seller -> {
            seller.setFirstName(sellerDetails.getFirstName());
            seller.setLastName(sellerDetails.getLastName());
            seller.setEmail(sellerDetails.getEmail());
            seller.setAddress(sellerDetails.getAddress());
            seller.setPhoneNumber(sellerDetails.getPhoneNumber());
            seller.setDocumentType(sellerDetails.getDocumentType());
            seller.setUsername(sellerDetails.getUsername());
            return sellerRepository.save(seller);
        });
    }

    /**
     * Verifica si existe un vendedor con el número de identificación proporcionado.
     *
     * Este método consulta el repositorio para verificar si existe un vendedor con el número de identificación dado.
     *
     * @param id El número de identificación del vendedor a verificar.
     * @return {@code true} si el vendedor existe, {@code false} si no.
     */
    public boolean existsById(String id) {
        return sellerRepository.existsByIdNumber(id);
    }

    /**
     * Verifica si existe un vendedor con el nombre de usuario proporcionado.
     *
     * Este método consulta el repositorio para verificar si existe un vendedor con el nombre de usuario dado.
     *
     * @param username El nombre de usuario del vendedor a verificar.
     * @return {@code true} si el vendedor existe, {@code false} si no.
     */
    public boolean existsByUsername(String username){
        return sellerRepository.existsByUsername(username);
    }

    /**
     * Elimina un vendedor desactivándolo.
     *
     * Este método busca un vendedor con el número de identificación proporcionado y, si se encuentra, desactiva su estado (estableciendo el campo {@code active} a {@code false}).
     *
     * @param id El número de identificación del vendedor a desactivar.
     * @param token El token de autenticación necesario para realizar la operación.
     * @return {@code true} si el vendedor fue desactivado exitosamente, o {@code false} si no se encuentra un vendedor con ese número de identificación.
     */
        public boolean deleteSeller(String id, String token) {
        return sellerRepository.findByIdNumber(id).map(seller -> {
            seller.setActive(false);
            sellerRepository.save(seller);
            return true;
        }).orElse(false);
    }


}
