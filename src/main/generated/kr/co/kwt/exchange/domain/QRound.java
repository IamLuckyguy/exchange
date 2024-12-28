package kr.co.kwt.exchange.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRound is a Querydsl query type for Round
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRound extends EntityPathBase<Round> {

    private static final long serialVersionUID = -302342930L;

    public static final QRound round1 = new QRound("round1");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> fetchedAt = createDateTime("fetchedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> round = createNumber("round", Integer.class);

    public QRound(String variable) {
        super(Round.class, forVariable(variable));
    }

    public QRound(Path<? extends Round> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRound(PathMetadata metadata) {
        super(Round.class, metadata);
    }

}

