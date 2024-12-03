## XaviEH - A tool for Exception Handling (EH) Code Coverage and Mutation Analysis

#### Coverage Criteria

- [x] General Code Coverage (instruction, branch, and method)
- [x] EH Code Coverage (throw instructions, instructions and branches within try, catch, and finally blocks, and methods with throws)
                                       
#### Mutation Operators

- [x] CBD - Catch Block Deletion
- [x] CBI - Catch Block Insertion
- [x] CRE - Catch and Rethrow Exception
- [x] FBD - Finally Block Deletion
- [x] CBR - Catch Block Replacement
- [x] PTL - Placing Try Block Late
- [x] TSD - Throw Statement Deletion

#### Usage

- use **mvn install**
- Enter in **/target**
- Execute: **java -jar project-mutant-2.0.0-jar-with-dependencies.jar** for run the program.
- The file **config.properties** (which is inside the folder (**/target/FilesNeedToRun**) - **be sure to copy the files from this folder to the directory where the executable jar file is located.**), has all the necessary parameters for the project to work correctly, they are:
  - **homeMaven**: path to the maven installed on your computer.
  - **urlMutations**: path where the mutants and information will be created (always finish the path with a slash at the end. **/**).
  - **projectsFile**: name of the file containing the projects that will be used. The same is composed of a url of git and some additional parameters, they are:
    - **-v**: defines the version of the project in question. (**required**)
    - **-c**: ID of commit of project.
    - **-m**: Submodule of project.
    - **-p**: Type of project (Maven or NotMaven) (parameter **g** to not maven project)
    - **-pp**: Path of default file project location. (Default: **/src/main/java**)
    - **Obs: The parameters are optionals, exception for [-v]** 


###### PARAMS: 
- **noTestProject**: The project will not be test before 
- **outputFile**: The console output will be saved to a local file.
- **noCloneRepository**: The file projects will be run without cloning the git repository.
- **noVerifyProject**: Before the project is analyzed it is checked if it has already been run before, if positive, it will be skipped. This option causes the project to not be parsed.
- Type of executions: 
  - **ExecuteMutationsAndCoverage**: The mutants will be created and also their coverage using JaCoCo.
  - **ExecuteEstatisticsCoverageEH**: information returned:
    - **MI**: Missed Instruction
    - **CI**: Covered Instruction 
    - **MB**: Missed Branch
    - **CB**: Covered Branch
    - **MM**: Missed Method	
    - **CM**: Covered Method
    - **THROW_MI**: Missed Instruction Throw
    - **THROW_CI**: Covered Instruction Throw
    - **THROWI_MI**: Missed Instruction Throw (Internal Exception) 
    - **THROWI_CI**: Covered Instruction Throw (Internal Exception)
    - **THROWE_MI**: Missed Instruction Throw (Internal Exception)
    - **THROWE_CI**: Covered Instruction Throw (External Exception)
    - **CATCH_MI**: Missed Instruction Catch
    - **CATCH_CI**: Covered Instruction Catch
    - **CATCH_MB**: Missed Branch Catch
    - **CATCH_CB**: Covered Branch Catch
    - **CATCHI_MI**: Missed Instruction Catch (Internal Exception)
    - **CATCHI_CI**: Covered Instruction Catch (Internal Exception)
    - **CATCHI_MB**: Missed Branch Catch (Internal Exception)
    - **CATCHI_CB**: Covered Branch Catch (Internal Exception)
    - **CATCHE_MI**: Missed Instruction Catch (External Exception)
    - **CATCHE_CI**: Covered Instruction Catch (External Exception)
    - **CATCHE_MB**: Missed Branch Catch (External Exception)
    - **CATCHE_CB**: Covered Branch Catch (External Exception)
  - **ExecuteEstatisticsCoverageEH2**: information returned:
    - **MI**: Missed Instruction
    - **CI**: Covered Instruction 
    - **MB**: Missed Branch
    - **CB**: Covered Branch
    - **MM**: Missed Method	
    - **CM**: Covered Method
    - **TRY_MI**: Missed Instruction Try
    - **TRY_CI**: Missed Instruction Try
    - **TRY_MB**: Missed Branch Try	
    - **TRY_CB**: Covered Branch Try	
    - **CATCH_MI**: Missed Instruction Try
    - **CATCH_CI**: Covered Instruction Try
    - **CATCH_MB**:	Missed Branch Try
    - **CATCH_CB**: Covered Branch Try
    - **FINALLY_MI**: Missed Instruction Finally
    - **FINALLY_CI**: Covered Instruction Finally
    - **FINALLY_MB**: Missed Branch Finally
    - **FINALLY_CB**: Covered Branch Finally
    - **THROW_MI**: Missed Instruction Throw
    - **THROW_CI**: Covered Instruction Throw
    - **THROWS_MM**: Missed Method Throws
    - **THROWS_CM**: Covered Method Throws
  - **ExecuteCloneAndRunTestsWithJaCoCo**: Make only tests and JaCoCo report of projects.


- Como rodar JaCoCo Tests com Gradlew

#### Adicionar no `build.gradle` Versões antigas -> 
```
plugins {
    ...
    id 'jacoco'
}

...
jacocoTestReport {
    group = "Reporting"
    reports {
        xml.enabled true
        csv.enabled true
	xml.destination "${buildDir}/../target/site/jacoco/jacoco.xml"
	csv.destination "${buildDir}/../target/site/jacoco/jacoco.csv"
        html.destination "${buildDir}/../target/site/jacoco"
    }
}
...
apply plugin: "java"
apply plugin: "jacoco"

```


#### Versões mais recentes

```

plugins {
    ...
    id 'jacoco'
}
...
test {
    finalizedBy jacocoTestReport // report is always generated after tests run
}

jacoco {
    toolVersion = "0.8.5"
    reportsDir = file("$buildDir/customJacocoReportDir")
}

jacocoTestReport {
    dependsOn test // tests are required to run before generating the report
    reports {
        xml.enabled true
        csv.enabled true
        html.enabled true
        xml.destination file("${buildDir}/../target/site/jacoco/jacoco.xml")
        csv.destination file("${buildDir}/../target/site/jacoco/jacoco.csv")
        html.destination file("${buildDir}/../target/site/jacoco")
    }
}
```