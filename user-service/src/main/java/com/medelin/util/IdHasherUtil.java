package com.medelin.util;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IdHasherUtil
{
    @Value("${hashids.secret-key}")
    private String secret;
    private final Hashids hashids;

    public IdHasherUtil()
    {
        String salt = secret;
        int minLength = 25;
        this.hashids = new Hashids(salt, minLength);
    }

    public String encode(Long id)
    {
        return hashids.encode(id);
    }

    public Long decode(String hashedId)
    {
        long[] decoded = hashids.decode(hashedId);
        if (decoded.length == 0)
        {
            throw new IllegalArgumentException("Invalid hashed ID");
        }
        return decoded[0];
    }
}
