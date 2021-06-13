package com.example.phonary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ThaiPhonemesFragment extends Fragment implements View.OnClickListener {

    final static int NUM_PHONEMES = 21;
    public String phoneme_fileName = "Thai_Phonemes_List.txt";
    public String phoneme_pronun_fileName = "Thai_Phonemes_Phonetic_List.txt"; //thaiPhonemePhoneticList.txt
    public ViewGroup group_btnPhonemes;
    public ArrayList<String> thaiPhonemesList = new ArrayList<>();
    public ArrayList<String> thaiPhonemePronunList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_thai_phonemes, container, false);

        // Bind views
        group_btnPhonemes = root.findViewById(R.id.group_btnPhonemes);

        readPhonemes();
        readPhonemePronun();

        initPhonemesButtons();

        return root;
    }

    private void readPhonemes() {
        BufferedReader input = null;
        try {
            InputStream iS = getResources().getAssets().open(phoneme_fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
            String sCurrentLine;
            while ((sCurrentLine = reader.readLine()) != null) {
                thaiPhonemesList.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null){
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void readPhonemePronun() {
        BufferedReader input = null;
        try {
            InputStream iS = getResources().getAssets().open(phoneme_pronun_fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
            String sCurrentLine = null;
            while ((sCurrentLine = reader.readLine()) != null) {
                thaiPhonemePronunList.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null){
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void initPhonemesButtons() {
        for (int i = 0; i < NUM_PHONEMES; i++) {
            Button btnPhoneme_i = (Button) group_btnPhonemes.getChildAt(i);
            btnPhoneme_i.setOnClickListener(this);
            btnPhoneme_i.setText(thaiPhonemesList.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        int btnPos = group_btnPhonemes.indexOfChild(v);
        // Prepare data bundle to letter-explaining activity
        Bundle bundle = new Bundle();
        bundle.putString("type", "Phoneme");
        bundle.putString("language", "Thai");
        bundle.putString("letter", thaiPhonemesList.get(btnPos));
        bundle.putString("pronunciation", thaiPhonemePronunList.get(btnPos));
        bundle.putInt("position", btnPos);
        // Transfer to letter-explaining activity
        Intent intent = new Intent(getActivity(), ShowOralMovement.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}