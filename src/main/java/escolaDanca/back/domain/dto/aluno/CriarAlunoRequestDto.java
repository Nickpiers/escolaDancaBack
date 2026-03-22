package escolaDanca.back.domain.dto.aluno;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CriarAlunoRequestDto(

        @NotBlank
        String cpf,

        @NotBlank
        String nome,

        @NotBlank
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        String plano,

        @NotBlank
        LocalDate dataInicio,

        LocalDate dataTermino,

        String responsavelNome,
        String responsavelCpf,
        String responsavelTelefone
) {

    public boolean temResponsavel() {
        return responsavelCpf != null && !responsavelCpf.isBlank()
                && responsavelNome != null && !responsavelNome.isBlank()
                && responsavelTelefone != null && !responsavelTelefone.isBlank();
    }

}
