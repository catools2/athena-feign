package org.catools.athena.rest.feign.jira;

import com.beust.jcommander.JCommander;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.atlassian.etl.jira.JiraSyncClient;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.rest.feign.core.cache.CoreCache;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class JiraCli {

  public static void main(String[] args) {
    getArgs(args).loadConfig();
    JiraSyncClient.syncJira();
  }

  @NotNull
  private static Args getArgs(String[] argc) {
    Args cmd = new Args();

    JCommander build = JCommander.newBuilder().programName("Athena Jira Data Loader").acceptUnknownOptions(false).addObject(cmd).build();

    build.setExpandAtSign(false);
    build.parse(argc);

    if (cmd.isHelp()) {
      build.usage();
    }

    return cmd;
  }
}