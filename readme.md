Phresh vaccination application for users to view their data.
It has been written in Vaadin 24, sadly I missed the need for Vaadin 8 in the initial set of rules.
Uses latest version of Springboot that works for Vaadin 24. I have created a feature branch to start working on Vaadin
8, but ran out of time.
For the feature branch, the login view and create user view exists, and a connection to a database can be used, however
the DB needs to be manually setup.

For the Vaadin 24 version, the test users are created in a dummy launch class called AppSetupService. The example users
that are created:

fred.flintstone@post.lu
pebbleRockBamBam1-

rumble.flintstone@post.lu
babyFlinstoneJustBorn2025!

Project builds using Maven clean verify and install.