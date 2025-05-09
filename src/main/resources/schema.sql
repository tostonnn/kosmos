CREATE TABLE IF NOT EXISTS doctor (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100),
                        ma_name VARCHAR(100),
                        pa_name VARCHAR(100),
                        speciality VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS consultorio (
                             id SERIAL PRIMARY KEY,
                             number INT NOT NULL,
                             floor INT NOT NULL
);

CREATE TABLE IF NOT EXISTS cita (
                      id SERIAL PRIMARY KEY,
                      consultorio_id INT NOT NULL,
                      doctor_id INT NOT NULL,
                      paciente VARCHAR(100) NOT NULL,
                      horario TIMESTAMP NOT NULL,
                      active BOOLEAN NOT NULL,
                      CONSTRAINT fk_consultorio FOREIGN KEY (consultorio_id) REFERENCES consultorio(id),
                      CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);
