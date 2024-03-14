package org.catools.athena.rest.feign.pipeline.configs;

import lombok.Getter;
import lombok.Setter;
import org.catools.athena.core.model.MetadataDto;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;

import java.util.Set;

public class PipelineConfigs {

  @Setter
  @Getter
  private static boolean createNewPipelineEnabled = ConfigUtils.getBoolean("athena.pipeline.always_create_new_pipeline", false);

  @Setter
  @Getter
  private static String executorName = ConfigUtils.getString("athena.pipeline.executor.name", System.getProperty("user.name"));

  @Setter
  @Getter
  private static String pipelineName = ConfigUtils.getString("athena.pipeline.name", "Local");

  @Setter
  @Getter
  private static String pipelineNumber = ConfigUtils.getString("athena.pipeline.number", "1");

  @Setter
  @Getter
  private static String pipelineDescription = ConfigUtils.getString("athena.pipeline.description", "All local execution");

  @Setter
  @Getter
  private static Set<MetadataDto> pipelineMetadata = ConfigUtils.getMetadataSet("athena.pipeline.metadata");

  @Setter
  @Getter
  private static Set<MetadataDto> pipelineExecutionMetadata = ConfigUtils.getMetadataSet("athena.pipeline.execution.metadata");

}
