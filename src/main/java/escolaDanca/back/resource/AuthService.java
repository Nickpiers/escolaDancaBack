package escolaDanca.back.resource;

import escolaDanca.back.bd.entity.UsuarioEntity;
import escolaDanca.back.bd.repository.UsuarioRepository;
import escolaDanca.back.domain.dto.LoginRequestDto;
import escolaDanca.back.domain.dto.LoginResponseDto;
import escolaDanca.back.exception.BusinessException;
import escolaDanca.back.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDto login(LoginRequestDto request) {

        UsuarioEntity usuario = usuarioRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDto(token, "Bearer", usuario.getTipoUsuario());
    }

}
