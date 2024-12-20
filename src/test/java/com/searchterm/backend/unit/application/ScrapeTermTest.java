package com.searchterm.backend.unit.application;

import com.searchterm.backend.application.domain.SearchTerm;
import com.searchterm.backend.application.usecases.ScrapeTermUseCase;
import com.searchterm.backend.infrastructure.cache.SiteMapCache;
import com.searchterm.backend.infrastructure.parser.HtmlParser;
import com.searchterm.backend.infrastructure.storage.SearchTermRepository;
import com.searchterm.backend.infrastructure.threads.TaskQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.*;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

public class ScrapeTermTest {

    @Mock
    private SearchTermRepository repository;

    @Mock
    private TaskQueue poolService;

    @Mock
    private HtmlParser htmlParser;

    @Mock
    private SiteMapCache siteMapCache;


    private ScrapeTermUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(poolService.getExecutor()).thenReturn(command -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(command);
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        useCase = new ScrapeTermUseCase(repository, poolService, htmlParser, siteMapCache);
    }

     @Test
    void executeShouldUseThreadService() {
        String termId = "TestId";
        String baseUrl = "https://someurl.com";
        SearchTerm searchTerm = new SearchTerm("TestWord");

        when(repository.findById(termId)).thenReturn(Optional.of(searchTerm));

        useCase.execute(termId, baseUrl);

        verify(poolService).addTask(any());
    }
}
