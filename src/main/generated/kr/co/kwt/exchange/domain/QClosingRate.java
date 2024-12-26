package kr.co.kwt.exchange.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QClosingRate is a Querydsl query type for ClosingRate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClosingRate extends EntityPathBase<ClosingRate> {

    private static final long serialVersionUID = 1177525909L;

    public static final QClosingRate closingRate1 = new QClosingRate("closingRate1");

    public final NumberPath<Double> closingRate = createNumber("closingRate", Double.class);

    public final StringPath currencyCode = createString("currencyCode");

    public final DateTimePath<java.time.LocalDateTime> fetchedAt = createDateTime("fetchedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QClosingRate(String variable) {
        super(ClosingRate.class, forVariable(variable));
    }

    public QClosingRate(Path<? extends ClosingRate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClosingRate(PathMetadata metadata) {
        super(ClosingRate.class, metadata);
    }

}

