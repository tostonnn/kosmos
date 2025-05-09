// DAO corregido
package com.fifty.kosmos.dao;

import com.fifty.kosmos.model.Citas;
import com.fifty.kosmos.model.Consultorio;
import com.fifty.kosmos.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitasDao extends JpaRepository<Citas, Long>, JpaSpecificationExecutor<Citas> {

    // Métodos corregidos
    List<Citas> findByDoctor(Doctor doctor);

    List<Citas> findByConsultorio(Consultorio consultorio);

    List<Citas> findByPaciente(String paciente);

    boolean existsByConsultorioAndHorario(Consultorio consultorio, LocalDateTime horario);

    boolean existsByDoctorAndHorario(Doctor doctor, LocalDateTime horario);

    @Query("SELECT c FROM Citas c WHERE c.paciente = :nombrePaciente AND c.horario BETWEEN :inicio AND :fin AND c.active = true")
    List<Citas> findByPacienteAndHorarioBetween(@Param("nombrePaciente") String nombrePaciente, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(c) FROM Citas c WHERE c.doctor = :doctor AND c.horario BETWEEN :fechaInicio AND :fechaFin AND c.active = true")
    long countByDoctorAndFecha(@Param("doctor") Doctor doctor, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}