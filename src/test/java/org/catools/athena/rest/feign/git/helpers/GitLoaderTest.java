package org.catools.athena.rest.feign.git.helpers;

import lombok.extern.slf4j.Slf4j;
import org.catools.athena.rest.feign.git.model.RepositoryInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
class GitLoaderTest {

  @Test
  void loadRepository() throws InterruptedException {
    String localPath = "./target/repository/catools" + Instant.now();
    String repoName = "catools";
    String repoUrl = "https://gitlab.com/catools/catools.git";

    Git git = GitCloneClient.clone(localPath, repoName, repoUrl);
    RepositoryInfo repositoryInfo = new RepositoryInfo(git, repoName, repoUrl);
    repositoryInfo.uploadRepository(50, 2, TimeUnit.HOURS, CommitTimeRevFilter.NO_MERGES);
  }
}