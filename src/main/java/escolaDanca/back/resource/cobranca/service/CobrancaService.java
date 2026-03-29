package escolaDanca.back.resource.cobranca.service;

import escolaDanca.back.bd.entity.AlunoEntity;
import escolaDanca.back.bd.entity.CobrancaEntity;
import escolaDanca.back.bd.entity.MatriculaEntity;
import escolaDanca.back.bd.repository.CobrancaRepository;
import escolaDanca.back.bd.repository.MatriculaRepository;
import escolaDanca.back.domain.dto.cobranca.CobrancaDto;
import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import escolaDanca.back.domain.dto.cobranca.CriarCobrancaRequestDto;
import static escolaDanca.back.domain.enums.StatusPagamento.ABERTO;

import escolaDanca.back.exception.BusinessException;
import escolaDanca.back.exception.ResourceNotFoundException;
import escolaDanca.back.utils.MascararCpf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final MatriculaRepository matriculaRepository;
    private static final String URL_LINK_BOLETO = "http://escoladanca/pagamentos/boleto/";
    private static final String URL_LINK_PIX = "http://escoladanca/pagamentos/pix/";

    public void criarCobranca(CriarCobrancaRequestDto requestDto) {

        final String cpfMascarado = MascararCpf.mascararCpf(requestDto.cpf());

        MatriculaEntity matricula = matriculaRepository.findByAlunoCpf(requestDto.cpf())
                .orElseThrow(() -> new ResourceNotFoundException("Matricula nao encontrada para cpf: " + cpfMascarado));

        if (cobrancaRepository.existsByCodigoInterno(requestDto.codigoInterno())) {
            throw new BusinessException("Cobrança ja existe!");
        }

        CobrancaEntity cobranca = new CobrancaEntity();

        cobranca.setMatricula(matricula);
        cobranca.setStatusPagamento(ABERTO);
        cobranca.setValorTotal(requestDto.valorTotal());
        cobranca.setCriadoEm(LocalDateTime.now());
        cobranca.setVencimento(LocalDate.now().plusMonths(1));

        String boletoUri = UUID.randomUUID().toString();
        String pixUri = UUID.randomUUID().toString();
        cobranca.setLinkBoleto(URL_LINK_BOLETO + boletoUri);
        cobranca.setLinkPix(URL_LINK_PIX + pixUri);

        cobranca.setCodigoInterno(requestDto.codigoInterno());

        cobrancaRepository.save(cobranca);
    }


    public ConsultarCobrancaResponseDto consultarUltimaCobranca(Long idAluno) {
        Optional<CobrancaEntity> cobranca = cobrancaRepository.findTopByMatriculaAlunoIdAlunoOrderByCriadoEmDesc(idAluno);

        ConsultarCobrancaResponseDto response = new ConsultarCobrancaResponseDto();

        if(cobranca.isEmpty()) {
            response.setCobrancas(null);
            response.setNumeroRegistros(0);
        } else {
            List<CobrancaDto> cobrancas = new ArrayList<>();
            cobrancas.add(toResponseDto(cobranca.get()));

            response.setCobrancas(cobrancas);
            response.setNumeroRegistros(1);
        }

        return response;
    }

    public ConsultarCobrancaResponseDto consultarCobrancaPeriodoAno(Long idAluno, LocalDate dataInicio) {
        LocalDateTime inicio = dataInicio.atStartOfDay();

        List<CobrancaEntity> cobrancasConsultadas = cobrancaRepository.findCobrancasUltimoAnoByAluno(idAluno, inicio);

        List<CobrancaDto> listaCobrancas = cobrancasConsultadas.stream()
                                                                .map(this::toResponseDto)
                                                                .toList();

        ConsultarCobrancaResponseDto response = new ConsultarCobrancaResponseDto();
        response.setCobrancas(listaCobrancas);
        response.setNumeroRegistros(listaCobrancas.size());

        return response;
    }

    private CobrancaDto toResponseDto(CobrancaEntity cobranca) {
        CobrancaDto dto = new CobrancaDto();
        dto.setStatusPagamento(cobranca.getStatusPagamento());
        dto.setTotalCobranca(cobranca.getValorTotal());
        dto.setTotalPago(cobranca.getValorPago());

        if (cobranca.getMatricula() != null && cobranca.getMatricula().getAluno() != null) {
            dto.setNomePagador(cobranca.getMatricula().getAluno().getNome());
            dto.setEmailPagador(cobranca.getMatricula().getAluno().getEmail());
            dto.setCpfPagador(cobranca.getMatricula().getAluno().getCpf());
        }

        dto.setVencimento(cobranca.getVencimento());
        dto.setLinkBoleto(cobranca.getLinkBoleto());
        dto.setPagoEm(cobranca.getPagoEm());
        dto.setCodigoPix(cobranca.getLinkPix());

        return dto;
    }
}
