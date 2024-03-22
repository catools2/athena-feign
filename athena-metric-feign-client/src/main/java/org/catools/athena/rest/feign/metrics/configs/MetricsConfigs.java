package org.catools.athena.rest.feign.metrics.configs;

import lombok.Getter;
import lombok.Setter;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;

public class MetricsConfigs {

  @Setter
  @Getter
  private static boolean enable = ConfigUtils.getBoolean("athena.metrics.enable", true);

}
