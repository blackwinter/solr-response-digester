package de.fh_koeln.ixtrieve.srd;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.BasicResultContext;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;

public class ResponseDigester {

  private static final String ALGORITHM = "SHA-256";

  private static final byte[] SEPARATOR = ":".getBytes();

  private MessageDigest digest = null;

  private String name = "srd";

  private final String key;

  ResponseDigester(final String name, final String key) {
    this(key);
    this.name = name;
  }

  ResponseDigester(final String key) {
    this.key = key;

    try {
      this.digest = MessageDigest.getInstance(ALGORITHM);
    } catch(NoSuchAlgorithmException e) {}
  }

  public SolrQueryResponse digest(final QueryResponseWriter writer, final SolrQueryRequest req, final SolrQueryResponse rsp) {
    if (key != null) {
      final String value = Long.toString(System.currentTimeMillis());

      final ArrayList<String> array = new ArrayList<>();
      array.add(name);
      array.add(key);
      array.add(value);
      array.add(writer.getContentType(req, rsp));
      array.add(Integer.toString(
          ((BasicResultContext)rsp.getResponse()).getDocList().matches()));

      final NamedList<String> list = new NamedList<>();
      list.add("key", key);
      list.add("value", value);
      list.add("digest", digest(array));

      rsp.getResponseHeader().add(name, list);
    }

    return rsp;
  }

  public String digest(final ArrayList<String> array) {
    if (digest == null) {
      return "";
    }

    digest.reset();

    for (final String item : array) {
      digest.update(item.getBytes());
      digest.update(SEPARATOR);
    }

    return new BigInteger(1, digest.digest()).toString(16);
  }

}
