package localhost.playground.multi.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ResourceController {

    @GetMapping("/resourceA/")
    fun resourceA(): Mono<String> {
        return Mono.just("Accessed resource A")
    }

    @GetMapping("/resourceB/")
    fun resourceB(): Mono<String> {
        return Mono.just("Accessed resource B")
    }
}
