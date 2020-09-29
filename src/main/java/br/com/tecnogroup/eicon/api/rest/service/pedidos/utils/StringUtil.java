package br.com.tecnogroup.eicon.api.rest.service.pedidos.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class StringUtil {

    private StringUtil() {

    }

    public static String formatConteudoJSONFrom(String conteudo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(conteudo);
        return gson.toJson(je);
    }

    public static String formatLocalDate(LocalDate data) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data);
    }
}
