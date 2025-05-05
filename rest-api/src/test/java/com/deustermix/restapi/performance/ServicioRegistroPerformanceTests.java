package com.deustermix.restapi.performance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.service.ServiceRegistro;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ServicioRegistroPerformanceTests {

    @Autowired
    private ServiceRegistro servicioRegistro; // Ensure this is injected by Spring

    @JUnitPerfTestActiveConfig
    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/registrarPerf-report.html"))
            .build();
       
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(
        executionsPerSec = 100, meanLatency = 10, maxLatency = 1500, minLatency = 10
    )
    public void testRegistroPerformance() {
        Usuario usuario = new Usuario("dniTest", "nombreTest", "apellidoTest", "nombreUsuarioTest", "emailTest", "contrasenaTest");
        servicioRegistro.registrar(usuario); // Ensure servicioRegistro is properly initialized
    }
}
