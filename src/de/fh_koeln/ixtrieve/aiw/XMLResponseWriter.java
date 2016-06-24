package de.fh_koeln.ixtrieve.aiw;

import java.io.Writer;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.BasicResultContext;
import org.apache.solr.response.SolrQueryResponse;

public class XMLResponseWriter extends org.apache.solr.response.XMLResponseWriter {

  private String name = "aiw";

  private String key = null;

  @Override
  public void init(NamedList n) {
    final SolrParams p = SolrParams.toSolrParams(n);
    key = p.get("key");
  }

  @Override
  public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp) throws IOException {
    if (key != null) {
      NamedList<Object> list = new NamedList<>();

      String value = Long.toString(System.currentTimeMillis());

      String numFound = Integer.toString(
          ((BasicResultContext)rsp.getResponse()).getDocList().matches());

      list.add("key", key);
      list.add("value", value);

      try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        digest.reset();
        digest.update((name + ":" + key + ":" + numFound + ":").getBytes());

        list.add("digest", new BigInteger(1, digest.digest(value.getBytes())).toString(16));
      } catch(NoSuchAlgorithmException e) {}

      rsp.getResponseHeader().add(name, list);
    }

    super.write(writer, req, rsp);
  }

}
