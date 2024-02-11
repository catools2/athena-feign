package org.catools.athena.rest.feign.git.utils;

import lombok.experimental.UtilityClass;
import org.catools.athena.rest.feign.git.model.RepositoryInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.util.concurrent.TimeUnit;

@UtilityClass
@SuppressWarnings("unused")
public class GitUtils {
  private static final String TAG_METADATA_NAME = "TAG";

  public static RepositoryInfo uploadRepository(Git git, String repoName, String repoUrl, int totalThreads, long timeout, TimeUnit unit, RevFilter filter) throws InterruptedException {
    RepositoryInfo repositoryInfo = new RepositoryInfo(git, repoName, repoUrl);
    repositoryInfo.uploadRepository(totalThreads, timeout, unit, filter);
    return repositoryInfo;
  }
}
