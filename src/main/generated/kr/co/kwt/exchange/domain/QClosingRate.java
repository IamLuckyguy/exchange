package kr.co.kwt.exchange.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClosingRate is a Querydsl query type for ClosingRate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClosingRate extends EntityPathBase<ClosingRate> {

    private static final long serialVersionUID = 1177525909L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClosingRate closingRate1 = new QClosingRate("closingRate1");

    public final NumberPath<Double> closingRate = createNumber("closingRate", Double.class);

    public final QExchange exchange;

    public final DateTimePath<java.time.LocalDateTime> fetchedAt = createDateTime("fetchedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QClosingRate(String variable) {
        this(ClosingRate.class, forVariable(variable), INITS);
    }

    public QClosingRate(Path<? extends ClosingRate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClosingRate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClosingRate(PathMetadata metadata, PathInits inits) {
        this(ClosingRate.class, metadata, inits);
    }

    public QClosingRate(Class<? extends ClosingRate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exchange = inits.isInitialized("exchange") ? new QExchange(forProperty("exchange")) : null;
    }

}

