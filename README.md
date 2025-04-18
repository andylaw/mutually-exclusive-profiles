# Mutually Exclusive Profiles Rule

## Background and purpose
This is a custom maven enforcer plugin rule to ensure only one of a set of mutually-exclusive
maven profiles is active at any one time. The driver for developing this was the need
to keep development, integration testing and production properties separate from each
other and to make sure that only one version was active at any one time.

More than one set of
profiles can be defined and each set is treated as a separate rule (see examples below).
Each set can additionally be configured to
require one member to be active.
The default setting is that zero or one member of a given set may be active
in order for the rule to allow the phase to continue.

I initially offered the code to the maven enforcer plugin, but - seemingly - there is no
demand for it. So here it is, set to languish in maven central obscurity. It was an
interesting journey, putting all the infrastructure in place to publish it. If you
find it useful, please let me know.

## Configuration

This rule runs as part of the maven-enforcer-plugin. It needs to be declared as a
dependency, and then configured as required in the `<plugins>` section under `<build>`.

The following parameters are supported by this rule:

* `<profilesRuleList>` - a list of sets of profiles, each of which is considered separately.
  The `<profilesRuleList>` tag contains one or more `<profilesRule>` tags:

  * `<profilesRule>` - a single set of mutually exclusive profiles

      * `<profilesString>` - A comma separated list of the names of profiles.

      * `<requireOne>` - An optional boolean value to indicate whether this rule requires one of the listed
        profiles to be active. Default is false
        (i.e. having none of the profiles listed being active is acceptable).



### Sample Plugin Configuration 1

This will fail the build if both `profile-1` and `profile-2` are active. If only one, or neither of them
is active then the build will continue.

```
<project>

[...]

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>3.5.0</version>

        <executions>
          <execution>
            <id>check-for-active-mutually-exclusive-profiles</id>
            <goals>
              <goal>enforce</goal>
            </goals>
            <configuration>
              <rules>
              
                <mutuallyExclusiveProfiles>
                  <profilesRuleList>
                  
                    <profilesRule>
                      <profilesString>profile-1,profile-2</profilesString>
                    </profilesRule>
                    
                  </profilesRuleList>
                </mutuallyExclusiveProfiles>
                
              </rules>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

[...]

</project>
```
### Sample Plugin Configuration 2

This will fail the build if both `profile-1` and `profile-2` are active, **_or_ if both are inactive**. Only if one of
the two specified profiles is active will the build continue.

```
<project>

[...]

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>3.5.0</version>

        <executions>
          <execution>
            <id>check-for-active-mutually-exclusive-profiles</id>
            <goals>
              <goal>enforce</goal>
            </goals>
            <configuration>
              <rules>
              
                <mutuallyExclusiveProfiles>
                  <profilesRuleList>
                  
                    <profilesRule>
                      <profilesString>profile-1,profile-2</profilesString>
                      <requireOnce>true</requireOnce>
                    </profilesRule>
                    
                  </profilesRuleList>
                </mutuallyExclusiveProfiles>
                
              </rules>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

[...]

</project>
```

### Sample Plugin Configuration 3

This configuration requires one or none of `[profile-1, profile-2]` **and** exactly one of `[a, b, c, d]` to be active.


```
<project>

[...]

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>3.5.0</version>

        <executions>
          <execution>
            <id>check-for-active-mutually-exclusive-profiles</id>
            <goals>
              <goal>enforce</goal>
            </goals>
            <configuration>
              <rules>

                <mutuallyExclusiveProfiles>
                  <profilesRuleList>

                    <profilesRule>
                      <profilesString>profile-1,profile-2</profilesString>
                    </profilesRule>
                    
                    <profilesRule>
                      <profilesString>a,b,c,d</profilesString>
                      <requireOne>true</requireOne>
                    </profilesRule>
                    
                  </profilesRuleList>
                </mutuallyExclusiveProfiles>
                
              </rules>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

[...]

</project>
```

Andy Law - April 2025
