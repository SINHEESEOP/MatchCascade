package com.matchcascade;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class ObservabilityConfigurationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Test
    void hikariCP_커넥션_메트릭이_노출된다() throws Exception {
        mockMvc.perform(get("/actuator/metrics/hikaricp.connections.active"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("hikaricp.connections.active")));
    }

    @Test
    void hibernate_통계_수집이_켜져있다() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        assertThat(sessionFactory.getStatistics().isStatisticsEnabled()).isTrue();
    }

    @Test
    void innodb_lock_wait_timeout이_5초로_설정된다() {
        String value = jdbcTemplate.queryForObject(
                "SHOW VARIABLES LIKE 'innodb_lock_wait_timeout'", (rs, rowNum) -> rs.getString("Value"));

        assertThat(value).isEqualTo("5");
    }

}
