allprojects {
    repositories {
        jcenter()
    }
}

val jacocoReports by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage::class, "jacocoReports"))
    }
}

dependencies {
    jacocoReports(project(":b:c"))
    jacocoReports(project(":b:d:e"))
}

tasks.register<Copy>("aggregateJacocoReports") {
    from(jacocoReports)
    into(file("$buildDir/jacoco"))
}
