package dev.silenzzz.tsb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Telegram bot configuratuion.
 */
@ConfigurationProperties(prefix = "bot")
@Component
@Data
public class BotConfigurationProperties {

    private String apiToken;

}
