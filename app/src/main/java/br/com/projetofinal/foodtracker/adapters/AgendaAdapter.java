package br.com.projetofinal.foodtracker.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.modelo.Agenda;

/**
 * Created by Lucas on 05/01/2017.
 */

public class AgendaAdapter extends ArrayAdapter<Agenda> {

    private final Context context;
    private final List<Agenda> agendas;

    public AgendaAdapter(Context context, List<Agenda> agendas) {
        super(context, R.layout.linha, agendas);
        this.context = context;
        this.agendas = agendas;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.linha, viewGroup, false);


        TextView data = (TextView) rowView.findViewById(R.id.tv_data_consulta_agenda);
        TextView tipoComida = (TextView) rowView.findViewById(R.id.tv_tipo_comida_consulta_agenda);
        TextView horaInicio = (TextView) rowView.findViewById(R.id.txt_hora_inicio);
        TextView horaFim = (TextView) rowView.findViewById(R.id.txt_hora_fim);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dataD = agendas.get(i).getData();
        String dataString = formatter.format(dataD);

        SimpleDateFormat formata_hora = new SimpleDateFormat("HH:mm");
        String hora_inicio = formata_hora.format(agendas.get(i).getHora_inicio());
        String hora_fim = formata_hora.format(agendas.get(i).getHora_fim());

        data.setText(dataString);
        tipoComida.setText("| Carro: " + agendas.get(i).getCarro().getId_carro() + "| Tipo comida: " + agendas.get(i).getCarro().getTipo_comida());
        horaInicio.setText(hora_inicio);
        horaFim.setText(hora_fim);

        return rowView;

    }

}
