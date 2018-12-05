package localhost.playground.multi.auth

import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

@RestController
class ResourceController {

    @GetMapping("/resourceA/")
    fun resourceA(exchange: ServerWebExchange): Mono<String> {

        return exchange.getPrincipal<UsernamePasswordAuthenticationToken>().map {

            logger.info { "Accessed resource A. Who: ${it.principal}  Authorities: ${it.authorities} Credentials: ${it.credentials}" }

            "Accessed resource A"
        }
    }

    @GetMapping("/resourceB/")
    fun resourceB(exchange: ServerWebExchange): Mono<String> {

        return exchange.getPrincipal<UsernamePasswordAuthenticationToken>().map {

            logger.info { "Accessed resource B. Who: ${it.principal}  Authorities: ${it.authorities} Credentials: ${it.credentials}" }

            "Accessed resource B"
        }


    }
}
