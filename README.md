# gradle-jacoco-aggregate

An example for how to use publication variants to gather files from tasks in projects.
This is the full example for [my StackOverflow](https://stackoverflow.com/a/56714097/5391954) answer about aggregating JaCoCo reports. 

## Question

The question boils down to:

> How can I gather individual JaCoCo reports from sub projects into one folder in the root project

## Solution

The solution comprises of two steps:

### 1. Publishing the JaCoCo reports as a variant of the sub projects

The idea is to have a pre-compiled script plugin in `buildSrc/src/main/kotlin/jacoco-conventions.gradle.kts` that configures JaCoCo.
The plugin defines a custom [configuration](https://docs.gradle.org/current/userguide/managing_dependency_configurations.html) called `jacocoReports`.
`jacocoReports` is consumable, meaning other projects can depend on it, but we can not get files by declaring dependencies in this configuration inside the `dependencies` block.
It has a `USAGE_ATTRIBUTE` set to `jacocoReports` which is needed to consume it from other projects.
Last but not least the outgoing artifacts of this configuration are the JaCoCo XML reports.
Since we want the reports to be created when the configuration is consumed, we need to tell the configuration which task creates the outgoing artifact.
This tells Gradle to call the `jacocoTestReport` task when the `jacocoReports` configuration is consumed.

### 2. Consuming the variants in the root project

In order to gather the the reports in the root project we need to create a configuration that consumes the `jacocoReports` publication variants.
This is what the `jacocoReports` configuration in `build.gradle.kts` does.
This time the configuration is not consumable, but resolvable, meaning that we can get the files that are in this configuration by resolving it.
Furthermore the `jacocoReports` configuration has the `USAGE_ATTRIBUTE` set to `jacocoReports`.
This why when we define a dependency in `jacocoReports` to another project it will use this variant.

The next step is to define project dependencies using the `jacocoReports` configuration.
Then we can have a simple copy task that copies the files from the `jacocoReports` configuration the the destination directory.

## Contribution policy

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License

This code is open source software licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).
