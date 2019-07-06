package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Producto;
import io.github.jhipster.application.repository.ProductoRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.application.domain.Producto}.
 */
@RestController
@RequestMapping("/api")
public class ProductoResource {

    private final Logger log = LoggerFactory.getLogger(ProductoResource.class);

    private static final String ENTITY_NAME = "producto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductoRepository productoRepository;

    public ProductoResource(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * {@code POST  /productos} : Create a new producto.
     *
     * @param producto the producto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new producto, or with status {@code 400 (Bad Request)} if the producto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/productos")
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) throws URISyntaxException {
        log.debug("REST request to save Producto : {}", producto);
        if (producto.getId() != null) {
            throw new BadRequestAlertException("A new producto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Producto result = productoRepository.save(producto);
        return ResponseEntity.created(new URI("/api/productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /productos} : Updates an existing producto.
     *
     * @param producto the producto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated producto,
     * or with status {@code 400 (Bad Request)} if the producto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the producto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/productos")
    public ResponseEntity<Producto> updateProducto(@RequestBody Producto producto) throws URISyntaxException {
        log.debug("REST request to update Producto : {}", producto);
        if (producto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Producto result = productoRepository.save(producto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, producto.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /productos} : get all the productos.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productos in body.
     */
    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> getAllProductos(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Productos");
        Page<Producto> page;
        if (eagerload) {
            page = productoRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = productoRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /productos/:id} : get the "id" producto.
     *
     * @param id the id of the producto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the producto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> getProducto(@PathVariable Long id) {
        log.debug("REST request to get Producto : {}", id);
        Optional<Producto> producto = productoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(producto);
    }

    /**
     * {@code DELETE  /productos/:id} : delete the "id" producto.
     *
     * @param id the id of the producto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        log.debug("REST request to delete Producto : {}", id);
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
