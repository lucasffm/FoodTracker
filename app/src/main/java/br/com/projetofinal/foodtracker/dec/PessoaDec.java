package br.com.projetofinal.foodtracker.dec;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.projetofinal.foodtracker.modelo.Pessoa;

/**
 * Created by Lucas on 19/09/2016.
 */
public class PessoaDec implements JsonDeserializer {


    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement element = json.getAsJsonObject();
        if (json.getAsJsonObject() != null){
            element = json.getAsJsonObject();
        }
        return (new Gson().fromJson(element, Pessoa.class));
    }
}
