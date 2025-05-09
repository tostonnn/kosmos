package com.fifty.kosmos.service;

import com.fifty.kosmos.dao.CitasDao;
//import com.fifty.kosmos.exception.IllegalArgumentException;
import com.fifty.kosmos.dao.ConsultorioDao;
import com.fifty.kosmos.dao.DoctorDao;
import com.fifty.kosmos.model.Citas;
import com.fifty.kosmos.model.Consultorio;
import com.fifty.kosmos.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {


    private final CitasDao citasDao;
    private final DoctorDao doctorDao;
    private final ConsultorioDao consultorioDao;

    @Autowired
    public CitaService(CitasDao citasDao, DoctorDao doctorDao, ConsultorioDao consultorioDao) {
        this.citasDao = citasDao;
        this.doctorDao = doctorDao;
        this.consultorioDao = consultorioDao;
    }

    public List<Citas> findAll() {
        return citasDao.findAll();
    }

    public Optional<Citas> findById(Long id) {
        return citasDao.findById(id);
    }

    @Transactional
    public Citas crearCita(Citas cita) {

        Optional<Doctor> optDoctor = doctorDao.findById(cita.getDoctor().getId());
        if (!optDoctor.isPresent()) {
            throw new IllegalArgumentException("Doctor no encontrado.");
        }

        Optional<Consultorio> optconsultorio = consultorioDao.findById(cita.getConsultorio().getId());
        if (!optconsultorio.isPresent()) {
            throw new IllegalArgumentException("Consultorio no encontrado.");
        }

        Doctor doctor = optDoctor.get();
        Consultorio consultorio = optconsultorio.get();

        LocalDateTime horarioConsulta = cita.getHorario();
        String nombrePaciente = cita.getPaciente();

        validarCita(doctor, consultorio, horarioConsulta, nombrePaciente);

        Citas citaNew = new Citas(doctor, consultorio, horarioConsulta, nombrePaciente);
        return citasDao.save(citaNew);
    }

    @Transactional
    public Citas actualizarCita(Long id, Citas citaNew) {
        Optional<Citas> optCita = citasDao.findById(id);

        if (!optCita.isPresent()) {
            throw new IllegalArgumentException("La cita que intentas editar no existe");
        }

        Citas cita = optCita.get();

        if (!cita.isActive()) {
            throw new IllegalArgumentException("No se puede editar una cita cancelada");
        }

        Optional<Doctor> optDoctor = doctorDao.findById(cita.getDoctor().getId());
        if (!optDoctor.isPresent()) {
            throw new IllegalArgumentException("Doctor no encontrado.");
        }
        Doctor doctor = optDoctor.get();

        Optional<Consultorio> optConsultorio = consultorioDao.findById(citaNew.getConsultorio().getId());
        if (!optConsultorio.isPresent()) {
            throw new IllegalArgumentException("Consultorio no encontrado.");
        }
        Consultorio consultorio = optConsultorio.get();

        LocalDateTime horarioConsulta = citaNew.getHorario();
        String nombrePaciente = citaNew.getPaciente();

        cita.setActive(false);
        citasDao.save(cita);

        validarCita(doctor, consultorio, horarioConsulta, nombrePaciente);

        cita.setDoctor(doctor);
        cita.setConsultorio(consultorio);
        cita.setHorario(horarioConsulta);
        cita.setPaciente(nombrePaciente);
        cita.setActive(true);

        return citasDao.save(cita);
    }

    @Transactional
    public void cancelarCita(Long id) {
        Optional<Citas> optCita = citasDao.findById(id);

        if (!optCita.isPresent()) {
            throw new IllegalArgumentException("La cita que intentas cancelar no existe");
        }

        Citas cita = optCita.get();

        if (!cita.isActive()) {
            throw new IllegalArgumentException("La cita ya esta cancelada");
        }

        if (cita.getHorario().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede cancelar una cita que ya ha pasado");
        }

        cita.setActive(false);
        citasDao.save(cita);
    }

    public List<Citas> buscarCitasFiltradas(Long doctorId, Long consultorioId, LocalDate fecha) {
        Specification<Citas> spec = Specification.where(null);

        if (doctorId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("doctor").get("id"), doctorId));
        }

        if (consultorioId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("consultorio").get("id"), consultorioId));
        }

        if (fecha != null) {
            LocalDateTime inicio = fecha.atStartOfDay();
            LocalDateTime fin = fecha.atTime(23, 59, 59);
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("horario"), inicio, fin));
        }

        return citasDao.findAll(spec);
    }

    // Validaciones
    private void validarCita(Doctor doctor, Consultorio consultorio, LocalDateTime horarioConsulta, String nombrePaciente)
             {
        // Horario futuro
        if (horarioConsulta.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden agendar citas en el pasado");
        }

        // Consultorio disponible
        if (citasDao.existsByConsultorioAndHorario(consultorio, horarioConsulta)) {
            throw new IllegalArgumentException("El consultorio estará ocupado en ese horario");
        }

        // Doctor disponible
        if (citasDao.existsByDoctorAndHorario(doctor, horarioConsulta)) {
            throw new IllegalArgumentException("El doctor ya tiene una cita asignada en ese horario");
        }

        // Cita cercana con dos horas de diferencia
        LocalDateTime dosHorasAntes = horarioConsulta.minusHours(2);
        LocalDateTime dosHorasDespues = horarioConsulta.plusHours(2);
        List<Citas> citasCercanas = citasDao.findByPacienteAndHorarioBetween(
                nombrePaciente, dosHorasAntes, dosHorasDespues);

        if (!citasCercanas.isEmpty()) {
            throw new IllegalArgumentException("El paciente ya tiene una cita con dos horas de diferencia");
        }

        // Menos de 8 horas de citas
        LocalDate fecha = horarioConsulta.toLocalDate();
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(LocalTime.MAX);

        long citasDelDiaDoctor = citasDao.countByDoctorAndFecha(doctor, inicioDia, finDia);
        if (citasDelDiaDoctor >= 8) {
            throw new IllegalArgumentException("El doctor ya llegó a su limite de tiempo");
        }
    }
}