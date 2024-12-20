package com.bgvacc.web.vatsim.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VatsimMemberSoloValidations implements Serializable {
  
  @JsonProperty("success")
  private boolean success;
  
  @JsonProperty("data")
  private List<VatsimMemberSoloValidation> data;
}
