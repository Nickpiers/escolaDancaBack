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

        String cpfMascarado = MascararCpf.mascararCpf(request.cpf());

        AlunoEntity aluno = alunoRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com cpf: " + cpfMascarado));

        if (usuarioRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF usuário já cadastrado: " + cpfMascarado);
        }

        String senhaHash = passwordEncoder.encode(request.senha());

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCpf(request.cpf());
        usuario.setEmail(aluno.getEmail());
        usuario.setSenhaHash(senhaHash);
        usuario.setTipoUsuario(Role.ALUNO);
        usuario.setAtivo(true);

        aluno.setUsuario(usuario);
        usuarioRepository.save(usuario);
    }
}
