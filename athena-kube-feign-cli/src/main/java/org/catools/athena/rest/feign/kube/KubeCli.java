package org.catools.athena.rest.feign.kube;

import com.beust.jcommander.JCommander;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;
import org.catools.athena.rest.feign.kube.configs.KubeConfigs;
import org.catools.athena.rest.feign.kube.utils.KubeLoader;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class KubeCli {

  public static void main(String[] argc) {
    Args args = getArgs(argc);
    args.loadConfig();

    KubeLoader.loadNamespaces(KubeConfigs.getNamespaces(), CoreConfigs.getThreadsCount(), CoreConfigs.getTimeoutInMinutes());

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