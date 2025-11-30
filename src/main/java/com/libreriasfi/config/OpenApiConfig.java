package com.libreriasfi.config; // ajusta al package que uses

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "API Librería SciFiTerror",
        version = "v1",
        description = "API del proyecto DSY1104 - Librería online"
    )
)
public class OpenApiConfig {
}
