package com.medelin.util;

import java.time.LocalDateTime;

public class TokenUtil
{
    public static LocalDateTime generateExpiryDate()
    {
        return LocalDateTime.now().plusMinutes(15);
    }
}
