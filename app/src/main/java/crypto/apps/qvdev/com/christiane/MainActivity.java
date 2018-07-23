package crypto.apps.qvdev.com.christiane;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @NonNull
    private Runnable getRunnableCreateWallet() {
        return new Runnable() {
            @Override
            public void run() {
                String walletFile = null; //create string to store wallet data

                File folder = getFolder();

                try {
                    walletFile = WalletUtils.generateLightNewWalletFile("aaa", folder);
                } catch (NoSuchAlgorithmException | NoSuchProviderException | CipherException | InvalidAlgorithmParameterException | IOException e) {
                    e.printStackTrace();
                }

                if (walletFile != null) { //if wallet was created successfully
                    Log.d(getClass().getSimpleName(), "wallet created::" + walletFile);
                    Credentials credentials;
                    try {
                        credentials = WalletUtils.loadCredentials("aaa", Environment.getExternalStorageDirectory() +
                                File.separator + WalletUtils.getTestnetKeyDirectory() + File.separator + walletFile);
                        Log.d(getClass().getSimpleName(), credentials.getAddress());
                    } catch (IOException | CipherException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.d(getClass().getSimpleName(), "wallet not created");
                }
            }
        };
    }

    @NonNull
    private File getFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + WalletUtils.getTestnetKeyDirectory());
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Log.d(getClass().getSimpleName(), "directory created");
        } else {
            Log.d(getClass().getSimpleName(), "directory failed");
        }
        return folder;
    }

    public void createWallet(View view) {
        Runnable runnable = getRunnableCreateWallet();
        new Thread(runnable).start();
//        runnable.run();
    }
}
