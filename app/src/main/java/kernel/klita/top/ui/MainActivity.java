package kernel.klita.top.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataOutputStream;
import java.io.IOException;
import kernel.klita.top.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToggle = findViewById(R.id.btnToggle);
        TextView tvStatus = findViewById(R.id.tvStatus);

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAirplaneMode();
            }
        });
    }

    private void toggleAirplaneMode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(process.getOutputStream());

                    // Toggle OFF
                    os.writeBytes("settings put global airplane_mode_on 1\n");
                    os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE_CHANGED --ez state true\n");

                    // Wait
                    Thread.sleep(3000);

                    // Toggle ON
                    os.writeBytes("settings put global airplane_mode_on 0\n");
                    os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE_CHANGED --ez state false\n");

                    os.flush();
                    os.close();
                    process.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
