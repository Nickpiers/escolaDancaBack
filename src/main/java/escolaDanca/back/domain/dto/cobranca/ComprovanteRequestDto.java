package escolaDanca.back.domain.dto.cobranca;

public record ComprovanteRequestDto(
        String cpf,
        String nome,
        String email,
        String valorTotal,
        String valorPago,
        String dataPagamento) {
}
