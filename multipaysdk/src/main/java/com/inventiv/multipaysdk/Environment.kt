package com.inventiv.multipaysdk

enum class Environment(
    internal val baseUrl: String,
    internal val apiServicePath: String
) {
    DEV(
        "https://dev-multinet-gateway-api-pandateam.inventiv.services/",
        "multipay-sdk/v1/"

    ),
    PILOT(
        "https://pilot-gatewaymultinet-api.inventiv.services/",
        "multipay-sdk/v1/"

    ),
    TEST(
        "https://test-multinet-gateway-api.inventiv.services/",
        "multipay-sdk/v1/"

    ),
    PRODUCTION(
        "https://gateway-api.multinet.com.tr/",
        "multipay-sdk/v1/"
    )
}