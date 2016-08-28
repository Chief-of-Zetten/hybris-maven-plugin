package com.divae.ageto.hybris.utils.aether;

import java.util.List;
import java.util.Set;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

import com.divae.ageto.hybris.install.task.dependencies.DependencyWrapper;
import com.google.common.collect.Sets;

/**
 * Created by mhaagen on 28.08.2016.
 */
public class DependencyResolver {

    public static Set<DependencyWrapper> listTransitiveDependencies(Set<DependencyWrapper> dependencySet) {
        Set<DependencyWrapper> dependencies = Sets.newHashSet();

        for (DependencyWrapper dependency : dependencySet) {
            RepositorySystem system = Booter.newRepositorySystem();

            RepositorySystemSession session = Booter.newRepositorySystemSession(system);

            Artifact artifact = new DefaultArtifact(
                    String.format("%s:%s:%s", dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion()));

            DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

            CollectRequest collectRequest = new CollectRequest();
            collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
            collectRequest.setRepositories(Booter.newRepositories(system, session));

            DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFlter);

            List<ArtifactResult> artifactResults = null;
            try {
                artifactResults = system.resolveDependencies(session, dependencyRequest).getArtifactResults();
            } catch (DependencyResolutionException e) {
                continue;
            }

            for (ArtifactResult artifactResult : artifactResults) {
                Artifact artifact1 = artifactResult.getArtifact();
                dependencies
                        .add(new DependencyWrapper(artifact1.getGroupId(), artifact1.getArtifactId(), artifact1.getVersion()));
            }
        }

        return dependencies;
    }
}