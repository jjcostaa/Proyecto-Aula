package com.fitzone.fitzonev2.controller;

import java.util.concurrent.ConcurrentHashMap;

public class TokenStorage {
    public static ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();
}
