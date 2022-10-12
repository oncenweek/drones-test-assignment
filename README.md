## Drones

[[_TOC_]]

---

:scroll: **START**

### Summary
This is IntellijIDEA Gradle project, uses in-memory H2 database, (all data resets to default on each run)
for build and run **Gradle** and **Java** must be in the PATH variable

### Build
1. Build via IDEA
2. execute console command in project directory ***gradlew bootJar***

###Run
1. Build via IDEA (bootRun task in gradle view)
2. execute execute console command in project directory ***gradlew bootRun***
3. execute builded on step 2 jar file: in directory **.\andrey-semenov\drones\build\libs** execute command ***java -jar drones-1.0-SNAPSHOT.jar*** 

###Test
project ups web server on port 8080 and has following endpoints:
1. _drone/register_ register a new drone, PUT method, accept json in following format:
{
    serialNumber: string
    model: 'LIGHTWEIGHT'|'MIDDLEWEIGHT'|'CRUSERVEIGHT'|'HEAVYWEIGHT';
	weightLimit: integer
}
returns JSON-formatted description of drone

2. _drone/load/{droneId}_ load drone with medication ***PUT*** method, path variable *droneId* is ID of drone, accepts integer value - ID of medication to load into drone

3. _drone/battery-level/{droneId}_ returns integer value of drone battery level (0-100), ***GET*** method, path variable *droneId* is ID of drone

4. _drone/payload/{droneId}_ returns array of JSON-formatted descriptions of loaded medications, ***GET*** method, path variable *droneId* is ID of drone

5. _drone/available-for-loading_ returns array of drones, that available for loading, ***GET*** method without params

6. _drone/medications_ return array of available in system medications, (uses, for example, to get ID of medication for _load_ method), ***GET*** method without params

7. _drone/audit/{droneId}/{maxRecords}_ return array of audit records, that added by periodic audit task. ***GET*** method, path variable *droneId* is ID of drone, path variable *maxRecords* is maximum number of entries to show (entries are sorted by creation time, desc)

:scroll: **END** 
