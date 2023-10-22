package com.tcc.beautyplannercliente;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tcc.beautyplannercliente.agendamento.FuncoesFragment;
import com.tcc.beautyplannercliente.fragment.FragmentCalendario;
import com.tcc.beautyplannercliente.fragment.FragmentHome;
import com.tcc.beautyplannercliente.util.Permissao;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    private FragmentHome fragmentHome;
    private FragmentCalendario fragmentCalendario;
    private FuncoesFragment fragmentFuncoes;
    //private FuncoesBuscarServicoActivity fragmentFuncoes;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);


        navigationBottom();


        fragmentHome = new FragmentHome();
        fragmentCalendario = new FragmentCalendario();
        fragmentFuncoes = new FuncoesFragment();
     //   fragmentFuncoes = new FuncoesBuscarServicoActivity();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frameLayout_Fragment,fragmentHome).commit();

        permissao();

    }

    private void permissao(){


        String permissoes[] = new String[]{

                android.Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS
        };


        Permissao.validate(this,333,permissoes);


    }

    private void navigationBottom(){


        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.item_navegacao_home:

                        fragment = fragmentHome;
                        break;

                    case R.id.item_navegacao_calendario:

                         fragment = fragmentCalendario;
                         //fragment = fragmentFuncoes;

                         break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment,fragment).commit();
                return true;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


    }
}