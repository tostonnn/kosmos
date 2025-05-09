package com.fifty.kosmos.dao;

import com.fifty.kosmos.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioDao extends JpaRepository<Consultorio, Long> {
}
