package kr.co.kwt.exchange.utils;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public enum RegexUtils {

    // 문자열에 괄호가 포함되었는지에 대한 정규식
    // 괄호 안에 환율에 대한 단위량이 포함되어 있다.
    UNIT_AMOUNT_REGEX(".*\\(.*\\).*\n"),
    UNIT_AMOUNT_EXTRACT_REGEX("\\\\((\\\\d+)\\\\)");

    RegexUtils(String regex) {
        this.pattern = Pattern.compile(regex);
        this.regex = regex;
    }

    private final String regex;
    private final Pattern pattern;
}
