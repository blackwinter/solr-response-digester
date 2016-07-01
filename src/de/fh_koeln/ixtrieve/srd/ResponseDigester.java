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

  private final String ALGORITHM = "SHA-256";

  private final byte[] SEPARATOR = ":".getBytes();

  private MessageDigest digest = null;

  private String name = "srd";

  private String key = null;

  ResponseDigester(String n, String k) {
    this(k);
    name = n;
  }

  ResponseDigester(String k) {
    key = k;

    try {
      digest = MessageDigest.getInstance(ALGORITHM);
    } catch(NoSuchAlgorithmException e) {}
  }

  public SolrQueryResponse digest(QueryResponseWriter writer, SolrQueryRequest req, SolrQueryResponse rsp) {
    if (key != null) {
      String value = Long.toString(System.currentTimeMillis());

      ArrayList<String> array = new ArrayList<>();
      array.add(name);
      array.add(key);
      array.add(value);
      array.add(writer.getContentType(req, rsp));
      array.add(Integer.toString(
          ((BasicResultContext)rsp.getResponse()).getDocList().matches()));

      NamedList<String> list = new NamedList<>();
      list.add("key", key);
      list.add("value", value);
      list.add("digest", digest(array));

      rsp.getResponseHeader().add(name, list);
    }

    return rsp;
  }

  public String digest(ArrayList<String> array) {
    if (digest == null) {
      return "";
    }

    digest.reset();

    for (String item : array) {
      digest.update(item.getBytes());
      digest.update(SEPARATOR);
    }

    return new BigInteger(1, digest.digest()).toString(16);
  }

}
