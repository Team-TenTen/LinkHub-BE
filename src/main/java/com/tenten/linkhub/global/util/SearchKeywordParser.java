package com.tenten.linkhub.global.util;

public final class SearchKeywordParser {

    private SearchKeywordParser() { throw new RuntimeException("SearchKeywordParser객체는 생성할 수 없습니다."); }

    public static String parseToTokenAndAppendPlus(String keyword, int tokenSize) {
        StringBuilder tokenizedKeyword = new StringBuilder();
        String[] splitKeyword = keyword.split(" ");

        for (String word: splitKeyword) {
            appendTokensWithPlus(tokenizedKeyword, word, tokenSize - 1);
        }

        return tokenizedKeyword.toString();
    }

    private static void appendTokensWithPlus(StringBuilder tokenizedKeyword, String word, int startIndex) {
        for (int i = startIndex; i < word.length(); i++) {
            String token = word.substring(i - 1, i + 1);
            tokenizedKeyword.append("+");
            tokenizedKeyword.append(token);
            tokenizedKeyword.append(" ");
        }
    }

}
