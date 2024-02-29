package org.catools.atlassian.etl.scale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.catools.atlassian.etl.scale.rest.cycle.ScaleExecutionStatus;
import org.catools.atlassian.etl.scale.utils.CustomDateDeserializer;
import org.catools.atlassian.etl.scale.utils.CustomDateSerializer;

import java.util.*;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScalePlanExecution {
  private String testCaseKey;
  private ScaleExecutionStatus status;
  private String environment;
  private String comment;
  private String userKey;
  private Long executionTime;

  @JsonDeserialize(using = CustomDateDeserializer.class)
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date executionDate;
  private HashMap<String, String> customFields;
  private HashSet<ScaleScriptResult> scriptResults;
}