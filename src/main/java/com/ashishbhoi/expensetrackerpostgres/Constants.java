package com.ashishbhoi.expensetrackerpostgres;

public class Constants {

    public static final String API_SECRET_KEY = System.getenv("API_SECRET_KEY");

    public static final long TOKEN_VALIDITY = 2 * 60 * 60 * 1000;
}
