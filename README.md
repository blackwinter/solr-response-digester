Implements a [Solr](https://lucene.apache.org/solr/) `XMLResponseWriter` class that adds a unique digest (based on configuration key, number of results found, and current time in milliseconds) to the response header.

## Build instructions

```sh
jrake DIR=path/to/lucene-solr
```

## Configuration (`solrconfig.xml`)

```xml
<config>
  [...]
  <lib path="${solr.install.dir:../../../..}/de.fh_koeln.ixtrieve.aiw.jar" />
  <queryResponseWriter name="xml" class="de.fh_koeln.ixtrieve.aiw.XMLResponseWriter">
    <str name="key">KEY</str>
  </queryResponseWriter>
</config>
```

## Result

```xml
<lst name="responseHeader">
  [...]
  <lst name="aiw">
    <str name="key">KEY</str>
    <str name="value">1466780880672</str>
    <str name="digest">76b22e2dc672fbb7405a06fb7cd48612adf46e60bfa94fba790ce4ce0ffdc26a</str>
  </lst>
</lst>
```
