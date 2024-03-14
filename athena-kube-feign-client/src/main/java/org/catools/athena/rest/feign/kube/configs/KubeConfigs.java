package org.catools.athena.rest.feign.kube.configs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;
import org.catools.athena.rest.feign.kube.enums.KubeConnectionType;

import java.util.List;

/**
 * Kubernetes' configuration including connection parameters.
 */
@UtilityClass
public class KubeConfigs {

  @Setter
  @Getter
  private static List<String> namespaces = ConfigUtils.getStrings("athena.kube.namespaces");

  @Setter
  @Getter
  private static String connectionType = ConfigUtils.getString("athena.kube.connection.type", KubeConnectionType.DEFAULT.name());

  @Setter
  @Getter
  private static Boolean shouldValidateSSL = ConfigUtils.getBoolean("athena.kube.connection.validate_ssl", false);

  @Setter
  @Getter
  private static String connectionUrl = ConfigUtils.getString("athena.kube.connection.url", KubeConnectionType.DEFAULT.name());

  @Setter
  @Getter
  private static String connectionUsername = ConfigUtils.getString("athena.kube.connection.username");

  @Setter
  @Getter
  private static String connectionPassword = ConfigUtils.getString("athena.kube.connection.password");

  @Setter
  @Getter
  private static String connectionToken = ConfigUtils.getString("athena.kube.connection.token");

  @Setter
  @Getter
  private static String kubeConfigPath = ConfigUtils.getString("athena.kube.connection.kube_config_path");

}
