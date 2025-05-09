package com.fifty.kosmos.controller;

import com.fifty.kosmos.model.Consultorio;
import com.fifty.kosmos.service.ConsultorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/consultorios")
public class ConsultoriosController {

    private final ConsultorioService consultorioService;

    @Autowired
    public ConsultoriosController(ConsultorioService consultorioService) {
        this.consultorioService = consultorioService;
    }

    @GetMapping
    public List<Consultorio> consultorios() {
        return consultorioService.getAllConsultorios();
    }
}
