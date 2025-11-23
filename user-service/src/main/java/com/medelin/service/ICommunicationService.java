package com.medelin.service;

public interface ICommunicationService
{
    public void sendPasswordResetEmail(String to, String link);
}
