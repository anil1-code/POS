package com.increff.pos;

import com.increff.pos.spring.AppConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(
        basePackages = {"com.increff.pos"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AppConfig.class)
)
@PropertySources({
        @PropertySource(value = "classpath:./test.properties", ignoreResourceNotFound = true)
})
public class TestConfig {
}
