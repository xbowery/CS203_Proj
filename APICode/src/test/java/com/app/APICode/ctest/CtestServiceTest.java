package com.app.APICode.ctest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CtestServiceTest {
    @Mock 
    private CtestRepository ctests;

    @InjectMocks
    private CtestServiceImpl ctestService;

}
