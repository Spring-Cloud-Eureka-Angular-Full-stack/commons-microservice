package com.lagm.microservices.commons.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lagm.microservices.commons.services.ICommonService;

public class CommonController<E, S extends ICommonService<E>> {

    private Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
    
    @Autowired
    protected S service;

    @GetMapping
    public ResponseEntity<?> findAll() {
        Map<String, Object> response = new HashMap<>();

        try {
            return ResponseEntity.ok().body(this.service.findAll());

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<E> optionalStudent = this.service.findById(id);

            if (optionalStudent.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(optionalStudent.get());

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody E entity) {
        Map<String, Object> response = new HashMap<>();

        try {
            E entityDB = this.service.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(entityDB);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<E> optionalEntity = this.service.findById(id);
            if (optionalEntity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            this.service.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            String message = e.getMessage();
            LOGGER.error(message, e);
            response.put("error", message);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
