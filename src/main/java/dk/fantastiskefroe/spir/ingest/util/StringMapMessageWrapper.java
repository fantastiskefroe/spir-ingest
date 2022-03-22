package dk.fantastiskefroe.spir.ingest.util;

import org.apache.logging.log4j.message.StringMapMessage;

public class StringMapMessageWrapper extends StringMapMessage {

    public StringMapMessageWrapper withNullable(String candidateKey, Object value) {
        return value != null ? (StringMapMessageWrapper) super.with(candidateKey, value) : this;
    }
}
