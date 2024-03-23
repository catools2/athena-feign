package org.catools.athena.rest.feign.git;

import com.beust.jcommander.JCommander;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.git.model.GitRepositoryDto;
import org.catools.athena.rest.feign.apispec.configs.GitConfigs;
import org.catools.athena.rest.feign.apispec.entity.RepoInfo;
import org.catools.athena.rest.feign.apispec.entity.RepoInfoSet;
import org.catools.athena.rest.feign.apispec.helpers.AthenaGitApi;
import org.catools.athena.rest.feign.apispec.helpers.GitCloneClient;
import org.catools.athena.rest.feign.apispec.model.RepositoryInfo;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;

@Slf4j
public class GitCli {

  public static void main(String[] argc) {
    Args args = getArgs(argc);
    args.loadConfig();

    if (GitConfigs.getRepoInfoSet() == null || GitConfigs.getRepoInfoSet().isEmpty()) {
      loadRepository(new RepoInfo(GitConfigs.getName(), GitConfigs.getUrl()));
    } else {
      loadRepositories(GitConfigs.getRepoInfoSet());
    }
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

  private static void loadRepositories(RepoInfoSet repoInfoSet) {
    for (RepoInfo repoInfo : repoInfoSet) {
      loadRepository(repoInfo);
    }
  }

  private static void loadRepository(RepoInfo repoInfo) {
    GitRepositoryDto savedRepository = AthenaGitApi.getRepository(repoInfo.getName());
    RevFilter revFilter = savedRepository == null || savedRepository.getLastSync() == null ?
        CommitTimeRevFilter.NO_MERGES :
        CommitTimeRevFilter.after(Date.from(savedRepository.getLastSync()));

    Git git = cloneRepository(repoInfo);

    RepositoryInfo repositoryInfo = new RepositoryInfo(git, repoInfo.getName(), repoInfo.getUrl());
    repositoryInfo.uploadRepository(CoreConfigs.getThreadsCount(), CoreConfigs.getTimeoutInMinutes(), revFilter);
  }

  private static Git cloneRepository(RepoInfo repoInfo) {
    String localPath = Path.of(StringUtils.defaultIfBlank(GitConfigs.getLocalPath(), "./tmp/repository/"), repoInfo.getName() + Instant.now())
        .toFile()
        .getPath();

    if (StringUtils.isNoneBlank(GitConfigs.getUsername())) {
      return GitCloneClient.clone(localPath, repoInfo.getName(), repoInfo.getUrl(), GitConfigs.getUsername(), GitConfigs.getPassword());
    }

    return GitCloneClient.clone(localPath, repoInfo.getName(), repoInfo.getUrl());
  }

}