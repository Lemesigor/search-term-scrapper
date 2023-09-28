package com.axreng.backend.infrastructure.storage;

import com.axreng.backend.application.domain.SearchTerm;

import java.util.Optional;

public interface SearchTermRepository {

    String save(String url);
    Optional<SearchTerm> findById(String id);

}