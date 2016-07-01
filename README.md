Implements a [Solr](https://lucene.apache.org/solr/) `XMLResponseWriter` class that adds a unique digest (based on configuration key, number of results found, and current time in milliseconds) to the response header.

## Build instructions

```sh
jrake DIR=path/to/lucene-solr
```

## Configuration (`solrconfig.xml`)

```xml
<config>
  [...]
  <lib path="${solr.install.dir:../../../..}/de.fh_koeln.ixtrieve.srd.jar" />
  <queryResponseWriter name="xml" class="de.fh_koeln.ixtrieve.srd.XMLResponseWriter">
    <str name="key">KEY</str>
  </queryResponseWriter>
</config>
```

## Result

```xml
<lst name="responseHeader">
  [...]
  <lst name="srd">
    <str name="key">KEY</str>
    <str name="value">1467375800940</str>
    <str name="digest">1c12a72548ab96c1aba506cbb0feb34652d382a39c1017b54e3125406e0bd7a5</str>
  </lst>
</lst>
```
