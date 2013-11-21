Contains a stress tester for Kiji Express jobs.  Does two things: prepares a Kiji instance by creating, populating, or dropping tables; and runs stress-testing jobs against the tables.

## The things:

install [leiningen](http://leiningen.org/).

* `lein pom` to generate a pom file for the project.  Useful for the IDE's.
* `lein prepare --create` to create the test table.  Uses the ddl in `resources/ddl/memory_stress.ddl`.
* `lein prepare --drop` to drop the test table.
* `lein prepare --load` to load rows into the test table.  Use `--rows N`, `--versions N`, and `--columns foo,bar,baz` to specify how many rows, how many versions per row, and what columns to load data into, respectively.  Defaults to 10000, 1, and `strings`.
* `lein scald foo.bar.baz.SomeJob --hdfs` to run a job.  Checkout `src/scala/org/kiji/express/memory/Jobs.scala` for some jobs, and their associated CLI options.

The `prepare` commands also take a optional `--uri kiji://foo.bar:2181/myInstance` option which defaults to `kiji://localhost:2181/default`.

----

(c) Copyright 2013 WibiData, Inc.

See the NOTICE file distributed with this work for additional
information regarding copyright ownership.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
