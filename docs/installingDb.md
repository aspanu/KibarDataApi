# Db Installation

1. Install Flyway (for OS X can be done with homebrew)

2. Install MySql, add a user and a db and add the db and user credentials to db/flyway.conf (it has now been added to gitignore so it shouldn't be committed)

3. Run `flyway migrate -configFile=db/flyway.conf -locations=filesystem:db/sql` to populate the db.

4. Re-run the flyway migrate to update it to the most recent version.