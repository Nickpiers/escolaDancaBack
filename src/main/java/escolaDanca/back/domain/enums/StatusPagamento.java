package escolaDanca.back.domain.enums;

// STATUS DO BOLETO
public enum StatusPagamento {

    CANCELLED,         // Boletos cancelados
    DRAFT,             // Boletos em rascunho, um estado intermediário entre criação e registro
    LATE,              // Boletos com pagamento em atraso, ou seja, após a data de vencimento
    OPEN,              // Boletos registrados, mas ainda não pagos
    PAID,              // Boletos que foram pagos com sucesso
    RECURRENCE_DRAFT,  // Boletos criados com recorrência que nos quais o usuário não deu andamento à criação da cobrança

}
