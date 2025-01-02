package kr.co.kwt.exchange.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoundRate is a Querydsl query type for RoundRate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoundRate extends EntityPathBase<RoundRate> {

    private static final long serialVersionUID = 76363502L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoundRate roundRate1 = new QRoundRate("roundRate1");

    public final QExchange exchange;

    public final DateTimePath<java.time.LocalDateTime> fetchedAt = createDateTime("fetchedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath liveStatus = createString("liveStatus");

    public final StringPath marketStatus = createString("marketStatus");

    public final QRound round;

    public final NumberPath<Double> roundRate = createNumber("roundRate", Double.class);

    public final StringPath trend = createString("trend");

    public final NumberPath<Double> trendDiff = createNumber("trendDiff", Double.class);

    public final NumberPath<Double> trendRate = createNumber("trendRate", Double.class);

    public QRoundRate(String variable) {
        this(RoundRate.class, forVariable(variable), INITS);
    }

    public QRoundRate(Path<? extends RoundRate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoundRate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoundRate(PathMetadata metadata, PathInits inits) {
        this(RoundRate.class, metadata, inits);
    }

    public QRoundRate(Class<? extends RoundRate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exchange = inits.isInitialized("exchange") ? new QExchange(forProperty("exchange")) : null;
        this.round = inits.isInitialized("round") ? new QRound(forProperty("round")) : null;
    }

}

