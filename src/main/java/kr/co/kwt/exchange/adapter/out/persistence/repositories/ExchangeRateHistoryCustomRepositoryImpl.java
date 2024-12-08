package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExchangeRateHistoryCustomRepositoryImpl implements ExchangeRateHistoryCustomRepository {

    private final DatabaseClient databaseClient;
    
}
