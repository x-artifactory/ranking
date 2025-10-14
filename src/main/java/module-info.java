/**
 * Strategy Ranking module.
 */
module io.github.x.artifactory.strategy.ranking {
    requires jdk.jfr;
    requires jdk.jdi;
    requires java.management;
    requires java.compiler;
    exports io.github.x.artifactory.strategy.ranking.api;
}