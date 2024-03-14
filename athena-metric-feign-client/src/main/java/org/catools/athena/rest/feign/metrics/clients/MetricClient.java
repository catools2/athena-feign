package org.catools.athena.rest.feign.metrics.clients;

import feign.*;
import org.catools.athena.metrics.model.MetricDto;

public interface MetricClient {

  @RequestLine("POST /metric")
  @Headers("Content-Type: application/json")
  Response saveMetric(MetricDto metric);

}
