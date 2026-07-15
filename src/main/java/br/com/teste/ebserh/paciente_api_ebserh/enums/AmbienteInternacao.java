package br.com.teste.ebserh.paciente_api_ebserh.enums;

public enum AmbienteInternacao {
    UTI("UTI"),
    ENFERMARIA("Enfermaria"),
    ISOLAMENTO("Isolamento"),
    OBSERVACAO("Observação");

    private final String descricao;

    AmbienteInternacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
