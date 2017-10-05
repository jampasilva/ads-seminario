package br.com.grapp.catalogo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Essa atividade {DetalheActivity} representa a visão em detalhe de uma mercadoria,
 * mas para esse DEMO utilizaremos apenas para permitir criar novas mercadorias.
 */
public class DetalheActivity extends AppCompatActivity {
    // Todos os widgets serão recuperados e armazenados
    //  em variáveis de instância
    private EditText _nomeView;
    private EditText _precoView;
    private Button _acaoSalvar;

    /**
     * Ocorre no momento da criação da atividade, baseado no ciclo de vida
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalhe);

        // Recupera os widgets (2 x EditText, 1 x Button
        _nomeView = (EditText) findViewById(R.id.nome);
        _precoView = (EditText) findViewById(R.id.preco);
        _acaoSalvar = (Button) findViewById(R.id.acaoSalvar);

        // Registra o manipulador do evento Click do botão
        _acaoSalvar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Solicita que a mercadoria seja persistida
                        executarSalvar();
                    }
                });
    }

    /**
     * Representa a ação que executará a validação e por fim a solicitação
     * para que a mercadoria seja persistida
     */
    private void executarSalvar() {
        // Remove erros (textos) associados aos widgets
        _nomeView.setError(null);
        _precoView.setError(null);

        // Recupera os valor informados
        String nome = _nomeView.getText().toString();
        String preco = _precoView.getText().toString();

        // Faremos as validações, havendo associaremos um erro e solicitamores o foco
        //  O nome não poderá ser vazio
        if (TextUtils.isEmpty(nome)) {
            _nomeView.setError(getString(R.string.erro_nome_invalido));
            _nomeView.requestFocus();

        // O preço deve ser válido (preço > zero)
        } else if (!ehPrecoValido(preco)) {
            _precoView.setError(getString(R.string.erro_preco_invalido));
            _precoView.requestFocus();
        } else {
            // Solicita que uma nova mercadoria seja persistida
            persistirNovaMercadoria(nome, Double.valueOf(preco));
        }
    }

    /**
     * Executa uma chamada HTTP utilizando o Volley API para inserir uma nova
     * mercadoria
     * @param nome Nome da mercadoria
     * @param preco Preço da mercadoria
     */
    private void persistirNovaMercadoria(String nome, double preco) {
        // Para melhorar a experiência do usuário apresentaremos uma tela de progresso
        final ProgressDialog progressoDlg = new ProgressDialog(this);
        progressoDlg.setTitle("Por favor espere");
        progressoDlg.setCancelable(false);
        progressoDlg.show();

        // Solicitamos a criação de uma mercadoria em forma JSON
        JSONObject mercadoria = criarMercadoriaComoJson(nome, preco);

        // Esse serviço quando solicitado (METHOD=POST) insere
        // uma nova mercadoria e retorno o seu ID em formato JSON
        String url = "http://2learning-svc.azurewebsites.net/api/mercadoria";

        // Criamos uma requisição que espera como retorno um objetos JSON
        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.POST,
                url,
                mercadoria,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Com sucesso finaliza essa atividade
                        DetalheActivity.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Dispensa o progresso
                        progressoDlg.dismiss();
                        // Apresenta uma mensagem (Toast)
                        Toast.makeText(DetalheActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Como o processo será asíncrono, então criamos uma fila de requisições
        RequestQueue filaRequisicao = Volley.newRequestQueue(this);
        // Adicionamos a nossa requisição para que seja executada
        filaRequisicao.add(requisicao);
    }

    /**
     * Cira um objeto JSON contento os valores informados pelos parâmetros
     * @param nome Nome da mercadoria
     * @param preco Preço da mercadoria
     * @return Objeto JSON
     */
    private JSONObject criarMercadoriaComoJson(String nome, double preco) {
        try {
            // Cria um objeto "JSON" e adiciona os campos "nome" e "preco"
            JSONObject mercadoria = new JSONObject();
            mercadoria.put("nome", nome);
            mercadoria.put("preco", preco);

            // Retorna a mercadoria criada
            return mercadoria;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retorna um valor booleano indicando se o preço informado pelo parâmetro
     * é válido
     * @param preco Preço que será validado
     * @return Retorna {true} se o informado for válido senão {false}
     */
    private boolean ehPrecoValido(String preco) {
        try {
            double vl = Double.valueOf(preco);

            return vl > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

