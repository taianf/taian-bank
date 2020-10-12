package br.com.taian.bank.api.config

import org.springframework.stereotype.*
import org.springframework.web.servlet.*
import javax.servlet.http.*

@Component
class EmailInterceptor : HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        dataObject: Any,
        e: Exception?
    ) {
        val proposalAcceptation = response.getHeader("Proposal-Acceptation")
        when {
            proposalAcceptation == null -> {
                return
            }
            proposalAcceptation.toBoolean() -> {
                println("Sending confirmation email")
                // chamar lógica de validar a proposta sem travar o retorno
            }
            else -> {
                println("Sending begging email")
                // chamar lógica de enviar o email implorando sem travar o retorno
            }
        }
    }

}
