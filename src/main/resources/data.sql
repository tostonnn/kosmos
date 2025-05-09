DELETE FROM doctor;
DELETE FROM consultorio;
DELETE FROM citas;

INSERT INTO doctor (name, pa_name, ma_name, speciality) VALUES
                                                                                  ('Carlos', 'Pérez', 'González', 'Cardiología'),
                                                                                  ('Ana', 'Ramírez', 'López', 'Pediatría'),
                                                                                  ('Luis', 'Martínez', 'Sánchez', 'Dermatología'),
                                                                                  ('María', 'Gómez', 'Torres', 'Oftalmología'),
                                                                                  ('Juan', 'Rodríguez', 'Méndez', 'Neurología');

INSERT INTO consultorio (number, floor) VALUES
                                           (101, 1),
                                           (102, 1),
                                           (201, 2),
                                           (202, 2),
                                           (301, 3);
