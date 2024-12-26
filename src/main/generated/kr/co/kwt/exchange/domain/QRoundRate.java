package kr.co.kwt.exchange.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoundRate is a Querydsl query type for RoundRate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoundRate extends EntityPathBase<RoundRate> {

    private static final long serialVersionUID = 76363502L;

    public static final QRoundRate roundRate1 = new QRoundRate("roundRate1");

    public final StringPath currencyCode = createString("currencyCode");

    public final DateTimePath<java.time.LocalDateTime> fetchedAt = createDateTime("fetchedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath liveStatus = createString("liveStatus");

    public final StringPath marketStatus = createString("marketStatus");

    public final NumberPath<Integer> round = createNumber("round", Integer.class);

    public final NumberPath<Double> roundRate = createNumber("roundRate", Double.class);

    public final StringPath trend = createString("trend");

    public final NumberPath<Double> trendRate = createNumber("trendRate", Double.class);

    public QRoundRate(String variable) {
        super(RoundRate.class, forVariable(variable));
    }

    public QRoundRate(Path<? extends RoundRate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoundRate(PathMetadata metadata) {
        super(RoundRate.class, metadata);
    }

}

