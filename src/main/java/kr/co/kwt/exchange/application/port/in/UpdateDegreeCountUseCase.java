package kr.co.kwt.exchange.application.port.in;

import reactor.core.publisher.Mono;

public interface UpdateDegreeCountUseCase {

    Mono<Integer> updateDegreeCount(int current);
}
