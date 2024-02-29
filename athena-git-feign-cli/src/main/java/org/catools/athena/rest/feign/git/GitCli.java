package org.catools.athena.rest.feign.git;

import com.beust.jcommander.JCommander;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.git.model.GitRepositoryDto;
import org.catools.athena.rest.feign.apispec.helpers.AthenaGitApi;
import org.catools.athena.rest.feign.apispec.helpers.GitCloneClient;
import org.catools.athena.rest.feign.apispec.model.RepositoryInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GitCli {

  public static void main(String[] argc) {
    Args args = getArgs(argc);
    args.loadConfig();

    Path localPath = Path.of(StringUtils.defaultIfBlank(args.getLocalPath(), "./tmp/repository/"), args.getName() + Instant.now());

    GitRepositoryDto savedRepository = AthenaGitApi.getRepository(args.getName());
    RevFilter revFilter = savedRepository == null || savedRepository.getLastSync() == null ?
                          CommitTimeRevFilter.NO_MERGES :
                          CommitTimeRevFilter.after(Date.from(savedRepository.getLastSync()));

    Git git = cloneRepository(args, localPath.toFile().getPath());

    RepositoryInfo repositoryInfo = new RepositoryInfo(git, args.getName(), args.getUrl());
    repositoryInfo.uploadRepository(args.getThreadsCount(), args.getTimeoutInMinutes(), revFilter);

  }

  @NotNull
  private static Args getArgs(String[] argc) {
    Args cmd = new Args();

    JCommander build = JCommander.newBuilder().programName("Athena Git Data Loader").addObject(cmd).build();

    build.setExpandAtSign(false);
    build.parse(argc);

    if (cmd.isHelp()) {
      build.usage();
    }

    return cmd;
  }

  private static Git cloneRepository(Args args, String localPath) {
    if (StringUtils.isNoneBlank(args.getUsername())) {
      return GitCloneClient.clone(localPath, args.getName(), args.getUrl(), args.getUsername(), args.getPassword());
    }

    return GitCloneClient.clone(localPath, args.getName(), args.getUrl());
  }

}