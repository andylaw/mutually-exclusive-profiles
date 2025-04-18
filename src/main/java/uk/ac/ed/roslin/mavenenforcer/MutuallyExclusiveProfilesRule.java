package uk.ac.ed.roslin.mavenenforcer;

/*-
 * #%L
 * Mutually Exclusive Profiles Rule
 * %%
 * Copyright (C) 2025 Andy Law and the University of Edinburgh
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.inject.Inject;
import javax.inject.Named;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.enforcer.rule.api.AbstractEnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;

/**
 * Custom Enforcer Rule - mutually exclusive profiles
 */
@Named("mutuallyExclusiveProfiles")
public class MutuallyExclusiveProfilesRule extends AbstractEnforcerRule {

    /**
     * list of Profiles specified in the configuration.
     */
    private List<MutuallyExclusiveProfilesSet> profilesRuleList;

    // Inject needed Maven components

    @Inject
    private MavenProject project;

    @Inject
    private MavenSession session;

    @Inject
    private RuntimeInformation runtimeInformation;

    /**
     * The main code to execute the checks. If there are any failures, then the code will throw an EnforcerRuleException
     * @throws EnforcerRuleException the expected outcome if there are any rule violations
     */
    public void execute() throws EnforcerRuleException {

        getLog().debug("Retrieved Target Folder: " + project.getBuild().getDirectory());
        getLog().debug("Retrieved ArtifactId: " + project.getArtifactId());
        getLog().debug("Retrieved Project: " + project);
        getLog().debug("Retrieved Maven version: " + runtimeInformation.getMavenVersion());
        getLog().debug("Retrieved Session: " + session);
        getLog().debug(() -> "Mutually Exclusive Profiles: " + profilesRuleList);
        getLog().debug("Profiles Active: " + project.getActiveProfiles());

        // Check each of our rules
        List<String> failures = new ArrayList<>();
        // Fail if we have more than one of our specified profiles active
        for (MutuallyExclusiveProfilesSet profileSet : this.profilesRuleList) {

            getLog().info("Profile Set: " + profileSet);

            if (! profileSet.ruleIsSatisfied(project.getActiveProfiles())) {
                failures.add("  " + profileSet);
            }
        }

        if (!failures.isEmpty()) {
            String failureString = "The following Mutually Exclusive Profile Set rule(s) failed:"
                    + System.lineSeparator()
                    + String.join(System.lineSeparator(), failures)
                    + System.lineSeparator()
                    + "Profiles Active were: " + project.getActiveProfiles();
            throw new EnforcerRuleException(failureString);
        }
    }


    /**
     * @return a text description of the rule
     */
    @Override
    public String toString() {
        return String.format("MutuallyExclusiveProfiles[profilesRuleList=%s]", profilesRuleList);
    }
}
