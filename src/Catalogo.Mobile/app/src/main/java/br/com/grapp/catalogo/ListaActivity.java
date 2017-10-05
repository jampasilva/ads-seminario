package br.com.grapp.catalogo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.grapp.catalogo.adaptadores.MercadoriaCardAdaptador;
import br.com.grapp.catalogo.models.Mercadoria;

/**
 * Essa classe {ListaActivity} servirá como ponto de entrada em nossa aplicação,
 * também terá a função de apresentar uma lista reciclável de mercadorias
 * que serão recuperadas através de um Serviço Web
 */
public class ListaActivity extends AppCompatActivity {
    // Coleção de mercadoria
    private ArrayList<Mercadoria> _catalogo;

    // Adaptador dos dados para o RecyclerView
    private MercadoriaCardAdaptador _adaptador;

    // Visão em Lista (Reciclável)
    private RecyclerView _recyclerView;

    // Gerenciador de layout utilizado na Visão em Lista
    private RecyclerView.LayoutManager _layoutManager;

    /**
     * Ocorre no momento da criação da atividade, baseado no ciclo de vida
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista);

        // Recupera o widget referente à Lista Reciclável
        _recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Define o Toolbar para funcionar como uma barra de ação (ActionBar)
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Recupera o botão flutuante para associar ao evento de Click
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaActivity.this, DetalheActivity.class));
            }
        });

        // Cria uma lista de mercadorias (Catálogo)
        _catalogo = new ArrayList<>();

        // O adaptador receberá como fonte de dados o catálogo
        _adaptador = new MercadoriaCardAdaptador(_catalogo);

        // Utilizaremos como gerenciador de layout o LinearLayoutManager
        _layoutManager = new LinearLayoutManager(this);

        // Como cada item possuirá um tamanho fixo, informamos isso
        // para a Lista Reciclável por questões de performance
        _recyclerView.setHasFixedSize(true);

        // Seta o gerenciador para a lista
        _recyclerView.setLayoutManager(_layoutManager);

        // Seta o adaptador para a lista
        _recyclerView.setAdapter(_adaptador);
    }

    /**
     * Permite que o menu seja criado baseado, neste caso, em um xml de recurso (menu)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Cria o menu baseado no arquivo menu_lista.xml
        getMenuInflater().inflate(R.menu.menu_lista, menu);

        return true;
    }

    /**
     * Trata o selecionar de um item do menu
     * @param item Item selecionado
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Recupera o ID do item que foi selecionado
        int id = item.getItemId();

        // Se for o item correspondendete a ação recarregar
        if (id == R.id.acao_recarregar) {
            carregarCatalogo();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Ocorre no momento que a atividade está para ser apresentada
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Quando estiver para visualizar essa atividade, então
        // carregaremos o catálogo
        carregarCatalogo();
    }

    /**
     * Solicita através de uma requisição HTTP dados de um servidor remoto, que corresponderá
     * ao conteúdo apresentado por essa atividade
     */
    private void carregarCatalogo() {
        // Para melhorar a experiência do usuário apresentaremos uma tela de progresso
        final ProgressDialog progressoDlg = new ProgressDialog(this);
        progressoDlg.setTitle("Por favor espere");
        progressoDlg.setCancelable(false);
        progressoDlg.show();

        // Esse serviço quando solicitado (METHOD=GET) retorna um conteúdo em formato JSON
        String url = "http://2learning-svc.azurewebsites.net/api/mercadoria";

        // Criamos uma requisição que espera como retorno um array de objetos JSON
        JsonArrayRequest requisicao = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Popula a lista de mercadorias (catálogo)
                        popularCatalogo(response);

                        // Dispensa o progresso
                        progressoDlg.dismiss();
                        // Apresenta uma mensagem (Toast)
                        Toast.makeText(ListaActivity.this, "Dados atualizados", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Dispensa o progresso
                        progressoDlg.dismiss();
                        // Apresenta uma mensagem (Toast)
                        Toast.makeText(ListaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Como o processo será asíncrono, então criamos uma fila de requisições
        RequestQueue filaRequisicao = Volley.newRequestQueue(this);
        // Adicionamos a nossa requisição para que seja executada
        filaRequisicao.add(requisicao);
    }

    /**
     * Popula a coleção (ArrayList<Mercadoria>) baseado no array JSON retornado pelo serviço
     * @param dados Dados retornados pelo serviço web
     */
    private void popularCatalogo(JSONArray dados) {
        _catalogo.clear();

        try {
            // Para cada item no array (JSONArray) faça
            for (int i = 0; i < dados.length(); i++) {
                // Recupera o objeto (JSONObject)
                JSONObject m = dados.getJSONObject(i);

                // Transfere os dados para um objeto Mercadoria
                Mercadoria mercadoria = new Mercadoria(
                        m.getInt("id"),
                        m.getString("nome"),
                        m.getDouble("preco"));

                // Adiciona a mercadoria criada no catálogo
                _catalogo.add(mercadoria);
            }
        } catch (Exception e) {
            // Caso ocorra um erro, apresenta-se a mensagem (NÃO É UMA BOA PRÁTICA)
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Sinaliza ao adaptador que os dados (DataSet) mudaram
        _adaptador.notifyDataSetChanged();
    }
}
