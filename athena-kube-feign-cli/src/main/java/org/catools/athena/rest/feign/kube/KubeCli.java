package org.catools.athena.rest.feign.kube;

import com.beust.jcommander.JCommander;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.kube.model.PodDto;
import org.catools.athena.rest.feign.core.client.CoreClient;
import org.catools.athena.rest.feign.kube.configs.KubeConfigBuilder;
import org.catools.athena.rest.feign.kube.configs.KubeConfigs;
import org.catools.athena.rest.feign.kube.utils.KubeLoader;
import org.catools.athena.rest.feign.kube.utils.KubeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Slf4j
public class KubeCli {

  public static void main(String[] argc) {
    Args args = getArgs(argc);
    args.loadConfig();

    CoreClient.getProject(new ProjectDto(KubeConfigs.getProjectCode(), KubeConfigs.getProjectName()));
    CoreV1Api kubeApiClient = KubeConfigBuilder.getKubeApiClient();
    Set<PodDto> pods = KubeUtil.getNamespacePods(kubeApiClient, KubeConfigs.getNamespace());
    KubeLoader.loadPods(pods, KubeConfigs.getThreadsCount(), KubeConfigs.getTimeoutInMinutes());

  }

  @NotNull
  private static Args getArgs(String[] argc) {
    Args cmd = new Args();

    JCommander build = JCommander.newBuilder().programName("Athena Kube Data Loader").addObject(cmd).build();

    build.setExpandAtSign(false);
    build.parse(argc);

    if (cmd.isHelp()) {
      build.usage();
    }

    return cmd;
  }
}