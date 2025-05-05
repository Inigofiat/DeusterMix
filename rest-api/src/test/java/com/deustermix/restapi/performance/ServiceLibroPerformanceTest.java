package com.deustermix.restapi.performance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.deustermix.restapi.service.ServiceLibro;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ServiceLibroPerformanceTest {
    
    @Autowired
    private ServiceLibro servicioLibro;

    @JUnitPerfTestActiveConfig
    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/libroPerf-report.html"))
            .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(
        executionsPerSec = 100, meanLatency = 50
    )    
    public void testObtenerTodosLibrosPerformance() {
        servicioLibro.obtenerTodosLosLibros();
    }
}
