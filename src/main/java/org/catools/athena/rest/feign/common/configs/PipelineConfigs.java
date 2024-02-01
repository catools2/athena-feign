package org.catools.athena.rest.feign.common.configs;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.MetadataDto;

import java.util.Set;

@Slf4j
@UtilityClass
public class PipelineConfigs {
  public static String getExecutorName() {
    return ConfigUtils.getString("athena.pipeline.executor.name");
  }

  public static String getPipelineName() {
    return ConfigUtils.getString("athena.pipeline.name");
  }

  public static String getPipelineDescription() {
    return ConfigUtils.getString("athena.pipeline.description");
  }

  public static String getPipelineNumber() {
    return ConfigUtils.getString("athena.pipeline.number");
  }

  public static Set<MetadataDto> getPipelineMetadata() {
    return ConfigUtils.getMetadataList("athena.pipeline.metadata");
  }

  public static Set<MetadataDto> getPipelineExecutionMetadata() {
    return ConfigUtils.getMetadataList("athena.pipeline.execution.metadata");
  }
}
