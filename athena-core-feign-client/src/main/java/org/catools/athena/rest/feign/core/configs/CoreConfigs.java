package org.catools.athena.rest.feign.core.configs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;

@UtilityClass
public class CoreConfigs {

  @Setter
  @Getter
  private static String athenaHost = ConfigUtils.getString("athena.host", "http://localhost:8080/api");

}
