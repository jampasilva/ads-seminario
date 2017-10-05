package br.com.grapp.catalogo.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.grapp.catalogo.R;
import br.com.grapp.catalogo.models.Mercadoria;


/**
 * Classe que servirá para adaptar os dados (DataSource) para a Visão (RecyclerView)
 */
public class MercadoriaCardAdaptador extends RecyclerView.Adapter<MercadoriaCardAdaptador.ViewHolder> {
    private ArrayList<Mercadoria> _mercadorias;

    /**
     * Representa a unidade de ligação entre os dados e o item da Visão (RecyclerView)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _nomeView;
        public TextView _precoView;

        public ViewHolder(View v) {
            super(v);

            // Recupera os widgets da visão (mercadoria_card.xml)
            _nomeView = (TextView) v.findViewById(R.id.nome);
            _precoView = (TextView) v.findViewById(R.id.preco);
        }
    }

    /**
     * Método de criação da Visão (RecyclerView)
     * @param mercadorias Coleção de mercadorias
     */
    public MercadoriaCardAdaptador(ArrayList<Mercadoria> mercadorias) {
        _mercadorias = mercadorias;
    }

    /**
     * Permite que uma visão seja criada baseada no xml mercadoria_card.xml, e por fim
     * associa a um objeto nosso ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MercadoriaCardAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Cria a visão
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.mercadoria_card, parent, false);

        // Cria a nossa unidade de ligação
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    /**
     * Permite associar uma mercadoria (Mercadoria) com nossa unidade de ligação (ViewHolder)
     * @param holder Objeto nosso de associação
     * @param position Posição da mercadoria (índice base zero)
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mercadoria m = _mercadorias.get(position);

        holder._nomeView.setText(m.getNome());
        holder._precoView.setText(String.format("R$ %.2f", m.getPreco()));
    }

    /**
     * Retorna o número de mercadorias existentes no catálogo (DataSource)
     * @return Número de mercadorias existentes
     */
    @Override
    public int getItemCount() {
        return _mercadorias.size();
    }
}
