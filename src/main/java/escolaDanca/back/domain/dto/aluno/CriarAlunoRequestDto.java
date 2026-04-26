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
        LocalDate dataInicio,

        LocalDate dataTermino) {
}
