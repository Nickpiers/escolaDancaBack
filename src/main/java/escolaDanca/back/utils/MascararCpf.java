package escolaDanca.back.utils;

public class MascararCpf {

    public static String mascararCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos.");
        }

        String ultimosDigitos = cpf.substring(7);
        String cpfMascarado = "xxxxxxx" + ultimosDigitos;

        return cpfMascarado;
    }
}
