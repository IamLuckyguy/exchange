package kr.co.kwt.exchange.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExchange is a Querydsl query type for Exchange
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExchange extends EntityPathBase<Exchange> {

    private static final long serialVersionUID = 1438213827L;

    public static final QExchange exchange = new QExchange("exchange");

    public final EnumPath<Country> country = createEnum("country", Country.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath currencyCode = createString("currencyCode");

    public final ListPath<RoundRate, QRoundRate> dailyRoundRates = this.<RoundRate, QRoundRate>createList("dailyRoundRates", RoundRate.class, QRoundRate.class, PathInits.DIRECT2);

    public final NumberPath<Integer> decimals = createNumber("decimals", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> unit = createNumber("unit", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final ListPath<ClosingRate, QClosingRate> yearlyClosingRates = this.<ClosingRate, QClosingRate>createList("yearlyClosingRates", ClosingRate.class, QClosingRate.class, PathInits.DIRECT2);

    public QExchange(String variable) {
        super(Exchange.class, forVariable(variable));
    }

    public QExchange(Path<? extends Exchange> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExchange(PathMetadata metadata) {
        super(Exchange.class, metadata);
    }

}

