package com.bgvacc.web.requests.discord;

import java.io.Serializable;
import java.util.List;
import lombok.*;

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
public class DiscordControllerSessionRequest implements Serializable {
  
  private List<DiscordEmbedsRequest> embeds;

}
