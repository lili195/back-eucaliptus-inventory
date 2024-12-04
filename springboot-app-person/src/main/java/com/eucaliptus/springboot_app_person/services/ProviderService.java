package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.model.Provider;
import com.eucaliptus.springboot_app_person.repository.ProviderRepository;
import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los proveedores.
 * Esta clase proporciona métodos para:
 * - Obtener todos los proveedores.
 * - Obtener los proveedores activos.
 * - Buscar un proveedor por su ID.
 * - Buscar un proveedor por el ID de la empresa.
 * - Guardar un nuevo proveedor.
 * - Verificar la existencia de un proveedor por su ID.
 * - Actualizar los detalles de un proveedor existente.
 * - Eliminar un proveedor.
 * - Eliminar un proveedor junto con sus productos.
 */
@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private APIService apiService;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Obtiene todos los proveedores almacenados en la base de datos.
     *
     * Este método devuelve una lista con todos los proveedores registrados en el repositorio.
     *
     * @return Una lista de todos los proveedores.
     */
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    /**
     * Obtiene todos los proveedores activos almacenados en la base de datos.
     *
     * Este método devuelve una lista con todos los proveedores activos registrados en el repositorio.
     *
     * @return Una lista de todos los proveedores activos.
     */
    public List<Provider> getAllActiveProviders() {
        return providerRepository.findByActiveTrue();
    }

    /**
     * Busca un proveedor por su número de identificación.
     *
     * Este método consulta la base de datos buscando un proveedor cuyo número de identificación coincida con el proporcionado.
     *
     * @param id El número de identificación del proveedor a buscar.
     * @return Un {@code Optional} que contiene el proveedor encontrado, o {@code Optional.empty()} si no se encuentra ninguno con ese número de identificación.
     */
    public Optional<Provider> getProviderById(String id) {
        return providerRepository.findByIdNumber(id);
    }

    /**
     * Busca un proveedor activo por el ID de la empresa.
     *
     * Este método consulta la base de datos buscando un proveedor activo cuyo número de identificación de empresa coincida con el proporcionado.
     *
     * @param companyId El número de identificación de la empresa del proveedor.
     * @return Un {@code Optional} que contiene el proveedor encontrado, o {@code Optional.empty()} si no se encuentra ninguno con ese ID de empresa.
     */
    public Optional<Provider> getProviderByCompanyId(String companyId) {
        return providerRepository.findByActiveTrueAndCompany_NitCompany(companyId);
    }

    /**
     * Guarda un nuevo proveedor en la base de datos.
     *
     * Este método guarda una nueva instancia de la clase {@code Provider} en el repositorio.
     *
     * @param provider El proveedor que se desea guardar.
     * @return El proveedor guardado, incluyendo el ID asignado si es la primera vez que se guarda.
     */
    public Provider saveProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    /**
     * Verifica si existe un proveedor con el número de identificación proporcionado.
     *
     * Este método consulta el repositorio para verificar si existe un proveedor con el número de identificación dado.
     *
     * @param id El número de identificación del proveedor a verificar.
     * @return {@code true} si el proveedor existe, {@code false} si no.
     */
    public boolean existsById(String id) {
        return providerRepository.existsByIdNumber(id);
    }

    /**
     * Actualiza los detalles de un proveedor existente.
     *
     * Este método busca un proveedor con el número de identificación proporcionado y, si se encuentra, actualiza sus detalles con los valores proporcionados en {@code providerDetails}.
     * Luego guarda los detalles actualizados en el repositorio.
     *
     * @param id El número de identificación del proveedor a actualizar.
     * @param providerDetails Un objeto {@code Provider} que contiene los nuevos detalles del proveedor.
     * @return Un {@code Optional} que contiene el proveedor actualizado, o {@code Optional.empty()} si no se encuentra un proveedor con ese número de identificación.
     */
    public Optional<Provider> updateProvider(String id, Provider providerDetails) {
        return providerRepository.findByIdNumber(id).map(provider -> {
            provider.setFirstName(providerDetails.getFirstName());
            provider.setLastName(providerDetails.getLastName());
            provider.setEmail(providerDetails.getEmail());
            provider.setAddress(providerDetails.getAddress());
            provider.setPhoneNumber(providerDetails.getPhoneNumber());
            provider.setDocumentType(providerDetails.getDocumentType());
            provider.setBankName(providerDetails.getBankName());
            provider.setBankAccountNumber(providerDetails.getBankAccountNumber());
            provider.setPersonType(providerDetails.getPersonType());
            return providerRepository.save(provider);
        });
    }

    /**
     * Elimina un proveedor desactivándolo.
     *
     * Este método busca un proveedor con el número de identificación proporcionado y, si se encuentra, desactiva su estado (estableciendo el campo {@code active} a {@code false}).
     *
     * @param id El número de identificación del proveedor a desactivar.
     * @return {@code true} si el proveedor fue desactivado exitosamente, o {@code false} si no se encuentra un proveedor con ese número de identificación.
     */
    public boolean deleteProvider(String id) {
        return providerRepository.findByIdNumber(id).map(provider -> {
            provider.setActive(false);
            providerRepository.save(provider);
            return true;
        }).orElse(false);
    }

    /**
     * Elimina un proveedor y todos los productos asociados a ese proveedor.
     *
     * Este método primero elimina al proveedor desactivándolo y luego elimina todos los productos asociados al proveedor a través de una llamada externa a otro servicio.
     *
     * @param idProvider El número de identificación del proveedor a eliminar.
     * @param token El token de autenticación necesario para hacer la llamada al servicio de productos.
     * @return {@code true} si el proveedor y los productos fueron eliminados exitosamente, o {@code false} si ocurrió un error.
     * @throws IllegalArgumentException Si ocurre un error al eliminar el proveedor o los productos.
     */
    @Transactional
    public boolean deleteProviderAndProducts(String idProvider, String token) {
        if (!deleteProvider(idProvider))
            throw new IllegalArgumentException("Error eliminando proveedor");
        if (!deleteProductsFromProvider(idProvider, token))
            throw new IllegalArgumentException("Error eliminando productos");
        return true;
    }

    /**
     * Elimina todos los productos asociados a un proveedor mediante una llamada al servicio de productos.
     *
     * Este método realiza una solicitud HTTP a un servicio externo para eliminar todos los productos asociados a un proveedor.
     *
     * @param idProvider El número de identificación del proveedor cuyos productos serán eliminados.
     * @param token El token de autenticación necesario para hacer la llamada al servicio de productos.
     * @return {@code true} si la eliminación de productos fue exitosa, o {@code false} si ocurrió un error en la llamada.
     */
    private boolean deleteProductsFromProvider(String idProvider, String token){
        HttpEntity<Void> entity = new HttpEntity<>(apiService.getHeader(token));
        ResponseEntity<String> response = restTemplate.exchange(
        ServicesUri.PRODUCT_SERVICE + "/products/deleteProductsByProvider/" + idProvider,
                HttpMethod.DELETE,
                entity,
                String.class
        );
        return response.getStatusCode().is2xxSuccessful();
    }


}
