package escolaDanca.back.resource.usuario.service;

import escolaDanca.back.bd.entity.AlunoEntity;
import escolaDanca.back.bd.entity.UsuarioEntity;
import escolaDanca.back.bd.repository.AlunoRepository;
import escolaDanca.back.bd.repository.UsuarioRepository;
import escolaDanca.back.domain.dto.usuario.CriarUsuarioRequestDto;
import escolaDanca.back.domain.enums.Role;
import escolaDanca.back.exception.BusinessException;
import escolaDanca.back.exception.ResourceNotFoundException;
import escolaDanca.back.utils.MascararCpf;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;

    public void criarUsuario(CriarUsuarioRequestDto request) {

        final String cpfMascarado = MascararCpf.mascararCpf(request.cpf());
        final boolean isAluno = request.tipoUsuario().equals(Role.ALUNO);
        AlunoEntity aluno = null;

        if (isAluno) {
            aluno = alunoRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com cpf: " + cpfMascarado));
        }

        if (usuarioRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF usuário já cadastrado: " + cpfMascarado);
        }

        final String senhaHash = passwordEncoder.encode(request.senha());

        final UsuarioEntity usuario = new UsuarioEntity();
        usuario.setEmail(request.email());
        usuario.setSenhaHash(senhaHash);
        usuario.setAtivo(true);
        usuario.setTipoUsuario(request.tipoUsuario());
        usuario.setCpf(request.cpf());

        if (isAluno) {
            aluno.setUsuario(usuario);
        }

        usuarioRepository.save(usuario);
    }
}
