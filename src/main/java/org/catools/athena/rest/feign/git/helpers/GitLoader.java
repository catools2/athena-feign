package org.catools.athena.rest.feign.git.helpers;

import feign.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.git.model.TagDto;
import org.catools.athena.rest.feign.client.FeignClient;
import org.catools.athena.rest.feign.core.clients.UserClient;
import org.catools.athena.rest.feign.git.client.CommitClient;
import org.catools.athena.rest.feign.git.client.CommitMetadataClient;
import org.catools.athena.rest.feign.git.client.RepositoryClient;
import org.catools.athena.rest.feign.git.client.TagClient;
import org.catools.athena.rest.feign.git.model.RepositoryInfo;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@UtilityClass
public class GitLoader {

  public static void persistRepository(RepositoryInfo repositoryInfo) {
    Response response = FeignClient.getClient(RepositoryClient.class).save(repositoryInfo.getRepositoryDto());
    getEntityId(response).ifPresent(s -> repositoryInfo.getRepositoryDto().setId(s));

    UserClient userClient = FeignClient.getClient(UserClient.class);
    repositoryInfo.getUsers().forEach(u -> getEntityId(userClient.save(u)).ifPresent(u::setId));

    TagClient tagClient = FeignClient.getClient(TagClient.class);
    repositoryInfo.getTags().forEach(u -> getEntityId(tagClient.save(u)).ifPresent(u::setId));

    CommitMetadataClient metadataClient = FeignClient.getClient(CommitMetadataClient.class);
    repositoryInfo.getMetadata().forEach(u -> getEntityId(metadataClient.save(u)).ifPresent(u::setId));

    CommitClient commitClient = FeignClient.getClient(CommitClient.class);
    repositoryInfo.getCommits().forEach(c -> {
      c.setMetadata(getMatchedMetadata(repositoryInfo.getMetadata(), c.getMetadata()));
      c.setTags(getMatchedTags(repositoryInfo.getTags(), c.getTags()));
      getEntityId(commitClient.save(c)).ifPresent(c::setId);
    });
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

  @NotNull
  private static Optional<Long> getEntityId(Response response) {
    return response.headers().get("entity_id").stream().findFirst().map(Long::valueOf);
  }
}
