package escolaDanca.back.resource;

import escolaDanca.back.bd.entity.UsuarioEntity;
import escolaDanca.back.bd.repository.UsuarioRepository;
import escolaDanca.back.domain.dto.LoginRequestDto;
import escolaDanca.back.domain.dto.LoginResponseDto;
import escolaDanca.back.domain.dto.aluno.ConsultarAlunoResponseDto;
import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import escolaDanca.back.domain.dto.evento.ListarEventosResponseDto;
import escolaDanca.back.domain.dto.usuario.ConsultarUsuarioResponseDto;
import escolaDanca.back.domain.enums.Role;
import escolaDanca.back.exception.BusinessException;
import escolaDanca.back.resource.aluno.service.AlunoService;
import escolaDanca.back.resource.cobranca.service.CobrancaService;
import escolaDanca.back.resource.evento.service.EventoService;
import escolaDanca.back.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AlunoService alunoService;
    private final EventoService eventoService;
    private final CobrancaService cobrancaService;

    private static final String TIPO_TOKEN = "Bearer";

    public LoginResponseDto login(LoginRequestDto request) {

        UsuarioEntity usuario = usuarioRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(usuario);

        if(usuario.getTipoUsuario().equals(Role.ALUNO)) {
            return recuperarInfosAluno(token, usuario);
        } else {
            return recuperarInfosAdmin(token, usuario);
        }
    }

    private LoginResponseDto recuperarInfosAluno(String token, UsuarioEntity usuario) {
        ConsultarUsuarioResponseDto infoUsuario =
                new ConsultarUsuarioResponseDto(
                        usuario.getIdUsuario(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getCpf()
                );

        ConsultarAlunoResponseDto aluno = alunoService.consultarAluno(usuario.getCpf());
        ListarEventosResponseDto infoEventos = eventoService.listarEventos();
        ConsultarCobrancaResponseDto infoCobrancas =
                cobrancaService.consultarCobrancaPeriodoAno(aluno.getId(), LocalDate.now());

        return new LoginResponseDto(
                token,
                TIPO_TOKEN,
                usuario.getTipoUsuario(),
                infoUsuario,
                infoEventos,
                infoCobrancas
        );
    }

    private LoginResponseDto recuperarInfosAdmin(String token, UsuarioEntity usuario) {
        ConsultarUsuarioResponseDto infoUsuario =
                new ConsultarUsuarioResponseDto(
                        usuario.getIdUsuario(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getCpf()
                );
        ListarEventosResponseDto infoEventos = eventoService.listarEventos();

        return new LoginResponseDto(
                token,
                TIPO_TOKEN,
                usuario.getTipoUsuario(),
                infoUsuario,
                infoEventos,
                null
        );
    }

}
