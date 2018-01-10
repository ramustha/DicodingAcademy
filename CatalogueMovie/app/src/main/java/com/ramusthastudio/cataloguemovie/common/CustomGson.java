package com.ramusthastudio.cataloguemovie.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CustomGson {
  private final static Gson sGson;

  private CustomGson() { }

  static {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      @Override
      public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        try {
          return dateFormat.parse(json.getAsString());
        } catch (ParseException e) {
          return null;
        }
      }
    });
    sGson = gsonBuilder.create();
  }

  public static Gson instance() { return sGson; }
}
