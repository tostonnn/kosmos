package com.fifty.kosmos.service;

import com.fifty.kosmos.dao.ConsultorioDao;
import com.fifty.kosmos.model.Consultorio;
import com.fifty.kosmos.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultorioService {

    private final ConsultorioDao consultorioDao;

    @Autowired
    public ConsultorioService(ConsultorioDao consultorioDao) {
        this.consultorioDao = consultorioDao;
    }

    public List<Consultorio> getAllConsultorios() {
        return consultorioDao.findAll();
    }
}
