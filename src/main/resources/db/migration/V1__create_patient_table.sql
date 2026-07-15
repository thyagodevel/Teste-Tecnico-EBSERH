CREATE TABLE pacientes (
                           id UUID PRIMARY KEY,

                           nome VARCHAR(150) NOT NULL,

                           cpf VARCHAR(11) NOT NULL,

                           data_nascimento DATE NOT NULL,

                           tipo_sanguineo VARCHAR(20) NOT NULL,

                           internado BOOLEAN NOT NULL,

                           ambiente_internacao VARCHAR(30),

                           numero_leito INTEGER,

                           criado_em TIMESTAMP NOT NULL,

                           atualizado_em TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX idx_paciente_cpf
    ON pacientes(cpf);