package br.com.elos.serialization;

import br.com.elos.helpers.Parse;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Calendar;

public class CalendarAdapter extends TypeAdapter<Calendar> {

    @Override
    public void write(JsonWriter writer, Calendar calendar) throws IOException {
        writer.value(new Parse().toString(calendar, "yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Override
    public Calendar read(JsonReader reader) throws IOException {
        return new Parse().toCalendar(reader.nextString().replace("T"," "), "yyyy-MM-dd HH:mm:ss");
    }
    
}
