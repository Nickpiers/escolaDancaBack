package escolaDanca.back.resource.cobranca.service;

import escolaDanca.back.bd.entity.CobrancaEntity;
import escolaDanca.back.bd.repository.CobrancaRepository;
import escolaDanca.back.domain.dto.cobranca.CobrancaDto;
import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import escolaDanca.back.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;

    public ConsultarCobrancaResponseDto consultarUltimaCobranca(Long idUsuario) {
        CobrancaEntity cobranca = cobrancaRepository
                .findTopByMatriculaAlunoUsuarioIdUsuarioOrderByCriadoEmDesc(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma cobrança encontrada para usuário: " + idUsuario));

        ConsultarCobrancaResponseDto response = new ConsultarCobrancaResponseDto();

        List<CobrancaDto> cobrancas = new ArrayList<>();
        cobrancas.add(toResponseDto(cobranca));

        response.setCobrancas(cobrancas);
        response.setNumeroRegistros(1);

        return response;
    }

    public ConsultarCobrancaResponseDto consultarCobrancaPeriodoAno(Long idUsuario, LocalDate dataInicio) {
        LocalDateTime inicio = dataInicio.atStartOfDay();

        List<CobrancaEntity> cobrancasConsultadas = cobrancaRepository.findCobrancasUltimoAnoByUsuario(idUsuario, inicio);

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
