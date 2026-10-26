# Selenium + TestNG + Jenkins

Java/Maven automation project with a declarative Jenkinsfile. Six tests: three browser scenarios (valid login, invalid login, logout) plus three configuration/HTTP checks. Chrome runs headless by default. The bundled demo page is served on a dynamically assigned loopback port, so no public practice website, Appium, Grid, or Docker is required. Includes the corrected hidden-button CSS from the earlier project.

## 1. Prepare the Jenkins execution machine
Install a full JDK 21, Maven 3.9+, Git, and Google Chrome on the Windows machine/agent that actually runs builds. Jenkins controller Java requirements are separate; use a Jenkins LTS compatible with Java 21. Downloads:
- Jenkins: https://www.jenkins.io/download/
- Chrome: https://www.google.com/chrome/
- Maven: https://maven.apache.org/download.cgi

If Jenkins runs as a Windows service, its account may have a different PATH, home directory, permissions, and Chrome installation visibility than your interactive account. Prefer a dedicated Jenkins agent with Chrome installed for all users. Selenium Manager needs network access and a writable cache in that agent account's home to download ChromeDriver on first use. If needed set CHROME_BINARY in the agent environment to the actual chrome.exe path, or configure chrome.binary in config.properties. Restart the agent/service after changing its environment. For Linux agents install Chrome and required OS libraries and run the agent under an ordinary user account. Do not disable Chrome sandboxing as a default workaround.

## 2. Verify locally first
In Command Prompt in the folder containing pom.xml:
```
mvn clean test
mvn test -Dheadless=false
mvn -Dtest=ConfigurationTest test
```
The last command needs no Chrome and tests the configuration and HTTP fixture. Full execution opens a fresh browser for each browser test. Tests are sequential; no shared parallel driver configuration is included.

## 3. Upload to GitHub
Create an empty GitHub repository. Commit this project's sources, pom.xml, testng.xml, and Jenkinsfile. Do not commit target, reports, credentials, or downloaded drivers. Example commands (replace the repository placeholder with your copied actual URL):
```
git init
git branch -M main
git add .
git commit -m "Add Selenium TestNG Jenkins pipeline"
git remote add origin YOUR_ACTUAL_REPOSITORY_URL
git push -u origin main
```

## 4. Configure Jenkins plugins and tools
Under Manage Jenkins > Plugins ensure Pipeline (Declarative Pipeline), Git, JUnit, and Maven Integration plugins are installed. Jenkins's JUnit plugin reads Surefire's JUnit-format XML; the test framework itself remains TestNG. A TestNG Jenkins reporting plugin is not required.
Under Manage Jenkins > Tools:
- Add a JDK named exactly JDK21. Point JAVA_HOME to your full JDK directory, without bin. Uncheck automatic install if using a local installation.
- Add Maven named exactly Maven3. Point MAVEN_HOME to your actual Maven installation directory, without bin, or enable an available automatic installer.
For agents on different operating systems, configure appropriate node-specific tool locations/installation so these names work on the selected agent.
Under Manage Jenkins > Nodes choose your execution node and add the label selenium. It must have Chrome and be online with at least one executor. The Jenkinsfile requests that label. For a local learning-only setup, you can label the built-in node selenium; use a separate agent for a shared/production Jenkins installation.

## 5. Create a Pipeline job
1. New Item > SeleniumTestNGJenkins > Pipeline > OK.
2. Under Pipeline choose Definition: Pipeline script from SCM.
3. SCM: Git; Repository URL: your actual GitHub clone URL.
4. For a private repository select Jenkins-managed credentials with read access; never put tokens in Jenkinsfile.
5. Branch Specifier: */main. Script Path: Jenkinsfile (case-sensitive).
6. Save > Build Now. Open Console Output.

Stages: Checkout -> Verify tools -> Run Selenium TestNG suite. The pipeline uses bat on Windows and sh on Unix. Builds have a 20-minute timeout, are sequential per job, and use a 15-build retention limit. A Maven/test failure is not swallowed or converted to success.

## 6. View results
After the build, Jenkins shows a Test Result link and trend from reports/testng/TEST-*.xml. Reports and screenshots are also archived under Build Artifacts, even when tests fail and files exist. TestNG HTML is reports/testng/index.html. Download the whole reports folder to view HTML assets together; Jenkins may restrict active HTML content via its content security policy, so the built-in Test Result view is the simplest way to inspect results. No HTML Publisher plugin is required.

If compilation or setup fails before XML generation, the publisher permits missing XML so it doesn't hide the original Maven/tool error. The pipeline is still failed by that original error. Old reports are removed before checkout to prevent stale result publication. Tests themselves are not allowed to silently pass with no tests: Surefire failIfNoTests is true.

## Config
src/test/resources/config.properties provides defaults; -D overrides environment variables, which override file values. headless defaults true. fixture.port=0 selects a free port. Keep fixture.bind=127.0.0.1 for this local-browser project. wait.seconds=15 and page.load.seconds=30. Optional chrome.binary/CHROME_BINARY points to Chrome on the execution agent. The demo uses public synthetic credentials demo / DemoPassword123, not production authentication.

## Eclipse
Import through File > Import > Maven > Existing Maven Projects. Maven > Update Project. With TestNG for Eclipse installed, right-click testng.xml > Run As > TestNG Suite. Jenkins executes the same suite via Maven Surefire. Eclipse settings and installed Eclipse plugins do not configure the Jenkins service account automatically.

## Troubleshooting
- Build waiting for executor: ensure an online node has the selenium label.
- Tool installation not found: JDK21 and Maven3 must match configured Jenkins tool names exactly.
- Chrome/driver startup failure: confirm agent account access to Chrome, network, writable cache; inspect console output.
- mvn not recognised locally: fix your local PATH. Jenkins uses its configured Maven tool rather than relying on your terminal's temporary PATH.
- No report: compilation/agent startup may have failed before tests; inspect the earliest console error.
- Images are saved only when browser startup succeeded; no screenshot is possible without a session.
