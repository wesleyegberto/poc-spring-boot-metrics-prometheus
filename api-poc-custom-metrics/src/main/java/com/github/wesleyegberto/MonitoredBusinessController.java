package com.github.wesleyegberto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("orders")
public class MonitoredBusinessController {
	private final String METRIC_NAME = "orders_placed";
	private final String TAG_NAME = "client_id";

	private final MeterRegistry meterRegistry;

	private final Map<String, Counter> counters = new HashMap<>();

	public MonitoredBusinessController(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	private Counter getClientCounter(String clientId) {
		if (counters.containsKey(clientId)) {
			return counters.get(clientId);
		}
		return Counter.builder(METRIC_NAME)
			.tags(TAG_NAME, clientId)
			.register(meterRegistry);
	}

	@PostMapping
	public ResponseEntity<?> placeOrder(@RequestBody Order order) {
		if (!order.hasClientId()) {
			return ResponseEntity.badRequest()
				.body(Map.of("error", "Client ID is required"));
		}

		var counter = getClientCounter(order.getClientId());
		counter.increment();
		return ResponseEntity.ok(Map.of("orderNumber", (int) (Math.random() * 100000)));
	}
}
