package com.axreng.backend.application.domain;

import com.axreng.backend.shared.Errors;
import com.axreng.backend.shared.RandomIdGenerator;

import java.util.HashSet;

public class SearchTerm {
    private final String word;
    private final String id;

    private HashSet<String> urls;

    private SearchStatus status;

    private static final int MAXIMUM_WORD_LENGTH = 32;
    private static final int MINIMUM_WORD_LENGTH = 4;

    public SearchTerm(String word) {
        if (isValidWord(word)) {
            this.word = word;
            this.id = RandomIdGenerator.generateRandomId(8);
            this.urls = new HashSet<>();
            this.status = SearchStatus.active;
        } else {
            throw new IllegalArgumentException(Errors.INVALID_WORD_ERROR);
        }
    }

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    private boolean isValidWord(String word) {
        return word.length() <= MAXIMUM_WORD_LENGTH && word.length() >= MINIMUM_WORD_LENGTH;
    }

    public synchronized void addUrl(String url) {
        urls.add(url);
    }

    public synchronized void setStatus(SearchStatus status) {
        this.status = status;
    }

    public synchronized SearchStatus getStatus() {
        return status;
    }

    public synchronized HashSet<String> getUrls() {
        return urls;
    }

}
