package com.example.nulsehelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Loading extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		// ����Handler��postDelayed�������ȴ�10000������ִ��run������
		// ��Activity�����Ǿ�����Ҫʹ��Handler��������UI����ִ��һЩ��ʱ�¼���
		// ����Handler��post�����ȿ���ִ�к�ʱ�¼�Ҳ������һЩUI���µ����飬�ȽϺ��ã��Ƽ�ʹ��
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// �ȴ�10000��������ٴ�ҳ�棬����ʾ��½�ɹ�
				Loading.this.finish();
				Toast.makeText(getApplicationContext(), "��¼�ɹ�",
						Toast.LENGTH_SHORT).show();
			}
		}, 10000);
	}
}