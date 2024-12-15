package kr.co.kwt.exchange.config.kms;

import lombok.Getter;

import java.util.List;

@Getter
public class GetKmsResponse<T> {

    private String serviceId;
    private String environment;
    private List<KmsSecret<T>> secrets;
}
