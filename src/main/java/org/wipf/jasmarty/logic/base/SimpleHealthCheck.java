package org.wipf.jasmarty.logic.base;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;
import org.wipf.jasmarty.logic.base.Wipf.httpRequestType;

@Liveness
@ApplicationScoped
public class SimpleHealthCheck implements HealthCheck {

	@Inject
	Wipf wipf;

	@Override
	public HealthCheckResponse call() {
		HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Rest connection health check");
		try {
			String s = wipf.httpRequest(httpRequestType.GET, "http://localhost:8080/wipf/up");
			System.out.println(s);
			responseBuilder.up();
		} catch (Exception e) {
			responseBuilder.down();
		}

		return HealthCheckResponse.up("Simple health check");
	}

}