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


/**
 * An Enumeration to report the current status of a MutuallyExclusiveProfilesSet
 */
public enum RuleStatus {
    /**
     * Default state - the rule is as yet untested
     */
    UNTESTED("Untested"),
    /**
     * More than one of the profiles is active, so this is a fail
     */
    FAILED_MULTIPLES_ACTIVE("Failed because more than one of the mutually exclusive profiles were active"),
    /**
     * None of the profiles is active, but we specified that one had to be available, so this is a fail
     */
    FAILED_NONE_ACTIVE("Failed because none of the mutually exclusive profiles were active and you specified that one was required"),
    /**
     * The rule has passed
     */
    OK("OK");

    private final String displayString;

    /**
     * Default constructor which stashes the human-readable status string
     * @param displayString the human-readable string that describes the particular enumeration state
     */
    RuleStatus(String displayString) {
        this.displayString = displayString;
    }

    @Override
    public String toString() {
        return displayString;
    }
}
