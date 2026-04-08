// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "WikibrokerOpenapiSdk",
    platforms: [
        .macOS(.v10_15),
        .iOS(.v12),
    ],
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "WikibrokerOpenapiSdk",
            targets: ["WikibrokerOpenapiSdk"]
        )
    ],
    dependencies: [
        .package(
            url: "https://github.com/Alamofire/Alamofire.git",
            .upToNextMajor(from: "5.11.0")
        )
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "WikibrokerOpenapiSdk"
        ),
        .testTarget(
            name: "WikibrokerOpenapiSdkTests",
            dependencies: [
                "WikibrokerOpenapiSdk",
                .product(name: "Alamofire", package: "Alamofire"),
            ]
        ),
    ],
    swiftLanguageModes: [.v6]
)
