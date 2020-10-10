package br.com.taian.bank.api.validation

import java.util.*

/**
 * Código para validar CPF em java:
 * https://www.devmedia.com.br/validando-o-cpf-em-uma-aplicacao-java/22097
 */


object ValidaCPF {
    fun isCPF(rawCPF: String): Boolean {
        val cpfPattern = "^\\d{3}.\\d{3}.\\d{3}[-]\\d{2}$".toRegex()
        if (!rawCPF.matches(cpfPattern)) return false
        val cpf = rawCPF.replace(".", "").replace("-", "")
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (cpf == "00000000000" || cpf == "11111111111" || cpf == "22222222222" || cpf == "33333333333" || cpf == "44444444444" || cpf == "55555555555" || cpf == "66666666666" || cpf == "77777777777" || cpf == "88888888888" || cpf == "99999999999" ||
            cpf.length != 11
        ) return false
        val dig10: Char
        val dig11: Char
        var sm: Int
        var i: Int
        var r: Int
        var num: Int
        var peso: Int

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        return try {
            // Calculo do 1o. Digito Verificador
            sm = 0
            peso = 10
            i = 0
            while (i < 9) {

                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (cpf[i].toInt() - 48)
                sm = sm + num * peso
                peso = peso - 1
                i++
            }
            r = 11 - sm % 11
            dig10 = if (r == 10 || r == 11) '0' else (r + 48).toChar() // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0
            peso = 11
            i = 0
            while (i < 10) {
                num = (cpf[i].toInt() - 48)
                sm = sm + num * peso
                peso = peso - 1
                i++
            }
            r = 11 - sm % 11
            dig11 = if (r == 10 || r == 11) '0' else (r + 48).toChar()

            // Verifica se os digitos calculados conferem com os digitos informados.
            dig10 == cpf[9] && dig11 == cpf[10]
        } catch (erro: InputMismatchException) {
            false
        }
    }

    fun imprimeCPF(cpf: String): String {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" + cpf.substring(9, 11)
    }
}
