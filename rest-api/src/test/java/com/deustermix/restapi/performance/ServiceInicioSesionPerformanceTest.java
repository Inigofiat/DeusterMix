package com.deustermix.restapi.performance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.deustermix.restapi.service.ServiceInicioSesion;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ServiceInicioSesionPerformanceTest {
    
    @Autowired
    private ServiceInicioSesion servicioInicioSesion;
    private static String tokenTest;

    @JUnitPerfTestActiveConfig
    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/IniSesPerf-report.html"))
            .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(
        executionsPerSec = 100, meanLatency = 10, maxLatency = 1500, minLatency = 10
    )    
    public void testLoginPerformance() {
        tokenTest = servicioInicioSesion.login("emailTest", "contrasenaTest");
    }


    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(
        executionsPerSec = 100, meanLatency = 10, maxLatency = 1500, minLatency = 10
    )
    public void testLogoutPerformance() {
        servicioInicioSesion.logout(tokenTest);
    }
}