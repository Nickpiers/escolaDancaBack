package escolaDanca.back.domain.dto.usuario;

public record ConsultarUsuarioResponseDto(
        Long idUsuario,
        String nome,
        String email,
        String cpf) {
}
