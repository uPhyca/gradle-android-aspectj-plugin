Release Process
===============

 1. Update the `CHANGELOG.md` file with relevant info and date.
 2. Update version number in `gradle.properties` file.
 3. Update version number in `README.md` file.
 4. Commit: `git commit -am "Prepare version X.Y.Z."`
 5. Tag: `git tag -a X.Y.Z -m "Version X.Y.Z"`
 6. Push: `git push && git push --tags`
 7. Release: `./gradlew clean check assemble uploadArchives`
 8. Update version number in `gradle.properties` file to next "SNAPSHOT" version.
 9. Commit: `git commit -am "Prepare next development version."`
 10. Push: `git push`
 11. Write a script for steps 2 - 11. *(Optional)*
