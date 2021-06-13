package com.example.phonary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ThaiVowelsFragment extends Fragment implements View.OnClickListener {

    final static int NUM_VOWELS = 32;
    public String vowels_fileName = "Thai_Vowels_List.txt";
    public String vowel_pronun_fileName = "Thai_Vowels_Phonetic_List.txt";
    public ViewGroup group_btnVowels;
    public ArrayList<String> thaiVowelsList = new ArrayList<>();
    public ArrayList<String> thaiVowelPronunList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_thai_vowels, container, false);

        // Bind views
        group_btnVowels = root.findViewById(R.id.group_btnVowels);

        readVowels();
        readVowelPronun();

        initVowelsButtons();

        return root;
    }

    private void readVowels() {
        BufferedReader input = null;
        try {
            InputStream iS = getResources().getAssets().open(vowels_fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
            String sCurrentLine;
            while ((sCurrentLine = reader.readLine()) != null) {
                thaiVowelsList.add(sCurrentLine);
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

    private void readVowelPronun() {
        BufferedReader input = null;
        try {
            InputStream iS = getResources().getAssets().open(vowel_pronun_fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
            String sCurrentLine;
            while ((sCurrentLine = reader.readLine()) != null) {
                thaiVowelPronunList.add(sCurrentLine);
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

    private void initVowelsButtons() {
        for (int i = 0; i < NUM_VOWELS; i++) {
            Button btnVowels_i = (Button) group_btnVowels.getChildAt(i);
            btnVowels_i.setOnClickListener(this);
            btnVowels_i.setText(thaiVowelsList.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        int btnPos = group_btnVowels.indexOfChild(v);
        // Prepare data bundle to letter-explaining activity
        Bundle bundle = new Bundle();
        bundle.putString("type", "Vowel");
        bundle.putString("language", "Thai");
        bundle.putString("letter", thaiVowelsList.get(btnPos));
        bundle.putString("pronunciation", thaiVowelPronunList.get(btnPos));
        bundle.putInt("position", btnPos);
        // Transfer to letter-explaining activity
        Intent intent = new Intent(getActivity(), ShowOralMovement.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}