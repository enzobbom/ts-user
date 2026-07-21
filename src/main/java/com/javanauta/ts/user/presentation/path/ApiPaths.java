package com.javanauta.ts.user.presentation.path;

import com.javanauta.ts.apicontract.version.ApiVersions;

public final class ApiPaths {
    private ApiPaths() {}

    public static final String USERS_V1 = ApiVersions.V1 + "/users";
    public static final String AUTH_V1 = ApiVersions.V1 + "/auth";
    public static final String CEP_V1 = ApiVersions.V1 + "/ceps";
}
