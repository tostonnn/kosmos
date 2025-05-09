package com.fifty.kosmos.controller;

import com.fifty.kosmos.model.Citas;
import com.fifty.kosmos.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/citas")
public class CitasController {

    private final CitaService citaService;

    @Autowired
    public CitasController(CitaService citaService) {
        this.citaService = citaService;
    }

//    @GetMapping
//    public List<Citas> getAllCitas() {
//        return citaService.findAll();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Citas> getCitaById(@PathVariable Long id) {
        return citaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Citas> createCita(@RequestBody Citas cita) {
        try {
            Citas nuevaCita = citaService.crearCita(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Citas> updateCita(@PathVariable Long id, @RequestBody Citas cita) {
        try {
            Citas citaActualizada = citaService.actualizarCita(id, cita);
            return ResponseEntity.ok(citaActualizada);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarCita(@PathVariable Long id) {
        try {
            citaService.cancelarCita(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<Citas> getCitasFiltradas(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long consultorioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return citaService.buscarCitasFiltradas(doctorId, consultorioId, fecha);
    }
}
