package de.fh_koeln.ixtrieve.srd;

import java.io.Writer;
import java.io.IOException;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class XMLResponseWriter extends org.apache.solr.response.XMLResponseWriter {

  private ResponseDigester rd = null;

  @Override
  public void init(final NamedList n) {
    final SolrParams p = SolrParams.toSolrParams(n);
    rd = new ResponseDigester(p.get("key"));
  }

  @Override
  public void write(final Writer writer, final SolrQueryRequest req, final SolrQueryResponse rsp) throws IOException {
    super.write(writer, req, rd.digest(this, req, rsp));
  }

}
