package org.catools.athena.rest.feign.git.helpers;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.core.model.UserDto;
import org.catools.athena.git.model.CommitDto;
import org.catools.athena.git.model.GitRepositoryDto;
import org.catools.athena.git.model.TagDto;
import org.catools.athena.rest.feign.common.utils.FeignUtils;
import org.catools.athena.rest.feign.core.client.UserClient;
import org.catools.athena.rest.feign.git.client.CommitClient;
import org.catools.athena.rest.feign.git.client.CommitMetadataClient;
import org.catools.athena.rest.feign.git.client.RepositoryClient;
import org.catools.athena.rest.feign.git.client.TagClient;
import org.catools.athena.rest.feign.git.model.RepositoryInfo;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static org.catools.athena.rest.feign.common.utils.FeignUtils.getEntityId;

@Slf4j
@UtilityClass
public class AthenaGitApi {
  private static final RepositoryClient repositoryClient = FeignUtils.getClient(RepositoryClient.class);
  private static final UserClient userClient = FeignUtils.getClient(UserClient.class);
  private static final TagClient tagClient = FeignUtils.getClient(TagClient.class);
  private static final CommitMetadataClient metadataClient = FeignUtils.getClient(CommitMetadataClient.class);
  private static final CommitClient commitClient = FeignUtils.getClient(CommitClient.class);

  public static void persistRepositoryInfo(RepositoryInfo repositoryInfo) {
    persistRepository(repositoryInfo.getRepositoryDto());

    repositoryInfo.getUsers().forEach(AthenaGitApi::persistUser);

    repositoryInfo.getTags().forEach(AthenaGitApi::persistTag);

    repositoryInfo.getMetadata().forEach(AthenaGitApi::persistMetadata);

    repositoryInfo.getCommits().forEach(c -> persistCommit(repositoryInfo, c));
  }

  public static void persistRepository(GitRepositoryDto repository) {
    getEntityId(repositoryClient.save(repository)).ifPresent(repository::setId);
  }

  public static void persistCommit(RepositoryInfo repositoryInfo, CommitDto c) {
    c.setMetadata(getMatchedMetadata(repositoryInfo.getMetadata(), c.getMetadata()));
    c.setTags(getMatchedTags(repositoryInfo.getTags(), c.getTags()));
    getEntityId(commitClient.save(c)).ifPresent(c::setId);
  }

  public static void persistMetadata(MetadataDto u) {
    getEntityId(metadataClient.save(u)).ifPresent(u::setId);
  }

  public static void persistTag(TagDto u) {
    getEntityId(tagClient.save(u)).ifPresent(u::setId);
  }

  public static void persistUser(UserDto u) {
    if (userClient.search(u.getUsername()) == null) {
      getEntityId(userClient.save(u)).ifPresent(u::setId);
    }
  }

  @NotNull
  private static Set<TagDto> getMatchedTags(Set<TagDto> tags1, Set<TagDto> tags2) {
    Set<TagDto> ouptut = new HashSet<>();
    for (TagDto m1 : tags1) {
      if (tags2.stream().anyMatch(md -> md.getName().equals(m1.getName()) && md.getHash().equals(m1.getHash())))
        ouptut.add(m1);
    }
    return ouptut;
  }

  @NotNull
  private static Set<MetadataDto> getMatchedMetadata(Set<MetadataDto> metadata1, Set<MetadataDto> metadata2) {
    Set<MetadataDto> ouptut = new HashSet<>();
    for (MetadataDto m1 : metadata1) {
      if (metadata2.stream().anyMatch(md -> md.getName().equals(m1.getName()) && md.getValue().equals(m1.getName())))
        ouptut.add(m1);
    }
    return ouptut;
  }
}
