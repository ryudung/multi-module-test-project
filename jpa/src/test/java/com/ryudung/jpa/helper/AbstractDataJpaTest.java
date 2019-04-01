package com.ryudung.jpa.helper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(basePackages = "com.ryudung.domain")
@DataJpaTest
public abstract class AbstractDataJpaTest {
}
