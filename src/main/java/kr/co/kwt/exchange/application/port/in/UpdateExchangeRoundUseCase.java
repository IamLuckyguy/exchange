package kr.co.kwt.exchange.application.port.in;

import reactor.core.publisher.Mono;

public interface UpdateExchangeRoundUseCase {

    Mono<Integer> updateDegreeCount(int current);
}
