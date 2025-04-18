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

import org.apache.maven.model.Profile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uk.ac.ed.roslin.mavenenforcer.RuleStatus.*;


/**
 * An object that represents a set of mutually exclusive maven profile names and will check the validity of the
 * build environment by comparing that list of profiles against the list of active profiles.
 */
public class MutuallyExclusiveProfilesSet {

    /**
     * The Set of profiles that are mutually exclusive
     */
    private final Set<String> profileNames = new HashSet<>();

    /**
     * Do we require one of them to be set?
     */
    private boolean requireOne = false;

    /**
     * A record of the current status of the rule
     */
    private RuleStatus status = UNTESTED;

    /**
     * Set the profileString, and split the list of profile names it contains into the profileNames Set
     * @param profilesString the list of mutually exclusive profile names from the pom configuration
     */
    public void setProfilesString(String profilesString) {
        this.profileNames.clear();
        for (String profile : profilesString.split(",")) {
            this.profileNames.add(profile.trim());
        }
    }

    /**
     * Set the value of the requireOne property. This is true if one of the profiles <b>must</b> be active
      * @param b the value to be set
     */
    public void setRequireOne(boolean b) {
        this.requireOne = b;
    }

    /**
     * Default constructor
     */
    public MutuallyExclusiveProfilesSet() {}

    /**
     * Check if this particular rule is satisfied. Counts how many of the mutually exclusive profiles are
     * currently active, and records a status as appropriate.
     * @param activeProfileNames a List of the currently active Profiles
     * @return true if the rule criteria are met, false otherwise
     */
    public boolean ruleIsSatisfied(List<Profile> activeProfileNames) {

        // This line is equivalent to a for loop counting how many of the profiles specified in
        // this rule are included in the supplied list of active profiles
        long countActives = activeProfileNames.stream()
                .map(Profile::getId)
                .filter(this.profileNames::contains)
                .count();
        // If we requireOne, then countActives must be 1. If we don't requireOne, then it must be <= 1
        if (countActives > 1) {
            this.status = FAILED_MULTIPLES_ACTIVE;
        }
        else if (countActives == 0 && this.requireOne) {
            this.status = FAILED_NONE_ACTIVE;
        }
        else {
            this.status = OK;
        }
        return this.status == OK;
    }

    /**
     * @return rule description
     */
    @Override
    public String toString() {
        return String.format("MutuallyExclusiveProfilesSet[oneRequired=%b, profileList=%s] - status: %s",
                requireOne, this.profileNames, this.status);
    }
}
