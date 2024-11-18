package com.bgvacc.web.requests.discord;

import java.io.Serializable;
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
public class DiscordEmbedsAuthorRequest implements Serializable {

  private String name;
}