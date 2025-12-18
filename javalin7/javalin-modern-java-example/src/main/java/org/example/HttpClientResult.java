package org.example;

import java.net.http.HttpResponse;

public record HttpClientResult(HttpResponse<String> response, Throwable error) { }
