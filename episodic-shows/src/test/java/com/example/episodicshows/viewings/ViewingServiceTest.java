package com.example.episodicshows.viewings;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ViewingServiceTest {
    @Mock
    ViewingRepository viewingRepository;

    @InjectMocks
    private ViewingService viewingService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // TODO yo yo yo
}