package org.catools.athena.rest.feign.git.helpers;

import lombok.extern.slf4j.Slf4j;
import org.catools.athena.rest.feign.git.model.RepositoryInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
class GitLoaderTest {

  @Test
  void loadRepository() throws GitAPIException, IOException, InterruptedException {
    String localPath = "./target/repository/catools2" + Instant.now();
    String repoName = "catools";
    String repoUrl = "https://gitlab.com/catools/catools2.git";

    Git git = GitCloneClient.clone(localPath, repoName, repoUrl);
    RepositoryInfo repositoryInfo = new RepositoryInfo(git, repoName, repoUrl, true);
    repositoryInfo.readRepository(50, 2, TimeUnit.HOURS, CommitTimeRevFilter.NO_MERGES);
  }
}