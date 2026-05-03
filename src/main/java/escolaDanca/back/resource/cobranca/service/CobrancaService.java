package escolaDanca.back.resource.cobranca.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import escolaDanca.back.bd.entity.CobrancaEntity;
import escolaDanca.back.bd.entity.MatriculaEntity;
import escolaDanca.back.bd.repository.AlunoRepository;
import escolaDanca.back.bd.repository.CobrancaRepository;
import escolaDanca.back.bd.repository.MatriculaRepository;
import escolaDanca.back.domain.dto.cobranca.CobrancaDto;
import escolaDanca.back.domain.dto.cobranca.ComprovanteRequestDto;
import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import escolaDanca.back.domain.dto.cobranca.CriarCobrancaRequestDto;
import static escolaDanca.back.domain.enums.StatusPagamento.ABERTO;

import escolaDanca.back.domain.enums.StatusPagamento;
import escolaDanca.back.exception.BusinessException;
import escolaDanca.back.exception.ResourceNotFoundException;
import escolaDanca.back.utils.MascararCpf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
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

    public ConsultarCobrancaResponseDto consultarCobrancaPeriodoAno(Long idUsuario, LocalDate dataReferencia) {
        Long idAluno = alunoRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno nao encontrado")).getIdAluno();

        Optional<CobrancaEntity> proximaCobranca = cobrancaRepository.findTopByMatriculaAlunoIdAlunoAndVencimentoGreaterThanEqualOrderByVencimentoAsc(
                idAluno,
                LocalDate.now()
        );

        LocalDateTime inicio = dataReferencia.minusYears(1).atStartOfDay();

        List<CobrancaEntity> cobrancasConsultadas = cobrancaRepository.findCobrancasUltimoAnoByAluno(idAluno, inicio);

        List<CobrancaDto> listaCobrancas = cobrancasConsultadas.stream()
                                                                .map(this::toResponseDto)
                                                                .toList();

        List<CobrancaDto> cobrancasPagas = listaCobrancas.stream()
                .filter(c -> c.getStatusPagamento() == StatusPagamento.QUITADO)
                .toList();

        List<CobrancaDto> cobrancasEmAberto = listaCobrancas.stream()
                .filter(c -> c.getStatusPagamento() != StatusPagamento.QUITADO)
                .toList();

        ConsultarCobrancaResponseDto response = new ConsultarCobrancaResponseDto();
        response.setNumeroRegistros(listaCobrancas.size());
        response.setCobrancasPagas(cobrancasPagas);
        response.setCobrancasEmAberto(cobrancasEmAberto);

        response.setIdProximaCobranca(proximaCobranca.map(CobrancaEntity::getIdCobranca).orElse(null));

        return response;
    }

    public void pagarCobranca(Long idCobranca) {
        CobrancaEntity cobranca = cobrancaRepository.findById(idCobranca)
                .orElseThrow(() -> new BusinessException("Cobrança com esse id nao encontrada: " + idCobranca));

        cobranca.setStatusPagamento(StatusPagamento.QUITADO);
        cobranca.setValorPago(cobranca.getValorTotal());
        cobranca.setPagoEm(LocalDateTime.now());

        cobrancaRepository.save(cobranca);
    }

    public byte[] gerarPdf(ComprovanteRequestDto request) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Image logo = Image.getInstance("src/main/java/escolaDanca/back/domain/escolaDancaLogo.jpg");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph titulo = new Paragraph("Comprovante de Pagamento", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(2);
            tabela.setWidthPercentage(100);

            tabela.addCell(cellLabel("CPF:"));
            tabela.addCell(cellValor(request.cpf()));

            tabela.addCell(cellLabel("Cliente:"));
            tabela.addCell(cellValor(request.nome()));

            tabela.addCell(cellLabel("Email:"));
            tabela.addCell(cellValor(request.email()));

            tabela.addCell(cellLabel("Valor Total:"));
            tabela.addCell(cellValor(request.valorTotal()));

            tabela.addCell(cellLabel("Valor Pago:"));
            tabela.addCell(cellValor(request.valorPago()));

            tabela.addCell(cellLabel("Data de Pagamento:"));
            tabela.addCell(cellValor(request.dataPagamento()));

            document.add(tabela);

            document.add(new Paragraph(" "));
            Paragraph rodape = new Paragraph("Obrigado por utilizar nossos serviços!",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));
            rodape.setAlignment(Element.ALIGN_CENTER);
            document.add(rodape);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private PdfPCell cellLabel(String texto) {
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
        PdfPCell cell = new PdfPCell(new Phrase(texto, labelFont));
        cell.setBackgroundColor(new BaseColor(230, 230, 250));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell cellValor(String texto) {
        Font valorFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(texto, valorFont));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private CobrancaDto toResponseDto(CobrancaEntity cobranca) {
        CobrancaDto dto = new CobrancaDto();
        dto.setIdCobranca(cobranca.getIdCobranca());
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
