package trade;

import apistream.PriceStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceStreamConfig {

    @Bean
    public PriceStream priceStream() {
        PriceStream stream = new PriceStream();
        stream.connect();
        return stream;
    }
}
